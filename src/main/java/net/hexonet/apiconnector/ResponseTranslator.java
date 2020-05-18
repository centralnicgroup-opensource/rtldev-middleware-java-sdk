package net.hexonet.apiconnector;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public final class ResponseTranslator {
    /** hidden class var of API description regex mappings for translation */
    private static Map<String, String> descriptionRegexMap;
    /** initialization */
    static {
        descriptionRegexMap = new HashMap<>();
        descriptionRegexMap.put("Authorization failed; Operation forbidden by ACL",
                "Authorization failed; Used Command `{COMMAND}` not white-listed by your Access Control List");
    };

    public static String translate(String raw, Map<String, String> cmd) {
        return translate(raw, cmd, Map.ofEntries());
    }

    public static String translate(String raw, Map<String, String> cmd, Map<String, String> ph) {
        String regex;
        Pattern pattern;
        Matcher matcher;
        String newraw = (raw.length() == 0) ? "empty" : raw;
        // Hint: Empty API Response (replace {CONNECTION_URL} later)

        // Explicit call for a static template
        if (ResponseTemplateManager.hasTemplate(newraw)) {
            // don't use getTemplate as it leads to endless loop as of again
            // creating a response instance
            newraw = ResponseTemplateManager.templates.get(newraw);
        }

        // Missing CODE or DESCRIPTION in API Response
        regex = newraw.toLowerCase();
        if (!regex.contains("description=")
                || !regex.contains("code=") && ResponseTemplateManager.hasTemplate("invalid")) {
            newraw = ResponseTemplateManager.templates.get("invalid");
        }

        // Explicit call for a static template
        if (ResponseTemplateManager.hasTemplate(newraw)) {
            // don't use getTemplate as it leads to endless loop as of again
            // creating a response instance
            newraw = ResponseTemplateManager.templates.get(newraw);
        }

        // generic API response description rewrite
        for (Map.Entry<String, String> me : descriptionRegexMap.entrySet()) {
            // replace command place holder with API command name used
            String val = me.getValue();
            if (cmd.containsKey("COMMAND")) {
                val = val.replace("{COMMAND}", cmd.get("COMMAND"));
            }
            // switch to better readable response if matching
            regex = "(?i)description=" + me.getKey();
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(newraw);
            if (matcher.find()) {
                // stop on first match
                newraw = newraw.replaceAll(regex, val);
                break;
            }
        }

        // generic replacing of place holder vars
        regex = "\\{[^}]+\\}";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(newraw);
        if (matcher.find()) {
            for (Map.Entry<String, String> me : ph.entrySet()) {
                newraw = newraw.replaceAll("\\{" + me.getKey() + "\\}", me.getValue());
            }
            newraw = newraw.replaceAll(regex, "");
        }
        return newraw;
    }
}
