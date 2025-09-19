#!/usr/bin/env python3
"""
Generator script to convert domains.json to DomainsData.java
This eliminates the need for runtime JSON parsing and Jackson dependency.
"""

import json
import os

def generate_domains_java():
    # Read domains.json
    with open('src/main/resources/domains.json', 'r') as f:
        domains = json.load(f)

    # Sort domains for deterministic output
    domains.sort()

    # Create the Java source code
    java_source = f'''package com.henricook.freeemaildomains.internal;

import java.util.Set;

/**
 * Generated class containing all free email domains.
 * This class is automatically generated from domains.json.
 * Do not edit manually.
 *
 * Total domains: {len(domains)}
 */
public final class DomainsData {{

    /**
     * Immutable set of all free email domain names in lowercase.
     */
    public static final Set<String> DOMAINS = Set.of(
'''

    # Add all domains, properly escaped and quoted
    for i, domain in enumerate(domains):
        # Escape any quotes in domain names
        escaped_domain = domain.replace('"', '\\"')

        # Add comma except for last item
        comma = "," if i < len(domains) - 1 else ""
        java_source += f'        "{escaped_domain}"{comma}\n'

    java_source += '''    );

    private DomainsData() {
        // Utility class
    }
}
'''

    # Ensure output directory exists
    output_dir = 'src/main/java/com/henricook/freeemaildomains/internal'
    os.makedirs(output_dir, exist_ok=True)

    # Write the generated Java file
    output_file = os.path.join(output_dir, 'DomainsData.java')
    with open(output_file, 'w') as f:
        f.write(java_source)

    print(f"Generated {output_file} with {len(domains)} domains")

if __name__ == '__main__':
    generate_domains_java()