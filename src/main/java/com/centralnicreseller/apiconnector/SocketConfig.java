package com.centralnicreseller.apiconnector;

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
    /** backend system session id */
    private String session = "";
    /** one time password (2FA) */
    private String otp = "";

    /**
     * Class constructor.
     */
    public SocketConfig() {
    }

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
            if (this.session.length() > 0) {
                data.append(URLEncoder.encode("s_session", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(this.session, "UTF-8"));
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
     * Setter method for subuser account name
     *
     * @param value subuser account name
     * @return current SocketConfig instance to reuse for method chaining
     */
    public SocketConfig setOpMode(String value) {
        this.opmode = value;
        return this;
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
}
