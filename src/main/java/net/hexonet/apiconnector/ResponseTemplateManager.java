package net.hexonet.apiconnector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ResponseTemplateManager covers functionality to manage response tempaltes
 * 
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public final class ResponseTemplateManager {
    /** backend system entity */
    public static Map<String, String> templates;

    /** initializations */
    static {
        templates = new HashMap<String, String>();
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

    /**
     * Generate API response template string for given code and description
     * 
     * @param code        API response code
     * @param description API response description
     * @return generated response template string
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
     * Add response template to template container
     * 
     * @param id    template id
     * @param plain API plain response
     * @return ResponseTemplateManager class for method chaining
     */
    public static Class<ResponseTemplateManager> addTemplate(String id, String plain) {
        templates.put(id, plain);
        return ResponseTemplateManager.class;
    }

    /**
     * Add response template to template container
     * 
     * @param id    template id
     * @param code  data provided for generating a new template to use
     * @param descr data provided for generating a new template to use
     * @return Response Instance
     */
    public static Class<ResponseTemplateManager> addTemplate(String id, String code, String descr) {
        return addTemplate(id, generateTemplate(code, descr));
    }

    /**
     * Get response template instance from template container
     * 
     * @param id template id
     * @return template instance
     */
    public static Response getTemplate(String id) {
        if (hasTemplate(id)) {
            return new Response(id);
        }
        return new Response("notfound");
    }

    /**
     * Return all available response templates
     * 
     * @return all available response template instances
     */
    public static Map<String, Response> getTemplates() {
        Map<String, Response> tpls = new HashMap<String, Response>();
        Iterator<Map.Entry<String, String>> it = templates.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            tpls.put(pair.getKey(), new Response(pair.getValue()));
        }
        return tpls;
    }

    /**
     * Check if given template exists in template container
     * 
     * @param id template id
     * @return boolean result
     */
    public static boolean hasTemplate(String id) {
        return templates.get(id) != null;
    }

    /**
     * Check if given API response hash matches a given template by code and description
     * 
     * @param tpl2 api response hash
     * @param id   template id
     * @return boolean result
     */
    public static boolean isTemplateMatchHash(Map<String, Object> tpl2, String id) {
        Map<String, Object> h = getTemplate(id).getHash();
        return (((String) h.get("CODE")).equals((String) tpl2.get("CODE"))
                && ((String) h.get("DESCRIPTION")).equals((String) tpl2.get("DESCRIPTION")));
    }

    /**
     * Check if given API plain response matches a given template by code and description
     * 
     * @param plain API plain response
     * @param id    template id
     * @return boolean result
     */
    public static boolean isTemplateMatchPlain(String plain, String id) {
        return isTemplateMatchHash(ResponseParser.parse(plain), id);
    }
}
