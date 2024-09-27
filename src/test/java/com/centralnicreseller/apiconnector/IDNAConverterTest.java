package com.centralnicreseller.apiconnector;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
}
