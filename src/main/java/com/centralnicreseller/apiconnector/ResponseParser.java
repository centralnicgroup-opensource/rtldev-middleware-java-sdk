package com.centralnicreseller.apiconnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ResponseParser covers all functionality to parse and serialize API response
 * data
 *
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public final class ResponseParser {
    // Private constructor to prevent instantiation
    private ResponseParser() {
            throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Method to parse the given raw api response
     *
     * @param r raw api response
     * @return parsed api response
     */
    public static Map<String, Object> parse(String r) {
        Map<String, Object> hash = new HashMap<>();
        String[] tmp = r.split("\\R", 0);
        Pattern p1 = Pattern.compile("^([^\\=]*[^\\t\\= ])[\\t ]*=[\\t ]*(.*)$",
                Pattern.CASE_INSENSITIVE);
        Pattern p2 = Pattern.compile("^property\\[([^\\]]*)\\]\\[([0-9]+)\\]", Pattern.CASE_INSENSITIVE);
        int idx = -1;
        Map<String, ArrayList<String>> properties = new HashMap<>();
        while (++idx < tmp.length) {
            String row = tmp[idx];
            Matcher m = p1.matcher(row);
            if (m.find()) {
                String property = m.group(1).toUpperCase();
                Matcher mm = p2.matcher(property);
                if (mm.find()) {
                    String key = mm.group(1).toUpperCase().replaceAll("\\s", "");
                    int idx2 = Integer.parseInt(mm.group(2));
                    ArrayList<String> list = properties.get(key);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(idx2, m.group(2).replaceFirst("[\\t ]*$", ""));
                    properties.put(key, list);
                } else {
                    String val = m.group(2);
                    if (val != null) {
                        hash.put(property, val.replaceAll("[\\t ]*$", ""));
                    }
                }
            }
        }
        if (!properties.isEmpty()) {
            hash.put("PROPERTY", properties);
        }
        return hash;
    };
}
