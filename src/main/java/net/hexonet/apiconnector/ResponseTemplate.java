package net.hexonet.apiconnector;

import java.util.Map;

/**
 * ResponseTemplate is the base class to work with parsed API response data or hardcoded response
 * template data
 * 
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public class ResponseTemplate {
    /** backend system plain response data */
    protected String raw;
    /** backend system parsed response data (hash format) */
    protected Map<String, Object> hash;

    /**
     * Class constructor.
     */
    public ResponseTemplate(String raw) {
        if (raw.length() == 0) {
            raw = ResponseTemplateManager.getInstance().getTemplate("empty").getPlain();
        }
        this.raw = raw;
        this.hash = ResponseParser.parse(raw);
    }

    /**
     * Get API response code
     * 
     * @return API response code
     */
    public int getCode() {
        return Integer.parseInt((String) this.hash.get("CODE"));
    }

    /**
     * Get API response description
     * 
     * @return API response description
     */
    public String getDescription() {
        return (String) this.hash.get("DESCRIPTION");
    }

    /**
     * Get Plain API response
     * 
     * @return Plain API response
     */
    public String getPlain() {
        return this.raw;
    }

    /**
     * Get Queuetime of API response
     * 
     * @return Queuetime of API response
     */
    public double getQueuetime() {
        String rt = (String) this.hash.get("QUEUETIME");
        if (rt != null) {
            return Double.parseDouble((String) this.hash.get("QUEUETIME"));
        }
        return 0.00;
    }

    /**
     * Get API response as Hash
     * 
     * @return API response hash
     */
    public Map<String, Object> getHash() {
        return this.hash;
    }

    /**
     * Get Runtime of API response
     * 
     * @return Runtime of API response
     */
    public double getRuntime() {
        String rt = (String) this.hash.get("RUNTIME");
        if (rt != null) {
            return Double.parseDouble((String) this.hash.get("RUNTIME"));
        }
        return 0.00;
    }

    /**
     * Check if current API response represents an error case API response code is an 5xx code
     * 
     * @return boolean result
     */
    public boolean isError() {
        String code = (String) this.hash.get("CODE");
        return code.charAt(0) == '5';
    }

    /**
     * Check if current API response represents a success case API response code is an 2xx code
     * 
     * @return boolean result
     */
    public boolean isSuccess() {
        String code = (String) this.hash.get("CODE");
        return code.charAt(0) == '2';
    }

    /**
     * Check if current API response represents a temporary error case API response code is an 4xx
     * code
     * 
     * @return boolean result
     */
    public boolean isTmpError() {
        String code = (String) this.hash.get("CODE");
        return code.charAt(0) == '4';
    }
}
