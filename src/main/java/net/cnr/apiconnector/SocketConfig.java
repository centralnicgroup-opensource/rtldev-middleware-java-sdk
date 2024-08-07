package net.cnr.apiconnector;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * SocketConfig is the base class to configure the socket for API communication
 * 
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public class SocketConfig {
    /** user account */
    private String login = "";
    /** user password */
    private String pw = "";
    /** subuser account */
    private String opmode = "";

    /**
     * Class constructor.
     */
    public SocketConfig() {}

    /**
     * Method to use to encode data to be ready for POST request
     * 
     * @return the ready to use, encoded POST request payload
     */
    public String getPOSTData() {
        try {
            StringBuilder data = new StringBuilder("");
            if (this.opmode.length() > 0) {
                data.append(URLEncoder.encode("s_opmode", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.opmode, "UTF-8"));
                data.append("&");
            }
            if (this.login.length() > 0) {
                data.append(URLEncoder.encode("s_login", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.login, "UTF-8"));
                data.append("&");
            }
            if (this.pw.length() > 0) {
                data.append(URLEncoder.encode("s_pw", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.pw, "UTF-8"));
                data.append("&");
            }
            return data.toString();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Setter method for login
     * 
     * @param value user name
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setLogin(String value) {
        this.login = value;
        return this;
    }

    /**
     * Setter method for user password
     * 
     * @param value password
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setPassword(String value) {
        this.pw = value;
        return this;
    }

    /**
     * Setter method for subuser account name
     * 
     * @param value subuser account name
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setOpMode(String value) {
        this.opmode = value;
        return this;
    }
}
