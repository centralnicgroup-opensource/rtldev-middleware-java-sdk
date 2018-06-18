package net.ispapi.apiconnector;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.lang.Math;

/**
 * HashResponse class provides basic functionality to work with API responses.
 *  
 * @author      Kai Schwarz
 * @version     %I%, %G%
 * @since 1.0
 */
public class HashResponse {

    /** represents the parsed API response data */
    protected Map<String, Object> hash;
    /** represents the raw API response data */
    protected String raw;
    /** represents the pattern to match columns used for pagination */
    protected Pattern pagerRegexp = Pattern.compile("^(TOTAL|FIRST|LAST|LIMIT|COUNT)$");
    /** represents an flag to turn column filter on/off */
    protected boolean useColRegexp = false;
    /** represents the column filter pattern */
    protected String columnRegexp;

    /**
     * Class constructor.
     * @param p_r raw api response
     */
    public HashResponse(String p_r) {
        raw = new String(p_r.isEmpty() ? DefaultResponse.get("empty") : p_r);
        hash = parse(raw);
    }

    /**
     * method to return the api raw (but filtered - in case of useColRegexp) response data
     * @return raw api response
     */
    public String raw() {
        return raw(false);
    }

    /**
     * method to return the api raw response data
     * @param noColumnFilter to explicitely suppress filtering in case a column filter is set
     * @return raw api response
     */
    public String raw(boolean noColumnFilter) {
        if (noColumnFilter || !useColRegexp) {
            return raw;
        }
        return serialize(hash());
    }

    /**
     * Method to return the parsed api response
     * @return parsed api response
     */
    public Map<String, Object> hash() {
        if (useColRegexp) {
            Map<String, Object> h = new HashMap<String, Object>(hash);
            Map<?, ?> properties = (HashMap<?, ?>) h.get("PROPERTY");
            if (properties != null){
                Map<String, Object> d = new HashMap<String, Object>();
                for (Map.Entry<?, ?> entry : properties.entrySet()) {
                    String key = (String) entry.getKey();
                    if (key.matches(columnRegexp)) {
                        d.put(key, entry.getValue());
                    }
                }
                h.put("PROPERTY", d);
            }
            return h;
        }
        return hash;
    }

    /**
     * method to turn of column filter
     */
    public void useColumnsReset() {
        useColRegexp = false;
        columnRegexp = null;
    }

    /**
     * method to set a column filter
     * @param pattern column filter pattern (show only the columns provided)
     */
    public void useColumns(String pattern) {
        useColRegexp = true;
        columnRegexp = pattern;
    }

    /**
     * method to access the api response code
     * @return api response code
     */
    public int code() {
        return Integer.parseInt((String) hash.get("CODE"));
    }

    /**
     * method to access the api response description
     * @return api response description
     */
    public String description() {
        return (String) hash.get("DESCRIPTION");
    }

    /**
     * method to access the api response runtime
     * @return api response runtime
     */
    public float runtime() {
        return Float.parseFloat((String) hash.get("RUNTIME"));
    }

    /**
     * method to access the api response queuetime
     * @return api response queuetime
     */
    public float queuetime() {
        return Float.parseFloat((String) hash.get("QUEUETIME"));
    }

    /**
     * method to access the pagination data "first"
     * represents the row index of 1st row of the current response of the whole result set
     * @return api response pagination data "first"
     */
    public int first() {
        String val = getColumnIndex("FIRST", 0);
        return val == null ? 0 : Integer.parseInt(val);
    }

    /**
     * method to access the pagination data "count"
     * represents the count of rows returned in the current response
     * @return api response pagination data "count"
     */
    public int count() {
        String val = getColumnIndex("COUNT", 0);
        if (val != null) {
            return Integer.parseInt(val);
        }
        int c = 0;
        int max = 0;
        ArrayList<?> cols = getColumnKeys();
        ArrayList<?> col;
        for (int i = 0; i < cols.size(); i++) {
            col = getColumn((String) cols.get(i));
            c = col.size();
            if (c > max) {
                max = c;
            }
        }
        return c;
    }

    /**
     * method to access the pagination data "last"
     * represents the row index of last row of the current response of the whole result set
     * @return api response pagination data "last"
     */
    public int last() {
        String val = getColumnIndex("LAST", 0);
        return val == null ? count() - 1 : Integer.parseInt(val);
    }

    /**
     * method to access the pagination data "limit"
     * represents the limited amount of rows requested to be returned
     * @return api response pagination data "limit"
     */
    public int limit() {
        String val = getColumnIndex("LIMIT", 0);
        return val == null ? count() : Integer.parseInt(val);
    }

