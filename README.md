# free-email-domains-java

[![Maven Central](https://img.shields.io/maven-central/v/com.henricook/free-email-domains-java.svg)](https://central.sonatype.com/artifact/com.henricook/free-email-domains-java)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A comprehensive Java library for identifying free email domain providers (Gmail, Yahoo, Outlook, etc.). This is a Java port of the popular [free-email-domains](https://github.com/Kikobeats/free-email-domains) JavaScript library by [Kiko Beats](https://github.com/Kikobeats).

Based on [HubSpot's blocked domains list](https://knowledge.hubspot.com/articles/kcs_article/forms/what-domains-are-blocked-when-using-the-forms-email-domains-to-block-feature), this library provides fast, reliable detection of personal vs. business email addresses.

## Features

- ✅ **Fast O(1) lookups** - Efficient HashSet-based domain checking
- ✅ **Comprehensive coverage** - 4,700+ free email domains
- ✅ **Thread-safe** - Safe for concurrent use
- ✅ **Zero dependencies** - Pure Java with no external dependencies
- ✅ **Java 8+ compatible** - Works with any modern Java or Scala project
- ✅ **Auto-sync** - Automatically updated when upstream changes
- ✅ **Lightweight** - Small JAR size with minimal footprint

## Installation

### Maven
```xml
<dependency>
    <groupId>com.henricook</groupId>
    <artifactId>free-email-domains-java</artifactId>
    <version>1.2.21</version>
</dependency>
```

### Gradle
```groovy
implementation 'com.henricook:free-email-domains-java:1.2.21'
```

### Scala SBT
```scala
libraryDependencies += "com.henricook" % "free-email-domains-java" % "1.2.21"
```

## Usage

### Basic Usage

```java
import com.henricook.freeemaildomains.FreeEmailDomains;

// Check if an email is from a free provider
boolean isFree = FreeEmailDomains.isFree("user@gmail.com");
// Returns: true

boolean isBusiness = !FreeEmailDomains.isFree("user@company.com");
// Returns: true

// Works with domains too
boolean isFreeDomain = FreeEmailDomains.isFree("yahoo.com");
// Returns: true
```

### Advanced Usage

```java
import com.henricook.freeemaildomains.FreeEmailDomains;
import java.util.Set;

// Get all free email domains
Set<String> allFreeDomains = FreeEmailDomains.all();
System.out.println("Total free domains: " + allFreeDomains.size());

// Extract domain from email
String domain = FreeEmailDomains.extractDomain("user@example.com");
// Returns: "example.com"

// Business logic example
public boolean isBusinessEmail(String email) {
    return !FreeEmailDomains.isFree(email);
}

// Filter business emails from a list
List<String> businessEmails = emailList.stream()
    .filter(email -> !FreeEmailDomains.isFree(email))
    .collect(Collectors.toList());
```

### Scala Usage

The library works seamlessly with Scala:

```scala
import com.henricook.freeemaildomains.FreeEmailDomains

// Basic usage
val isFree = FreeEmailDomains.isFree("user@gmail.com") // true
val isBusiness = !FreeEmailDomains.isFree("user@company.com") // true

// Functional style
val emails = List("user@gmail.com", "admin@company.com", "test@yahoo.com")
val (freeEmails, businessEmails) = emails.partition(FreeEmailDomains.isFree)
```

## Use Cases

- **B2B SaaS applications** - Require business emails for trials/signups
- **Lead scoring** - Score business emails higher than personal ones
- **CRM systems** - Categorize contacts as business vs. personal
- **Marketing automation** - Different workflows for B2B vs. B2C leads
- **Form validation** - Block personal emails in business contexts
- **Analytics** - Distinguish between business and consumer users

## Supported Domains

The library detects 4,700+ free email providers including:

- **Popular providers**: gmail.com, yahoo.com, outlook.com, hotmail.com
- **International providers**: yandex.ru, mail.ru, qq.com, 163.com
- **Temporary email services**: 10minutemail.com, guerrillamail.com
- **Privacy-focused**: protonmail.com, tutanota.com
- **Many more...** - See [DomainsData.java](src/main/java/com/henricook/freeemaildomains/internal/DomainsData.java) for the complete list

## Performance

- **Initialization**: Lazy-loaded on first use
- **Lookup speed**: O(1) - typically under 1 microsecond
- **Memory usage**: ~500KB for domain storage
- **Thread safety**: Full concurrent access support

Benchmark (10,000 lookups):
```
Average time per lookup: ~0.1 microseconds
Total time for 10,000 lookups: ~1ms
```

## API Reference

### `FreeEmailDomains.isFree(String emailOrDomain)`
Check if email address or domain is from a free provider.

**Parameters:**
- `emailOrDomain` - Email address (user@domain.com) or domain (domain.com)

**Returns:** `boolean` - true if free provider, false otherwise

**Throws:** `IllegalArgumentException` if input is null/empty or invalid

---

### `FreeEmailDomains.all()`
Get all free email domains as an immutable set.

**Returns:** `Set<String>` - Immutable set of all free domain names (lowercase)

---

### `FreeEmailDomains.extractDomain(String emailOrDomain)`
Extract domain from email address or return as-is if already a domain.

**Parameters:**
- `emailOrDomain` - Email address or domain name

**Returns:** `String` - The domain part

**Throws:** `IllegalArgumentException` if input is null/empty or invalid

## Requirements

- **Java 8+** or **Scala 2.12+**
- **No external dependencies** - Pure Java

## Versioning

This library follows the upstream [free-email-domains](https://github.com/Kikobeats/free-email-domains) versioning. The version number matches the upstream JavaScript library to indicate data compatibility.

## Updates

The domain list is automatically synchronized with the upstream repository weekly. When new domains are added upstream, this library will be updated and published automatically.

## Contributing

Contributions are welcome! Please note that domain additions should be made to the upstream [free-email-domains](https://github.com/Kikobeats/free-email-domains) repository. This library automatically syncs with those changes.

For library-specific improvements:
1. Fork this repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

MIT License - see [LICENSE](LICENSE) file for details.

## Credits

- **Domain data**: [free-email-domains](https://github.com/Kikobeats/free-email-domains) by [Kiko Beats](https://github.com/Kikobeats)
- **Original data source**: [HubSpot blocked domains list](https://knowledge.hubspot.com/articles/kcs_article/forms/what-domains-are-blocked-when-using-the-forms-email-domains-to-block-feature)
- **Java implementation**: This library

## Related Projects

- [free-email-domains](https://github.com/Kikobeats/free-email-domains) - Original JavaScript library
- [business-email-validator](https://github.com/LoginRadius/business-email-validator) - JavaScript business email validator
- [disposable-email-domains](https://github.com/disposable-email-domains/disposable-email-domains) - Disposable/temporary email detection