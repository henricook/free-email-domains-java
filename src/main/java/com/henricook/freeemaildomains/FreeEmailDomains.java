package com.henricook.freeemaildomains;

import com.henricook.freeemaildomains.internal.DomainsData;

import java.util.Locale;
import java.util.Set;

/**
 * A comprehensive utility for identifying free email domain providers.
 *
 * This library provides a simple API to check if an email address or domain
 * belongs to a free email provider (like Gmail, Yahoo, Outlook, etc.) based on
 * the comprehensive list maintained by HubSpot and used by the free-email-domains
 * JavaScript library by Kiko Beats.
 *
 * <p>Usage examples:</p>
 * <pre>
 * // Check if an email is from a free provider
 * boolean isFree = FreeEmailDomains.isFree("user@gmail.com"); // true
 * boolean isBusiness = !FreeEmailDomains.isFree("user@company.com"); // true
 *
 * // Check a domain directly
 * boolean isFreeDomain = FreeEmailDomains.isFree("yahoo.com"); // true
 *
 * // Get all free domains
 * Set&lt;String&gt; allDomains = FreeEmailDomains.all();
 * </pre>
 *
 * <p>This library is thread-safe and performs efficient O(1) lookups.</p>
 *
 * @author Henri
 * @since 1.2.21
 */
public final class FreeEmailDomains {

    private FreeEmailDomains() {
        // Utility class
    }

    /**
     * Checks if the given email address or domain belongs to a free email provider.
     *
     * <p>This method accepts either a full email address (user@domain.com) or just
     * a domain name (domain.com). The comparison is case-insensitive.</p>
     *
     * @param emailOrDomain the email address or domain to check (e.g., "user@gmail.com" or "gmail.com")
     * @return true if the email/domain is from a free provider, false otherwise
     * @throws IllegalArgumentException if the input is null or empty
     */
    public static boolean isFree(String emailOrDomain) {
        if (emailOrDomain == null || emailOrDomain.trim().isEmpty()) {
            throw new IllegalArgumentException("Email or domain cannot be null or empty");
        }

        String domain = extractDomain(emailOrDomain.trim());
        return DomainsData.DOMAINS.contains(domain.toLowerCase(Locale.ROOT));
    }

    /**
     * Returns an immutable set containing all free email domains.
     *
     * <p>The returned set contains domain names in lowercase and can be used
     * for custom filtering or validation logic.</p>
     *
     * @return an immutable set of all free email domain names
     */
    public static Set<String> all() {
        return DomainsData.DOMAINS;
    }

    /**
     * Extracts the domain part from an email address or returns the input if it's already a domain.
     *
     * <p>Examples:</p>
     * <ul>
     *   <li>"user@example.com" → "example.com"</li>
     *   <li>"example.com" → "example.com"</li>
     *   <li>"EXAMPLE.COM" → "EXAMPLE.COM" (case preserved)</li>
     * </ul>
     *
     * @param emailOrDomain the email address or domain
     * @return the domain part
     * @throws IllegalArgumentException if the input is null or empty
     */
    public static String extractDomain(String emailOrDomain) {
        if (emailOrDomain == null || emailOrDomain.trim().isEmpty()) {
            throw new IllegalArgumentException("Email or domain cannot be null or empty");
        }

        String trimmed = emailOrDomain.trim();
        int atIndex = trimmed.lastIndexOf('@');

        if (atIndex == -1) {
            // No @ symbol, assume it's already a domain
            return trimmed;
        }

        if (atIndex == trimmed.length() - 1) {
            // @ is at the end, invalid
            throw new IllegalArgumentException("Invalid email format: " + emailOrDomain);
        }

        return trimmed.substring(atIndex + 1);
    }
}