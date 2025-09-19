package com.henricook.freeemaildomains;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for FreeEmailDomains utility class.
 */
class FreeEmailDomainsTest {

    @Nested
    @DisplayName("isFree() method tests")
    class IsFreeTests {

        @Test
        @DisplayName("Should return true for known free email domains")
        void shouldReturnTrueForKnownFreeEmailDomains() {
            // Test popular free email providers
            assertTrue(FreeEmailDomains.isFree("gmail.com"));
            assertTrue(FreeEmailDomains.isFree("yahoo.com"));
            assertTrue(FreeEmailDomains.isFree("outlook.com"));
            assertTrue(FreeEmailDomains.isFree("hotmail.com"));
            assertTrue(FreeEmailDomains.isFree("aol.com"));
            assertTrue(FreeEmailDomains.isFree("icloud.com"));
            assertTrue(FreeEmailDomains.isFree("protonmail.com"));
        }

        @Test
        @DisplayName("Should return true for full email addresses with free domains")
        void shouldReturnTrueForFullEmailAddressesWithFreeDomains() {
            assertTrue(FreeEmailDomains.isFree("user@gmail.com"));
            assertTrue(FreeEmailDomains.isFree("test.email@yahoo.com"));
            assertTrue(FreeEmailDomains.isFree("someone@outlook.com"));
            assertTrue(FreeEmailDomains.isFree("user123@hotmail.com"));
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(FreeEmailDomains.isFree("GMAIL.COM"));
            assertTrue(FreeEmailDomains.isFree("Gmail.Com"));
            assertTrue(FreeEmailDomains.isFree("user@YAHOO.COM"));
            assertTrue(FreeEmailDomains.isFree("USER@Outlook.COM"));
        }

        @Test
        @DisplayName("Should return false for business/corporate domains")
        void shouldReturnFalseForBusinessDomains() {
            assertFalse(FreeEmailDomains.isFree("company.com"));
            assertFalse(FreeEmailDomains.isFree("business.org"));
            assertFalse(FreeEmailDomains.isFree("user@mycompany.com"));
            assertFalse(FreeEmailDomains.isFree("employee@corporation.net"));
            assertFalse(FreeEmailDomains.isFree("example.com")); // RFC example domain
        }

        @Test
        @DisplayName("Should handle emails with multiple @ symbols")
        void shouldHandleMultipleAtSymbols() {
            // Should use the last @ symbol
            assertTrue(FreeEmailDomains.isFree("user@domain@gmail.com"));
            assertFalse(FreeEmailDomains.isFree("user@domain@company.com"));
        }

        @Test
        @DisplayName("Should handle whitespace")
        void shouldHandleWhitespace() {
            assertTrue(FreeEmailDomains.isFree("  gmail.com  "));
            assertTrue(FreeEmailDomains.isFree("  user@yahoo.com  "));
            assertTrue(FreeEmailDomains.isFree("\toutlook.com\n"));
        }

        @Test
        @DisplayName("Should throw exception for null input")
        void shouldThrowExceptionForNullInput() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> FreeEmailDomains.isFree(null)
            );
            assertEquals("Email or domain cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for empty input")
        void shouldThrowExceptionForEmptyInput() {
            IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> FreeEmailDomains.isFree("")
            );
            assertEquals("Email or domain cannot be null or empty", exception1.getMessage());

            IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () -> FreeEmailDomains.isFree("   ")
            );
            assertEquals("Email or domain cannot be null or empty", exception2.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for invalid email format")
        void shouldThrowExceptionForInvalidEmailFormat() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> FreeEmailDomains.isFree("user@")
            );
            assertTrue(exception.getMessage().contains("Invalid email format"));
        }
    }

    @Nested
    @DisplayName("extractDomain() method tests")
    class ExtractDomainTests {

        @Test
        @DisplayName("Should extract domain from email address")
        void shouldExtractDomainFromEmailAddress() {
            assertEquals("gmail.com", FreeEmailDomains.extractDomain("user@gmail.com"));
            assertEquals("yahoo.com", FreeEmailDomains.extractDomain("test.email@yahoo.com"));
            assertEquals("company.com", FreeEmailDomains.extractDomain("employee@company.com"));
        }

        @Test
        @DisplayName("Should return domain when already a domain")
        void shouldReturnDomainWhenAlreadyADomain() {
            assertEquals("gmail.com", FreeEmailDomains.extractDomain("gmail.com"));
            assertEquals("company.com", FreeEmailDomains.extractDomain("company.com"));
            assertEquals("example.org", FreeEmailDomains.extractDomain("example.org"));
        }

        @Test
        @DisplayName("Should preserve case")
        void shouldPreserveCase() {
            assertEquals("Gmail.Com", FreeEmailDomains.extractDomain("user@Gmail.Com"));
            assertEquals("COMPANY.COM", FreeEmailDomains.extractDomain("COMPANY.COM"));
        }

        @Test
        @DisplayName("Should handle multiple @ symbols")
        void shouldHandleMultipleAtSymbols() {
            assertEquals("gmail.com", FreeEmailDomains.extractDomain("user@domain@gmail.com"));
            assertEquals("final.com", FreeEmailDomains.extractDomain("a@b@c@final.com"));
        }

        @Test
        @DisplayName("Should handle whitespace")
        void shouldHandleWhitespace() {
            assertEquals("gmail.com", FreeEmailDomains.extractDomain("  user@gmail.com  "));
            assertEquals("company.com", FreeEmailDomains.extractDomain("  company.com  "));
        }

        @Test
        @DisplayName("Should throw exception for null input")
        void shouldThrowExceptionForNullInput() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> FreeEmailDomains.extractDomain(null)
            );
            assertEquals("Email or domain cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for empty input")
        void shouldThrowExceptionForEmptyInput() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> FreeEmailDomains.extractDomain("")
            );
            assertEquals("Email or domain cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for invalid email format")
        void shouldThrowExceptionForInvalidEmailFormat() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> FreeEmailDomains.extractDomain("user@")
            );
            assertTrue(exception.getMessage().contains("Invalid email format"));
        }
    }

    @Nested
    @DisplayName("all() method tests")
    class AllTests {

        @Test
        @DisplayName("Should return non-empty set of domains")
        void shouldReturnNonEmptySetOfDomains() {
            Set<String> domains = FreeEmailDomains.all();
            assertNotNull(domains);
            assertFalse(domains.isEmpty());
            assertTrue(domains.size() > 1000); // Should have many domains
        }

        @Test
        @DisplayName("Should contain known free email domains")
        void shouldContainKnownFreeEmailDomains() {
            Set<String> domains = FreeEmailDomains.all();
            assertTrue(domains.contains("gmail.com"));
            assertTrue(domains.contains("yahoo.com"));
            assertTrue(domains.contains("outlook.com"));
            assertTrue(domains.contains("hotmail.com"));
            assertTrue(domains.contains("aol.com"));
        }

        @Test
        @DisplayName("Should return immutable set")
        void shouldReturnImmutableSet() {
            Set<String> domains = FreeEmailDomains.all();
            assertThrows(UnsupportedOperationException.class, () -> domains.add("test.com"));
            assertThrows(UnsupportedOperationException.class, () -> domains.remove("gmail.com"));
            assertThrows(UnsupportedOperationException.class, domains::clear);
        }

        @Test
        @DisplayName("Should return same instance on multiple calls")
        void shouldReturnSameInstanceOnMultipleCalls() {
            Set<String> domains1 = FreeEmailDomains.all();
            Set<String> domains2 = FreeEmailDomains.all();
            assertSame(domains1, domains2); // Should be same instance due to caching
        }

        @Test
        @DisplayName("Should contain only lowercase domains")
        void shouldContainOnlyLowercaseDomains() {
            Set<String> domains = FreeEmailDomains.all();
            for (String domain : domains) {
                assertEquals(domain.toLowerCase(), domain,
                    "Domain should be lowercase: " + domain);
            }
        }
    }

    @Nested
    @DisplayName("Performance tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should perform fast lookups")
        void shouldPerformFastLookups() {
            // Warm up
            for (int i = 0; i < 1000; i++) {
                FreeEmailDomains.isFree("gmail.com");
            }

            // Measure performance
            long startTime = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                FreeEmailDomains.isFree("test" + i + "@gmail.com");
            }
            long endTime = System.nanoTime();

            long durationMs = (endTime - startTime) / 1_000_000;
            assertTrue(durationMs < 100, "10,000 lookups should complete in under 100ms, took: " + durationMs + "ms");
        }
    }

    @Nested
    @DisplayName("Thread safety tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should be thread safe")
        void shouldBeThreadSafe() throws InterruptedException {
            int numThreads = 10;
            int numOperationsPerThread = 1000;
            Thread[] threads = new Thread[numThreads];

            // Create threads that perform concurrent operations
            for (int i = 0; i < numThreads; i++) {
                final int threadId = i;
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < numOperationsPerThread; j++) {
                        assertTrue(FreeEmailDomains.isFree("user" + threadId + j + "@gmail.com"));
                        assertFalse(FreeEmailDomains.isFree("user" + threadId + j + "@company" + threadId + ".com"));
                        assertNotNull(FreeEmailDomains.all());
                    }
                });
            }

            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Verify the domains set is still intact
            assertTrue(FreeEmailDomains.isFree("gmail.com"));
            assertFalse(FreeEmailDomains.isFree("example.com"));
        }
    }
}