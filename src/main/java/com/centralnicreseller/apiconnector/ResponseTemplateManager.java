package com.centralnicreseller.apiconnector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ResponseTemplateManager covers functionality to manage response templates.
 *
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public final class ResponseTemplateManager {
    /** Backend system entity */
    private static final Map<String, String> templates;

    // Static initialization block
    static {
        templates = new HashMap<>();
        templates.put("404", generateTemplate("421", "Page not found"));
        templates.put("500", generateTemplate("500", "Internal server error"));
        templates.put("empty", generateTemplate("423",
                "Empty API response. Probably unreachable API end point {CONNECTION_URL}"));
        templates.put("error", generateTemplate("421",
                "Command failed due to server error. Client should try again"));
        templates.put("expired", generateTemplate("530", "SESSION NOT FOUND"));
        templates.put("httperror",
                generateTemplate("421", "Command failed due to HTTP communication error"));
        templates.put("invalid", generateTemplate("423", "Invalid API response. Contact Support"));
        templates.put("unauthorized", generateTemplate("500", "Unauthorized"));
        templates.put("notfound", generateTemplate("500", "Response Template not found"));
    }

    // Private constructor to prevent instantiation
    private ResponseTemplateManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Generate API response template string for a given code and description.
     *
     * @param code        API response code.
     * @param description API response description.
     * @return Generated response template string.
     */
    public static String generateTemplate(String code, String description) {
        StringBuilder plain = new StringBuilder("[RESPONSE]\r\nCODE=");
        plain.append(code);
        plain.append("\r\nDESCRIPTION=");
        plain.append(description);
        plain.append("\r\nEOF\r\n");
        return plain.toString();
    }

    /**
     * Add a response template to the template container.
     *
     * @param id    Template ID.
     * @param plain API plain response.
     * @return ResponseTemplateManager class for method chaining.
     */
    public static Class<ResponseTemplateManager> addTemplate(String id, String plain) {
        templates.put(id, plain);
        return ResponseTemplateManager.class;
    }

    /**
     * Add a response template to the template container.
     *
     * @param id    Template ID.
     * @param code  Data provided for generating a new template to use.
     * @param descr Data provided for generating a new template to use.
     * @return ResponseTemplateManager class for method chaining.
     */
    public static Class<ResponseTemplateManager> addTemplate(String id, String code, String descr) {
        return addTemplate(id, generateTemplate(code, descr));
    }

    /**
     * Get a response template instance from the template container.
     *
     * @param id Template ID.
     * @return Template instance.
     */
    public static Response getTemplate(String id) {
        if (hasTemplate(id)) {
            return new Response(id);
        }
        return new Response("notfound");
    }

    /**
     * Return all available response templates.
     *
     * @return All available response template instances.
     */
    public static Map<String, Response> getTemplates() {
        Map<String, Response> tpls = new HashMap<>();
        Iterator<Map.Entry<String, String>> it = templates.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            tpls.put(pair.getKey(), new Response(pair.getValue()));
        }
        return tpls;
    }

    /**
     * Check if a given template exists in the template container.
     *
     * @param id Template ID.
     * @return Boolean result.
     */
    public static boolean hasTemplate(String id) {
        return templates.get(id) != null;
    }

    /**
     * Check if a given API response hash matches a given template by code and
     * description.
     *
     * @param tpl2 API response hash.
     * @param id   Template ID.
     * @return Boolean result.
     */
    public static boolean isTemplateMatchHash(Map<String, Object> tpl2, String id) {
        Map<String, Object> h = getTemplate(id).getHash();
        return (((String) h.get("CODE")).equals((String) tpl2.get("CODE"))
                && ((String) h.get("DESCRIPTION")).equals((String) tpl2.get("DESCRIPTION")));
    }

    /**
     * Check if a given API plain response matches a given template by code and
     * description.
     *
     * @param plain API plain response.
     * @param id    Template ID.
     * @return Boolean result.
     */
    public static boolean isTemplateMatchPlain(String plain, String id) {
        return isTemplateMatchHash(ResponseParser.parse(plain), id);
    }

    // Getter for the templates map (if needed)
    public static Map<String, String> getTemplatesMap() {
        return new HashMap<>(templates);
    }
}
