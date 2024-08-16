package com.centralnicreseller.apiconnector;

import java.util.regex.Pattern;

import com.ibm.icu.text.IDNA;
import com.ibm.icu.text.IDNA.Info;

/**
 * Utility class for processing domain names using the IDNA protocol.
 * IDNAProcessor.java.
 *
 * @author Asif Nawaz
 * @version %I%, %G%
 * @since 5.0
 */
public final class IDNAProcessor {

    // Private constructor to prevent instantiation
    private IDNAProcessor() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * IDNA instance for transitional processing.
     */
    private static final IDNA IDNA_TRANSITIONAL = IDNA.getUTS46Instance(IDNA.DEFAULT);

    /**
     * IDNA instance for non-transitional processing to ASCII.
     */
    private static final IDNA IDNA_NON_TRANSITIONAL_TO_ASCII = IDNA.getUTS46Instance(IDNA.NONTRANSITIONAL_TO_ASCII);

    /**
     * IDNA instance for non-transitional processing to Unicode.
     */
    private static final IDNA IDNA_NON_TRANSITIONAL_TO_UNICODE = IDNA.getUTS46Instance(IDNA.NONTRANSITIONAL_TO_UNICODE);

    /**
     * Pattern matching non-transitional TLDs.
     */
    private static final Pattern NON_TRANSITIONAL_TLDS = Pattern.compile(
        "\\.(?:art|be|ca|de|swiss|fr|pm|re|tf|wf|yt)\\.?$"
        // Note: .com and .net are not supported as non-transitional TLDs.
    );

    /**
     * Determines whether the domain name should be processed using
     * transitional processing based on its TLD.
     *
     * @param domainName The domain name to check.
     * @return {@code true} if the domain name should use transitional processing; {@code false} otherwise.
     */
    public static boolean isTransitionalProcessing(String domainName) {
        return NON_TRANSITIONAL_TLDS.matcher(domainName).find();
    }

    /**
     * Converts a domain name to its ASCII representation using the appropriate
     * IDNA processing method.
     *
     * @param domainName The domain name to convert.
     * @return The ASCII representation of the domain name.
     * @throws IllegalArgumentException if the domain name cannot be converted to ASCII.
     */
    public static String toASCII(String domainName) {
        return toASCII(domainName, isTransitionalProcessing(domainName));
    }

    /**
     * Converts a domain name to its ASCII representation, specifying whether to use
     * transitional processing.
     *
     * @param domainName The domain name to convert.
     * @param useTransitional Whether to use transitional processing.
     * @return The ASCII representation of the domain name.
     * @throws IllegalArgumentException if the domain name cannot be converted to ASCII.
     */
    public static String toASCII(String domainName, boolean useTransitional) {
        Info info = new Info();
        StringBuilder asciiDomain = new StringBuilder();
        IDNA idnaInstance = useTransitional ? IDNA_TRANSITIONAL : IDNA_NON_TRANSITIONAL_TO_ASCII;
        idnaInstance.nameToASCII(domainName, asciiDomain, info);
        if (asciiDomain.length() > 0 && !info.hasErrors()) {
            return asciiDomain.toString();
        }
        throw new IllegalArgumentException("Unable to translate " + domainName + " to ASCII.");
    }

    /**
     * Converts a domain name to its Unicode representation using the appropriate
     * IDNA processing method.
     *
     * @param domainName The domain name to convert.
     * @return The Unicode representation of the domain name.
     * @throws IllegalArgumentException if the domain name cannot be converted to Unicode.
     */
    public static String toUnicode(String domainName) {
        return toUnicode(domainName, isTransitionalProcessing(domainName));
    }

    /**
     * Converts a domain name to its Unicode representation, specifying whether to use
     * transitional processing.
     *
     * @param domainName The domain name to convert.
     * @param useTransitional Whether to use transitional processing.
     * @return The Unicode representation of the domain name.
     * @throws IllegalArgumentException if the domain name cannot be converted to Unicode.
     */
    public static String toUnicode(String domainName, boolean useTransitional) {
        Info info = new Info();
        StringBuilder unicodeDomain = new StringBuilder();
        IDNA idnaInstance = useTransitional ? IDNA_TRANSITIONAL : IDNA_NON_TRANSITIONAL_TO_UNICODE;
        idnaInstance.nameToUnicode(domainName, unicodeDomain, info);
        if (unicodeDomain.length() > 0 && !info.hasErrors()) {
            return unicodeDomain.toString();
        }
        throw new IllegalArgumentException("Unable to translate " + domainName + " to Unicode.");
    }
}
