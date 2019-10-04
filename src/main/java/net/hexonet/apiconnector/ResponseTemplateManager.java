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
public class ResponseTemplateManager {
    /** backend system entity */
    private Map<String, String> templates;
    /** hidden class var of type of its own class */
    private static ResponseTemplateManager instance;

    /**
     * Class constructor.
     */
    private ResponseTemplateManager() {
        this.templates = new HashMap<String, String>();
        this.templates.put("404", this.generateTemplate("421", "Page not found"));
        this.templates.put("500", this.generateTemplate("500", "Internal server error"));
        this.templates.put("empty", this.generateTemplate("423",
                "Empty API response. Probably unreachable API end point"));
        this.templates.put("error", this.generateTemplate("421",
                "Command failed due to server error. Client should try again"));
        this.templates.put("expired", this.generateTemplate("530", "SESSION NOT FOUND"));
        this.templates.put("httperror",
                this.generateTemplate("421", "Command failed due to HTTP communication error"));
        this.templates.put("unauthorized", this.generateTemplate("500", "Unauthorized"));
    }

    /**
     * Get ResponseTemplateManager Singleton Instance
     *
     * @return ResponseTemplateManager Instance
     */
    public static ResponseTemplateManager getInstance() {
        if (ResponseTemplateManager.instance == null) {
            ResponseTemplateManager.instance = new ResponseTemplateManager();
        }
        return ResponseTemplateManager.instance;
    }

    /**
     * Generate API response template string for given code and description
     * 
     * @param code        API response code
     * @param description API response description
     * @return generated response template string
     */
    public String generateTemplate(String code, String description) {
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
     * @return ResponseTemplateManager instance for method chaining
     */
    public ResponseTemplateManager addTemplate(String id, String plain) {
        this.templates.put(id, plain);
        return this;
    }

    /**
     * Get response template instance from template container
     * 
     * @param id template id
     * @return template instance
     */
    public ResponseTemplate getTemplate(String id) {
        if (this.hasTemplate(id)) {
            return new ResponseTemplate(this.templates.get(id));
        }
        return new ResponseTemplate(this.generateTemplate("500", "Response Template not found"));
    }

    /**
     * Return all available response templates
     * 
     * @return all available response template instances
     */
    public Map<String, ResponseTemplate> getTemplates() {
        Map<String, ResponseTemplate> tpls = new HashMap<String, ResponseTemplate>();
        Iterator<Map.Entry<String, String>> it = this.templates.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            tpls.put(pair.getKey(), new ResponseTemplate(pair.getValue()));
        }
        return tpls;
    }

    /**
     * Check if given template exists in template container
     * 
     * @param id template id
     * @return boolean result
     */
    public boolean hasTemplate(String id) {
        return this.templates.get(id) != null;
    }

    /**
     * Check if given API response hash matches a given template by code and description
     * 
     * @param tpl2 api response hash
     * @param id   template id
     * @return boolean result
     */
    public boolean isTemplateMatchHash(Map<String, Object> tpl2, String id) {
        Map<String, Object> h = this.getTemplate(id).getHash();
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
    public boolean isTemplateMatchPlain(String plain, String id) {
        return this.isTemplateMatchHash(ResponseParser.parse(plain), id);
    }
}
