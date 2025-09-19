# free-email-domains-java

A Java port of the [Kikobeats/free-email-domains](https://github.com/Kikobeats/free-email-domains) JavaScript library for detecting free email providers.

## Project Overview

This library helps distinguish between free email providers (Gmail, Yahoo, etc.) and business/corporate email addresses. It's published to Maven Central and automatically syncs with the upstream JavaScript library.

**Package**: `com.henricook.freeemaildomains`
**Maven Central**: `com.henricook:free-email-domains-java`
**Current Version**: 1.2.21

## Architecture

### Zero Dependencies Design
- **No runtime JSON parsing** - domains are compiled into Java source code
- **No Jackson dependency** - eliminated to keep library lightweight
- **Java 8 compatible** - uses `HashSet` + `Collections.unmodifiableSet()` instead of `Set.of()`

### Key Files

- **`src/main/java/com/henricook/freeemaildomains/FreeEmailDomains.java`**: Main API class
- **`src/main/java/com/henricook/freeemaildomains/internal/DomainsData.java`**: Generated class with 4,779 domains as static Set
- **`generate-domains.py`**: Python script that converts `domains.json` to Java source code
- **`src/main/resources/domains.json`**: Source data from upstream (build-time only)

### Code Generation Process
1. `domains.json` downloaded from upstream Kikobeats repository
2. `generate-domains.py` converts JSON array to Java `HashSet` initialization
3. Domains compiled into bytecode at build time (no runtime parsing)

## GitHub Actions Workflows

### CI (`ci.yml`)
- Tests on Java 8, 11, 17, 21
- Builds and uploads artifacts
- Uses latest actions: checkout@v5, setup-java@v5

### Release (`release.yml`)
- Triggered by version tags (`v*`)
- Publishes to Maven Central with GPG signing
- Creates GitHub releases with artifacts
- Uses `softprops/action-gh-release@v2` (modern replacement for deprecated actions)

### Sync Upstream (`sync-upstream.yml`)
- Runs weekly (Sundays 2 AM UTC) + manual trigger
- Downloads latest `domains.json` from upstream
- Regenerates `DomainsData.java` if changes detected
- Auto-creates PR and merges if tests pass
- Triggers release workflow for new versions

## Maven Central Publishing

**Sonatype Central Portal** (new system, not legacy OSSRH)
- Namespace: `com.henricook` (verified)
- GPG signing required
- Automatic publishing on successful deployment

### Required Secrets
- `SONATYPE_USERNAME` / `SONATYPE_PASSWORD`
- `GPG_PRIVATE_KEY` / `GPG_PASSPHRASE`

## Usage Examples

```java
import com.henricook.freeemaildomains.FreeEmailDomains;

// Check emails
FreeEmailDomains.isFree("user@gmail.com");        // true
FreeEmailDomains.isFree("employee@company.com");  // false

// Check domains
FreeEmailDomains.isFree("yahoo.com");             // true

// Get all domains
Set<String> allDomains = FreeEmailDomains.getAllDomains();
```

## Commands to Remember

### Local Development
```bash
# Run tests
mvn clean test

# Build package
mvn clean package

# Install locally
mvn clean install

# Regenerate domains
python3 generate-domains.py
```

### Release Process
```bash
# Create release tag (triggers CI/CD)
git tag v1.2.22
git push origin v1.2.22
```

## Performance Characteristics

- **JAR Size**: ~87KB (vs 30KB + 2MB Jackson dependency before optimization)
- **Startup**: No initialization penalty (compiled constants)
- **Lookup**: O(1) HashSet contains() operation
- **Memory**: ~4,779 String objects loaded on first use
- **Thread Safety**: Immutable Set, fully thread-safe

## Testing

- **24 tests total** covering edge cases, thread safety, performance
- **JUnit 5** test framework
- **Nested test classes** for organization
- **Coverage**: Basic functionality, domain extraction, thread safety, performance

## Historical Notes

### Major Refactoring (Initial Design → Code Generation)
Originally used Jackson to parse JSON at runtime, but this was questioned and completely refactored to:
- Generate Java source at build time
- Eliminate runtime dependencies
- Improve performance and reduce complexity

### GitHub Actions Updates
Updated all actions to latest versions and replaced deprecated `actions/create-release` + `actions/upload-release-asset` with modern `softprops/action-gh-release@v2`.

## Domain Count
Current: **4,779 domains** (as of version 1.2.21)
Updates automatically when upstream changes.