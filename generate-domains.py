#!/usr/bin/env python3
"""
Generator script to convert domains.json to DomainsData.java
This eliminates the need for runtime JSON parsing and Jackson dependency.

The domain list is split across multiple helper methods so that each method's
bytecode stays well under the JVM's 64 KB per-method limit. Without this split,
once the upstream list grew past ~5-6k domains, javac would fail with
"code too large" on the static initializer.
"""

import json
import os

# Number of domains to add per helper method. With ~10 bytes of bytecode per
# `add(String)` call, 1000 entries leaves a comfortable margin under the 64 KB
# (65535 byte) per-method ceiling and accommodates future upstream growth.
BATCH_SIZE = 1000


def generate_domains_java():
    with open('src/main/resources/domains.json', 'r') as f:
        domains = json.load(f)

    domains.sort()

    total = len(domains)
    batches = [domains[i:i + BATCH_SIZE] for i in range(0, total, BATCH_SIZE)]

    lines = []
    lines.append('package com.henricook.freeemaildomains.internal;')
    lines.append('')
    lines.append('import java.util.Collections;')
    lines.append('import java.util.HashSet;')
    lines.append('import java.util.Set;')
    lines.append('')
    lines.append('/**')
    lines.append(' * Generated class containing all free email domains.')
    lines.append(' * This class is automatically generated from domains.json.')
    lines.append(' * Do not edit manually.')
    lines.append(' *')
    lines.append(f' * Total domains: {total}')
    lines.append(' */')
    lines.append('public final class DomainsData {')
    lines.append('')
    lines.append('    public static final Set<String> DOMAINS;')
    lines.append('')
    lines.append('    static {')
    lines.append(f'        Set<String> domains = new HashSet<>({total});')
    for index, _ in enumerate(batches):
        lines.append(f'        addBatch{index}(domains);')
    lines.append('        DOMAINS = Collections.unmodifiableSet(domains);')
    lines.append('    }')

    for index, batch in enumerate(batches):
        lines.append('')
        lines.append(f'    private static void addBatch{index}(Set<String> domains) {{')
        for domain in batch:
            escaped = domain.replace('\\', '\\\\').replace('"', '\\"')
            lines.append(f'        domains.add("{escaped}");')
        lines.append('    }')

    lines.append('')
    lines.append('    private DomainsData() {')
    lines.append('    }')
    lines.append('}')
    lines.append('')

    output_dir = 'src/main/java/com/henricook/freeemaildomains/internal'
    os.makedirs(output_dir, exist_ok=True)

    output_file = os.path.join(output_dir, 'DomainsData.java')
    with open(output_file, 'w') as f:
        f.write('\n'.join(lines))

    print(f"Generated {output_file} with {total} domains across {len(batches)} batch method(s)")


if __name__ == '__main__':
    generate_domains_java()
