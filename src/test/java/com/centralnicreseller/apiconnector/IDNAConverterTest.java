package com.centralnicreseller.apiconnector;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for class IDNAConverter
 */
public class IDNAConverterTest {

    /**
     * Test constructor with single domain name's Unicode and ASCII representations.
     */
    @Test
    public void testConstructorSingleDomain() {
        String unicodeDomain = "example.com";
        String asciiDomain = "xn--example-5xa.com";

        IDNAConverter converter = new IDNAConverter(unicodeDomain, asciiDomain);

        assertEquals(unicodeDomain, converter.getIdn());
        assertEquals(asciiDomain, converter.getPc());
    }

    /**
     * Test constructor with lists of domain names' Unicode and ASCII representations.
     */
    @Test
    public void testConstructorDomainLists() {
        List<String> unicodeDomains = Arrays.asList("example.com", "exämple.com");
        List<String> asciiDomains = Arrays.asList("xn--example-5xa.com", "xn--exmple-cua.com");

        IDNAConverter converter = new IDNAConverter(unicodeDomains, asciiDomains);

        assertEquals(unicodeDomains, converter.getIdnList());
        assertEquals(asciiDomains, converter.getPcList());
    }

    /**
     * Test static convert method with a single domain name.
     */
    @Test
    public void testConvertSingleDomain() {
        String domainName = "exämple.com";
        IDNAConverter converter = IDNAConverter.convert(domainName);

        assertNotNull(converter);
        assertEquals("xn--exmple-cua.com", converter.getPc());
        assertEquals("exämple.com", converter.getIdn());
    }

    /**
     * Test static convert method with a single domain name and transitional processing.
     */
    @Test
    public void testConvertSingleDomainWithTransitional() {
        String domainName = "exämple.com";
        boolean useTransitional = true;
        IDNAConverter converter = IDNAConverter.convert(domainName, useTransitional);

        assertNotNull(converter);
        assertEquals("xn--exmple-cua.com", converter.getPc());
        assertEquals("exämple.com", converter.getIdn());
    }

    /**
     * Test static convert method with a list of domain names.
     */
    @Test
    public void testConvertDomainList() {
        List<String> domainNames = Arrays.asList("example.com", "exämple.com");
        IDNAConverter converter = IDNAConverter.convert(domainNames);

        assertNotNull(converter);
        assertEquals(Arrays.asList("example.com", "xn--exmple-cua.com"), converter.getPcList());
        assertEquals(Arrays.asList("example.com", "exämple.com"), converter.getIdnList());
    }

    /**
     * Test static convert method with a list of domain names and transitional processing.
     */
    @Test
    public void testConvertDomainListWithTransitional() {
        List<String> domainNames = Arrays.asList("example.com", "exämple.com");
        boolean useTransitional = true;
        IDNAConverter converter = IDNAConverter.convert(domainNames, useTransitional);

        assertNotNull(converter);
        assertEquals(Arrays.asList("example.com", "xn--exmple-cua.com"), converter.getPcList());
        assertEquals(Arrays.asList("example.com", "exämple.com"), converter.getIdnList());
    }

    /**
     * TLDs that default to non-transitional processing.
     */
    private static final List<String> NON_TRANSITIONAL_TLDS = Arrays.asList(
        "art", "be", "ca", "de", "swiss", "fr", "pm", "re", "tf", "wf", "yt");

    /**
     * Default processing keeps ß for .de.
     */
    @Test
    public void testDefaultKeepsSharpSForDe() {
        assertEquals("xn--fa-hia.de", IDNAConverter.convert("faß.de").getPc());
        assertEquals("xn--fa-hia.de", IDNAProcessor.toASCII("faß.de"));
    }

    /**
     * Default processing maps ß to ss for .com.
     */
    @Test
    public void testDefaultMapsSharpSForCom() {
        assertEquals("fass.com", IDNAConverter.convert("faß.com").getPc());
        assertEquals("fass.com", IDNAProcessor.toASCII("faß.com"));
    }

    /**
     * Every listed TLD keeps ß by default.
     */
    @Test
    public void testDefaultKeepsSharpSForAllNonTransitionalTlds() {
        for (String tld : NON_TRANSITIONAL_TLDS) {
            assertEquals("faß." + tld, "xn--fa-hia." + tld, IDNAConverter.convert("faß." + tld).getPc());
            assertFalse(tld, IDNAProcessor.isTransitionalProcessing("faß." + tld));
        }
    }

    /**
     * The TLD is matched case-insensitively.
     */
    @Test
    public void testTldMatchIsCaseInsensitive() {
        String pc = IDNAConverter.convert("faß.DE").getPc();
        assertEquals("xn--fa-hia", pc.split("\\.")[0]);
        assertFalse(IDNAProcessor.isTransitionalProcessing("faß.DE"));
    }

    /**
     * The TLD is matched only at the end of the domain name.
     */
    @Test
    public void testTldMatchedOnlyAtEnd() {
        assertEquals("fass.de.com", IDNAConverter.convert("faß.de.com").getPc());
        assertEquals("fass.dealer", IDNAConverter.convert("faß.dealer").getPc());
        assertTrue(IDNAProcessor.isTransitionalProcessing("faß.de.com"));
        assertTrue(IDNAProcessor.isTransitionalProcessing("faß.dealer"));
    }

    /**
     * An explicit useTransitional argument overrides the TLD default.
     */
    @Test
    public void testExplicitOverrideWinsBothWays() {
        assertEquals("fass.de", IDNAConverter.convert("faß.de", true).getPc());
        assertEquals("xn--fa-hia.de", IDNAConverter.convert("faß.de", false).getPc());
        assertEquals("fass.com", IDNAConverter.convert("faß.com", true).getPc());
        assertEquals("xn--fa-hia.com", IDNAConverter.convert("faß.com", false).getPc());
    }

    /**
     * Default toUnicode keeps ß for .de.
     */
    @Test
    public void testDefaultToUnicodeKeepsSharpSForDe() {
        assertEquals("faß.de", IDNAProcessor.toUnicode("xn--fa-hia.de"));
        assertEquals("faß.de", IDNAConverter.convert("xn--fa-hia.de").getIdn());
    }

    /**
     * Transitional processing maps final sigma to sigma; non-transitional keeps it.
     */
    @Test
    public void testFinalSigmaFollowsProcessingMode() {
        String transitional = IDNAConverter.convert("ας.com", true).getPc();
        String nonTransitional = IDNAConverter.convert("ας.com", false).getPc();
        assertEquals(IDNAConverter.convert("ασ.com", false).getPc(), transitional);
        assertNotEquals(transitional, nonTransitional);
    }
}
