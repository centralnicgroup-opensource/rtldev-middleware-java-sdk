package net.hexonet.apiconnector;

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
    /** backend system entity */
    private String entity = "";
    /** user account */
    private String login = "";
    /** one time password (2FA) */
    private String otp = "";
    /** user password */
    private String pw = "";
    /** used ip address to communicate */
    private String remoteaddr = "";
    /** backend system session id */
    private String session = "";
    /** subuser account */
    private String user = "";

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
            if (this.entity.length() > 0) {
                data.append(URLEncoder.encode("s_entity", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.entity, "UTF-8"));
                data.append("&");
            }
            if (this.login.length() > 0) {
                data.append(URLEncoder.encode("s_login", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.login, "UTF-8"));
                data.append("&");
            }
            if (this.otp.length() > 0) {
                data.append(URLEncoder.encode("s_otp", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.otp, "UTF-8"));
                data.append("&");
            }
            if (this.pw.length() > 0) {
                data.append(URLEncoder.encode("s_pw", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.pw, "UTF-8"));
                data.append("&");
            }
            if (this.remoteaddr.length() > 0) {
                data.append(URLEncoder.encode("s_remoteaddr", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.remoteaddr, "UTF-8"));
                data.append("&");
            }
            if (this.session.length() > 0) {
                data.append(URLEncoder.encode("s_session", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.session, "UTF-8"));
                data.append("&");
            }
            if (this.user.length() > 0) {
                data.append(URLEncoder.encode("s_user", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.user, "UTF-8"));
                data.append("&");
            }
            return data.toString();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Method to use to get the underlying backend api session id
     * 
     * @return the backend api session id (empty string if not set)
     */
    public String getSession() {
        return this.session;
    }

    /**
     * Method to use to get the configured backend system entity. Use "54cd" for LIVE System and
     * "1234" for OT&amp;E System
     * 
     * @return the backend system entity
     */
    public String getSystemEntity() {
        return this.entity;
    }

    /**
     * Setter method for login
     * 
     * @param value user name
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setLogin(String value) {
        this.session = "";
        this.login = value;
        return this;
    }

    /**
     * Setter method for one time password (otp)
     * 
     * @param value otp
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setOTP(String value) {
        this.session = "";
        this.otp = value;
        return this;
    }

    /**
     * Setter method for user password
     * 
     * @param value password
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setPassword(String value) {
        this.session = "";
        this.pw = value;
        return this;
    }

    /**
     * Setter method for used remote ip address
     * 
     * @param value ip address
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setRemoteAddress(String value) {
        this.remoteaddr = value;
        return this;
    }

    /**
     * Setter method for backend api session id
     * 
     * @param value session id
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setSession(String value) {
        this.session = value;
        this.login = "";
        this.pw = "";
        this.otp = "";
        return this;
    }

    /**
     * Setter method for backend system entity. Use "54cd" for LIVE System and "1234" for OT&amp;E
     * System
     * 
     * @param value system entity
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setSystemEntity(String value) {
        this.entity = value;
        return this;
    }

    /**
     * Setter method for subuser account name
     * 
     * @param value subuser account name
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setUser(String value) {
        this.user = value;
        return this;
    }
}
