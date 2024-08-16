package com.centralnicreseller.apiconnector;

import java.util.HashMap;
import java.util.Map;

/**
 * ResponseTemplateManager covers functionality to manage response
 * TEMPLATES_DATA.
 *
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public final class ResponseTemplateManager {
    /** Backend system entity */
    private static final Map<String, String> TEMPLATES_DATA = new HashMap<>();

    // Static initialization block
    static {
        TEMPLATES_DATA.put("404", generateTemplate("421", "Page not found"));
        TEMPLATES_DATA.put("500", generateTemplate("500", "Internal server error"));
        TEMPLATES_DATA.put("empty", generateTemplate("423",
                "Empty API response. Probably unreachable API end point {CONNECTION_URL}"));
        TEMPLATES_DATA.put("error", generateTemplate("421",
                "Command failed due to server error. Client should try again"));
        TEMPLATES_DATA.put("expired", generateTemplate("530", "SESSION NOT FOUND"));
        TEMPLATES_DATA.put("httperror",
                generateTemplate("421", "Command failed due to HTTP communication error"));
        TEMPLATES_DATA.put("invalid", generateTemplate("423", "Invalid API response. Contact Support"));
        TEMPLATES_DATA.put("unauthorized", generateTemplate("500", "Unauthorized"));
        TEMPLATES_DATA.put("notfound", generateTemplate("500", "Response Template not found"));
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
        TEMPLATES_DATA.put(id, plain);
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
     * Return all available response TEMPLATES_DATA.
     *
     * @return All available response template instances.
     */
    public static Map<String, Response> getTemplates() {
        Map<String, Response> tpls = new HashMap<>();
        for (Map.Entry<String, String> pair : TEMPLATES_DATA.entrySet()) {
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
        return TEMPLATES_DATA.containsKey(id);
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

    /**
     * Retrieves a copy of the current API response templates map.
     * <p>
     * This method returns a new {@link HashMap} containing the entries from the
     * {@code TEMPLATES_DATA} map. The returned map is a shallow copy, meaning
     * modifications to the returned map will not affect the original
     * {@code TEMPLATES_DATA}
     * map.
     * </p>
     *
     * @return A new {@link Map} containing the API response templates. The map's
     *         keys are template identifiers, and the values are the corresponding
     *         response template strings.
     */
    public static Map<String, String> getTemplatesMap() {
        return new HashMap<>(TEMPLATES_DATA);
    }

}
