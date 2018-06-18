package net.ispapi.apiconnector;

import java.util.HashMap;
import java.util.Map;

/**
 * DefaultResponse class manages default api response templates
 * to be used for different reasons.
 * It also provides functionality to compare a response against a template.
 * 
 * Basically used to provide custom response templates that are
 * used in error cases to have a useful way to responds to the client.
 * 
 * @author      Kai Schwarz
 * @version     %I%, %G%
 * @since 1.0
 */
class DefaultResponse {
    /**
     * Singleton Class constructor as we have just static methods.
     */
    private DefaultResponse() {
    }

    /** represents the template container */
    private static Map<String, String> templates;
    static {
        Map<String, String> mm = new HashMap<String, String>();
        mm.put("empty", "[RESPONSE]\r\ncode=423\r\ndescription=Empty API response\r\nEOF\r\n");
        mm.put("error", "[RESPONSE]\r\ncode=421\r\ndescription=Command failed due to server error. Client should try again\r\nEOF\r\n");
        mm.put("expired", "[RESPONSE]\r\ncode=530\r\ndescription=SESSION NOT FOUND\r\nEOF\r\n");
        mm.put("commonerror", "[RESPONSE]\r\nDESCRIPTION=Command failed;####ERRMSG####;\r\nCODE=500\r\nQUEUETIME=0\r\nRUNTIME=0\r\nEOF");
        templates = mm;
    }

    /** 
     * Method to get all available response templates
     * @return all available response templates
     */
    public static Map<String, String> getAll() {
        return templates;
    }

    /**
     * Method to get a parsed response template by ID
     * @param templateID template ID
     * @return parsed response template
     */
    public static Map<String, Object> getParsed(String templateID) {
        return HashResponse.parse(templates.get(templateID));
    }

    /**
     * Method to get a raw response template by ID
     * @param templateID template ID
     * @return raw response template
     */
    public static String get(String templateID) {
        return templates.get(templateID);
    }

    /**
     * Method to set a response template
     * @param templateID template ID
     * @param templateContent the response template string
     */
    public static void set(String templateID, String templateContent) {
        templates.put(templateID, templateContent);
    }

    /**
     * Method to set a response template
     * @param templateID template ID
     * @param templateContent the response template string
     */
    public static void set(String templateID, Map<String, Object> templateContent) {
        templates.put(templateID, HashResponse.serialize(templateContent));
    }

    /**
     * Method to compare a given raw api response with a response template.
     * It just compare CODE and DESCRIPTION.
     * @param p_r raw api response
     * @param p_templateid template ID
     * @return comparision result
     */
    public static boolean match(String p_r, String p_templateid) {
        Map<String, Object> tpl = getParsed(p_templateid);
        Map<String, Object> r = HashResponse.parse(p_r);
        return (tpl.get("CODE").equals(r.get("CODE")) && tpl.get("DESCRIPTION").equals(r.get("DESCRIPTION")));
    }

    /**
     * Method to compare a given parsed api response with a response template.
     * It just compare CODE and DESCRIPTION.
     * @param p_r raw api response
     * @param p_templateid template ID
     * @return comparision result
     */
    public static boolean match(Map<String, Object> p_r, String p_templateid) {
        Map<String, Object> tpl = getParsed(p_templateid);
        return (tpl.get("CODE").equals(p_r.get("CODE")) && tpl.get("DESCRIPTION").equals(p_r.get("DESCRIPTION")));
    }
}
