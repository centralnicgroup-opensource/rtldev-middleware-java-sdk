package net.hexonet.apiconnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ResponseParser covers all functionality to parse and serialize API response data
 * 
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public class ResponseParser {
    /**
     * Method to stringify a parsed api response
     * 
     * @param p_hash parsed api response
     * @return stringified/raw api response
     */
    public static String serialize(Map<?, ?> p_hash) {
        StringBuilder plain = new StringBuilder("[RESPONSE]");
        for (Map.Entry<?, ?> entry2 : p_hash.entrySet()) {
            String key = (String) entry2.getKey();
            if (key.equals("PROPERTY")) {
                Map<?, ?> properties = (HashMap<?, ?>) p_hash.get(key);
                for (Map.Entry<?, ?> entry : properties.entrySet()) {
                    String subkey = (String) entry.getKey();
                    ArrayList<?> values = (ArrayList<?>) entry.getValue();
                    for (int i = 0; i < values.size(); i++) {
                        plain.append("\r\nPROPERTY[" + subkey + "][" + i + "]=" + values.get(i));
                    }
                }
            } else {
                String tmp = (String) p_hash.get(key);
                if (tmp != null) {
                    plain.append("\r\n" + key + "=" + tmp);
                }
            }
        }
        plain.append("\r\nEOF\r\n");
        return plain.toString();
    };

    /**
     * Method to parse the given raw api response
     * 
     * @param r raw api response
     * @return parsed api response
     */
    public static Map<String, Object> parse(String r) {
        Map<String, Object> hash = new HashMap<String, Object>();
        String[] tmp = r.split("\\R", 0);
        Pattern p1 = Pattern.compile("^([^\\=]*[^\\t\\= ])[\\t ]*=[\\t ]*(.*)$",
                Pattern.CASE_INSENSITIVE);
        Pattern p2 =
                Pattern.compile("^property\\[([^\\]]*)\\]\\[([0-9]+)\\]", Pattern.CASE_INSENSITIVE);
        int idx = -1;
        Map<String, ArrayList<String>> properties = new HashMap<String, ArrayList<String>>();
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
                        list = new ArrayList<String>();
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
