package com.centralnicreseller.apiconnector; // of course use your namespace instead of our one

/**
 * Custom Logger class example for debug outputs
 *
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 3.2
 */
public class CustomLogger extends Logger {
    /**
     * Default constructor for CustomLogger
     */
    public CustomLogger() {
        super();
    }

    /**
     * log incoming data
     * @param post  request data
     * @param r     API response object instance
     * @param error http error message or null
     */
    @Override
    public void log(String post, Response r, String error) {
        // your logic here

        // System.out.println(r.getCommandPlain());
        // System.out.println(post);
        // if (error != null) {
        // System.err.println("HTTP communication failed: " + error);
        // }
    }
}
