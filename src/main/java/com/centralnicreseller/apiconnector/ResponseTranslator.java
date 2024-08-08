package com.centralnicreseller.apiconnector;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ResponseTranslator is used to translate HEXONET API responses.
 *
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 4.0
 */
public final class ResponseTranslator {
    /** Hidden class variable for API description regex mappings for translation */
    private static final Map<String, String> descriptionRegexMap;

    // Initialization block
    static {
        descriptionRegexMap = new HashMap<>();
        descriptionRegexMap.put("Authorization failed; Operation forbidden by ACL",
                "Authorization failed; Used Command `{COMMAND}` not white-listed by your Access Control List");
    }

    // Private constructor to prevent instantiation
    private ResponseTranslator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Translate the given API response, no placeholder vars given.
     *
     * @param raw The API response in plain text.
     * @param cmd The requested API command.
     * @return the translated API response.
     */
    public static String translate(String raw, Map<String, String> cmd) {
        return translate(raw, cmd, Map.ofEntries());
    }

    /**
     * Translate the given API response with placeholder variables.
     *
     * @param raw The API response in plain text.
     * @param cmd The requested API command.
     * @param ph  The placeholder vars container.
     * @return the translated API response.
     */
    public static String translate(String raw, Map<String, String> cmd, Map<String, String> ph) {
        String regex;
        Pattern pattern;
        Matcher matcher;
        String newRaw = (raw.length() == 0) ? "empty" : raw;

        // Hint: Empty API Response (replace {CONNECTION_URL} later)
        // Explicit call for a static template
        if (ResponseTemplateManager.hasTemplate(newRaw)) {
            // don't use getTemplate as it leads to endless loop as of again
            // creating a response instance
            newRaw = ResponseTemplateManager.templates.get(newRaw);
        }

        // Missing CODE or DESCRIPTION in API Response
        regex = newRaw.toLowerCase();
        if (!regex.contains("description\s=")
                || (!regex.contains("code\s=") && ResponseTemplateManager.hasTemplate("invalid"))) {
            newRaw = ResponseTemplateManager.templates.get("invalid");
        }

        // Explicit call for a static template
        if (ResponseTemplateManager.hasTemplate(newRaw)) {
            // don't use getTemplate as it leads to endless loop as of again
            // creating a response instance
            newRaw = ResponseTemplateManager.templates.get(newRaw);
        }

        // Generic API response description rewrite
        for (Map.Entry<String, String> me : descriptionRegexMap.entrySet()) {
            // Replace command placeholder with API command name used
            String val = me.getValue();
            if (cmd.containsKey("COMMAND")) {
                val = val.replace("{COMMAND}", cmd.get("COMMAND"));
            }
            // Switch to better readable response if matching
            regex = "(?i)description\s=" + me.getKey();
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(newRaw);
            if (matcher.find()) {
                // Stop on first match
                newRaw = newRaw.replaceAll(regex, val);
                break;
            }
        }

        // Generic replacing of placeholder vars
        regex = "\\{[^}]+\\}";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(newRaw);
        if (matcher.find()) {
            for (Map.Entry<String, String> me : ph.entrySet()) {
                newRaw = newRaw.replaceAll("\\{" + me.getKey() + "\\}", me.getValue());
            }
            newRaw = newRaw.replaceAll(regex, "");
        }
        return newRaw;
    }
}
