package com.henricook.freeemaildomains.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Internal utility class for loading free email domains from JSON resource.
 * This class is not part of the public API and should not be used directly.
 */
public final class DomainsLoader {

    private static final String DOMAINS_RESOURCE = "/domains.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private DomainsLoader() {
        // Utility class
    }

    /**
     * Loads the free email domains from the JSON resource file.
     * Returns an immutable set of domain names in lowercase.
     *
     * @return Set of free email domain names
     * @throws RuntimeException if the resource cannot be loaded or parsed
     */
    public static Set<String> loadDomains() {
        try (InputStream inputStream = DomainsLoader.class.getResourceAsStream(DOMAINS_RESOURCE)) {
            if (inputStream == null) {
                throw new RuntimeException("Could not find domains resource: " + DOMAINS_RESOURCE);
            }

            List<String> domainsList = MAPPER.readValue(inputStream, new TypeReference<List<String>>() {});

            if (domainsList == null || domainsList.isEmpty()) {
                throw new RuntimeException("Domains resource is empty or invalid");
            }

            // Convert to lowercase and create immutable set for fast lookups
            Set<String> domains = new HashSet<>(domainsList.size());
            for (String domain : domainsList) {
                if (domain != null && !domain.trim().isEmpty()) {
                    domains.add(domain.toLowerCase().trim());
                }
            }

            return Collections.unmodifiableSet(domains);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load free email domains", e);
        }
    }
}