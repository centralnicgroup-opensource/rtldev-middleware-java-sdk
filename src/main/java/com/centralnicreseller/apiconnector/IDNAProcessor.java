package com.centralnicreseller.apiconnector;

import java.util.regex.Pattern;

import com.ibm.icu.text.IDNA;
import com.ibm.icu.text.IDNA.Info;

class IDNAProcessor {
    private static final IDNA idnaTransitional = IDNA.getUTS46Instance(IDNA.DEFAULT);
    private static final IDNA idnaNonTransitionalToASCII = IDNA.getUTS46Instance(IDNA.NONTRANSITIONAL_TO_ASCII);
    private static final IDNA idnaNonTransitionalToUnicode = IDNA.getUTS46Instance(IDNA.NONTRANSITIONAL_TO_UNICODE);
    private static final Pattern NON_TRANSITIONAL_TLDS = Pattern.compile(
            "\\.(?:art|be|ca|de|swiss|fr|pm|re|tf|wf|yt)\\.?$" // TODO: com and net not supported as well?
    );

    public static boolean isTransitionalProcessing(String domainName) {
        return NON_TRANSITIONAL_TLDS.matcher(domainName).find();
    }

    public static String toASCII(String domainName) {
        return toASCII(domainName, isTransitionalProcessing(domainName));
    }

    public static String toASCII(String domainName, boolean useTransitional) {
        Info info = new Info();
        StringBuilder asciiDomain = new StringBuilder();
        IDNA idnaInstance = useTransitional ? idnaTransitional : idnaNonTransitionalToASCII;
        idnaInstance.nameToASCII(domainName, asciiDomain, info);
        if (asciiDomain.length() > 0 && !info.hasErrors()) {
            return asciiDomain.toString();
        }
        throw new IllegalArgumentException("Unable to translate " + domainName + " to ASCII.");
    }

    public static String toUnicode(String domainName) {
        return toUnicode(domainName, isTransitionalProcessing(domainName));
    }

    public static String toUnicode(String domainName, boolean useTransitional) {
        Info info = new Info();
        StringBuilder unicodeDomain = new StringBuilder();
        IDNA idnaInstance = useTransitional ? idnaTransitional : idnaNonTransitionalToUnicode;
        idnaInstance.nameToUnicode(domainName, unicodeDomain, info);
        if (unicodeDomain.length() > 0 && !info.hasErrors()) {
            return unicodeDomain.toString();
        }
        throw new IllegalArgumentException("Unable to translate " + domainName + " to Unicode.");
    }
}
