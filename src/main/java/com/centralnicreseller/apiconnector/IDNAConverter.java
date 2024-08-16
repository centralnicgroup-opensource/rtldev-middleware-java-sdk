package com.centralnicreseller.apiconnector;

import java.util.ArrayList;
import java.util.List;

/**
 *  Utility class for processing domain names using the IDNA protocol.
 * IDNAConverter.java.
 *
 * @author Asif Nawaz
 * @version %I%, %G%
 * @since 5.0
 */
public class IDNAConverter {
    /**
     * The Unicode representation of the domain name.
     */
    private String idn;

    /**
     * The ASCII (Punycode) representation of the domain name.
     */
    private String pc;

    /**
     * A list of Unicode representations for a list of domain names.
     */
    private List<String> idnList;

    /**
     * A list of ASCII (Punycode) representations for a list of domain names.
     */
    private List<String> pcList;

    /**
     * Constructs an IDNAConverter with a single domain name's Unicode and ASCII representations.
     *
     * @param idn The Unicode representation of the domain name.
     * @param pc The ASCII (Punycode) representation of the domain name.
     */
    public IDNAConverter(String idn, String pc) {
        this.idn = idn;
        this.pc = pc;
    }

    /**
     * Constructs an IDNAConverter with lists of domain names' Unicode and ASCII representations.
     *
     * @param idnList The list of Unicode representations of the domain names.
     * @param pcList The list of ASCII (Punycode) representations of the domain names.
     */
    public IDNAConverter(List<String> idnList, List<String> pcList) {
        this.idnList = idnList;
        this.pcList = pcList;
    }

    /**
     * Converts a single domain name to its Unicode and ASCII representations.
     *
     * @param domainName The domain name to convert.
     * @return An IDNAConverter containing both the Unicode and ASCII representations.
     */
    public static IDNAConverter convert(String domainName) {
        try {
            String pc = IDNAProcessor.toASCII(domainName);
            String idn = IDNAProcessor.toUnicode(domainName);
            return new IDNAConverter(idn, pc);
        } catch (Exception e) {
            return new IDNAConverter(domainName, domainName);
        }
    }

    /**
     * Converts a single domain name to its Unicode and ASCII representations,
     * specifying whether to use transitional processing.
     *
     * @param domainName The domain name to convert.
     * @param useTransitional Whether to use transitional processing.
     * @return An IDNAConverter containing both the Unicode and ASCII representations.
     */
    public static IDNAConverter convert(String domainName, boolean useTransitional) {
        try {
            String pc = IDNAProcessor.toASCII(domainName, useTransitional);
            String idn = IDNAProcessor.toUnicode(domainName, useTransitional);
            return new IDNAConverter(idn, pc);
        } catch (Exception e) {
            return new IDNAConverter(domainName, domainName);
        }
    }

    /**
     * Converts a list of domain names to their Unicode and ASCII representations.
     *
     * @param domainNames The list of domain names to convert.
     * @return An IDNAConverter containing lists of both the Unicode and ASCII representations.
     */
    public static IDNAConverter convert(List<String> domainNames) {
        List<String> idnResults = new ArrayList<>();
        List<String> pcResults = new ArrayList<>();

        for (String domainName : domainNames) {
            try {
                pcResults.add(IDNAProcessor.toASCII(domainName));
                idnResults.add(IDNAProcessor.toUnicode(domainName));
            } catch (Exception e) {
                pcResults.add(domainName);
                idnResults.add(domainName);
            }
        }

        return new IDNAConverter(idnResults, pcResults);
    }

    /**
     * Converts a list of domain names to their Unicode and ASCII representations,
     * specifying whether to use transitional processing.
     *
     * @param domainNames The list of domain names to convert.
     * @param useTransitional Whether to use transitional processing.
     * @return An IDNAConverter containing lists of both the Unicode and ASCII representations.
     */
    public static IDNAConverter convert(List<String> domainNames, boolean useTransitional) {
        List<String> idnResults = new ArrayList<>();
        List<String> pcResults = new ArrayList<>();

        for (String domainName : domainNames) {
            try {
                pcResults.add(IDNAProcessor.toASCII(domainName, useTransitional));
                idnResults.add(IDNAProcessor.toUnicode(domainName, useTransitional));
            } catch (Exception e) {
                pcResults.add(domainName);
                idnResults.add(domainName);
            }
        }

        return new IDNAConverter(idnResults, pcResults);
    }

    /**
     * Returns the Unicode representation of the domain name.
     *
     * @return The Unicode representation.
     */
    public String getIdn() {
        return idn;
    }

    /**
     * Returns the ASCII (Punycode) representation of the domain name.
     *
     * @return The ASCII representation.
     */
    public String getPc() {
        return pc;
    }

    /**
     * Returns the list of Unicode representations of the domain names.
     *
     * @return The list of Unicode representations.
     */
    public List<String> getIdnList() {
        return idnList;
    }

    /**
     * Returns the list of ASCII (Punycode) representations of the domain names.
     *
     * @return The list of ASCII representations.
     */
    public List<String> getPcList() {
        return pcList;
    }
}