    /**
     * method to access the pagination data "total"
     * represents the total amount of rows available in the whole result set
     * @return api response pagination data "total"
     */
    public int total() {
        String val = getColumnIndex("TOTAL", 0);
        return val == null ? count() : Integer.parseInt(val);
    }

    /**
     * method to return the amount of pages of the current result set
     * @return amount of pages of the current result set
     */
    public int pages() {
        int t = total();
        if (t > 0) {
            int limit = limit();
            return (int) Math.ceil((double) t / limit);
        }
        return 1;
    }

    /**
     * method to return the number of the current page
     * @return number of the current page
     */
    public int page() {
        if (this.count() > 0) {
            // limit cannot be 0 as this.count() will cover this, no worries
            return (int) Math.floor((double) first() / limit()) + 1;
        }
        return 1;
    }

    /**
     * method to get the previous page number
     * @return number of the previous page
     */
    public int prevpage() {
        int p = page() - 1;
        return (p > 0) ? p : 1;
    }

    /**
     * method to get the next page number
     * @return number of the next page
     */
    public int nextpage() {
        int p = page() + 1;
        int pages = pages();
        return (p <= pages ? p : pages);
    }

    /**
     * method to return all pagination data at once
     * @return full pagination data
     */
    public Map<String, Integer> getPagination() {
        Map<String, Integer> pagination = new HashMap<String, Integer>();
        pagination.put("FIRST", first());
        pagination.put("LAST", last());
        pagination.put("COUNT", count());
        pagination.put("TOTAL", total());
        pagination.put("LIMIT", limit());
        pagination.put("PAGES", pages());
        pagination.put("PAGE", page());
        pagination.put("PAGENEXT", nextpage());
        pagination.put("PAGEPREV", prevpage());
        return pagination;
    };

    /**
     * method to check if the api response represents a success case
     * @return check result
     */
    public boolean isSuccess() {
        int code = code();
        return (code >= 200 && code < 300);
    }

    /**
     * method to check if the api response represents a temporary error case
     * @return check result
     */
    public boolean isTmpError() {
        int code = code();
        return (code >= 400 && code < 500);
    }

    /**
     * method to check if the api response represents an error case
     * @return check result
     */
    public boolean isError() {
        int code = code();
        return (code >= 500 && code <= 600);
    }

    /**
     * method to get a full list available columns in api response
     * @return full list of api response columns
     */
    public ArrayList<?> getColumnKeys() {
        ArrayList<String> columns = new ArrayList<String>();
        Map<?, ?> property = (HashMap<?, ?>) hash.get("PROPERTY");
        if (hash == null) {
            return columns;
        }
        if (property != null) {
            for (Map.Entry<?, ?> entry : property.entrySet()) {
                String key = (String) entry.getKey();
                Matcher m = pagerRegexp.matcher(key);
                if (!m.find()) {
                    columns.add((String) entry.getKey());
                }
            }
        }
        return columns;
    }

    /**
     * method to get the full column data for the given column id
     * @param p_columnid column id
     * @return column data
     */
    public ArrayList<?> getColumn(String p_columnid) {
        Map<?, ?> property = (HashMap<?, ?>) hash.get("PROPERTY");
        if (hash == null) {
            return null;
        }
        ArrayList<?> column = (ArrayList<?>) property.get(p_columnid);
        return column;
    }

    /**
     * method to get a response data field by column id and index
     * @param p_columnid column id
     * @param p_index data index
     * @return response data field
     */
    public String getColumnIndex(String p_columnid, int p_index) {
        Map<?, ?> property = (HashMap<?, ?>) hash.get("PROPERTY");
        if (property == null) {
            return null;
        }
        ArrayList<?> column = (ArrayList<?>) property.get(p_columnid);
        if (column == null) {
            return null;
        }
        if (column.size()<=p_index){
            return null;
        }
        return (String) column.get(p_index);
    }

    /**
     * Method to stringify a parsed api response
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
     * method to parse the given raw api response
     * @param r raw api response
     * @return parsed api response
     */
    public static Map<String, Object> parse(String r) {
        Map<String, Object> hash = new HashMap<String, Object>();
        String[] tmp = r.split("\\R", 0);
        Pattern p1 = Pattern.compile("^([^\\=]*[^\\t\\= ])[\\t ]*=[\\t ]*(.*)$", Pattern.CASE_INSENSITIVE);
        Pattern p2 = Pattern.compile("^property\\[([^\\]]*)\\]\\[([0-9]+)\\]", Pattern.CASE_INSENSITIVE);
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
