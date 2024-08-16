package com.centralnicreseller.apiconnector;

/**
 * Default Logger class for debug outputs Overridable by custom integration,
 * check
 * CustomLogger.java.
 *
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 3.2
 */
public class Logger {
    /**
     * log incoming data
     *
     * @param post request data
     * @param r    API response object instance
     */
    public void log(String post, Response r) {
        this.log(post, r, null);
    }

    /**
     * log incoming data
     *
     * @param post  request data
     * @param r     API response object instance
     * @param error http error message or null
     */
    public void log(String post, Response r, String error) {
        System.out.println(r.getCommandPlain());
        System.out.println(post);
        if (error != null) {
            System.err.println("HTTP communication failed: " + error);
        }
    }
}
