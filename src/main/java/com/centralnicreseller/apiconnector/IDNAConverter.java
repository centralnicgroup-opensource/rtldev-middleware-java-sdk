package com.centralnicreseller.apiconnector;
import java.util.ArrayList;
import java.util.List;

public class IDNAConverter {
    private String IDN;
    private String PC;
    private List<String> IDNList;
    private List<String> PCList;

    public IDNAConverter(String IDN, String PC) {
        this.IDN = IDN;
        this.PC = PC;
    }

    public IDNAConverter(List<String> IDNList, List<String> PCList) {
        this.IDNList = IDNList;
        this.PCList = PCList;
    }

    public static IDNAConverter convert(String domainName) {
        try {
            String pc = IDNAProcessor.toASCII(domainName);
            String idn = IDNAProcessor.toUnicode(domainName);
            return new IDNAConverter(idn, pc);
        } catch (Exception e) {
            return new IDNAConverter(domainName, domainName);
        }
    }

    public static IDNAConverter convert(String domainName, boolean useTransitional) {
        try {
            String pc = IDNAProcessor.toASCII(domainName, useTransitional);
            String idn = IDNAProcessor.toUnicode(domainName, useTransitional);
            return new IDNAConverter(idn, pc);
        } catch (Exception e) {
            return new IDNAConverter(domainName, domainName);
        }
    }

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

    public String getIDN() {
        return IDN;
    }

    public String getPC() {
        return PC;
    }

    public List<String> getIDNList() {
        return IDNList;
    }

    public List<String> getPCList() {
        return PCList;
    }
}
