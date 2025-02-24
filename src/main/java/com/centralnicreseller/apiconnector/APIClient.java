package com.centralnicreseller.apiconnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * APIClient is the entry point class for communicating with the insanely fast
 * CentralNic Reseller (fka RRPProxy) backend api. It allows two ways of
 * communication:
 * <ul>
 * <li>session based communication</li>
 * <li>sessionless communication</li>
 * </ul>
 * A session based communication makes sense in case you use it to build your
 * own frontend on top. It allows also to use 2FA (2 Factor Auth) by providing
 * "otp" in the config parameter of the login method. A sessionless
 * communication makes sense in case you do not need to care about the above and
 * you have just to request some commands.
 *
 * Possible commands can be found
 * <a href=
 * "https://kb.centralnicreseller.com/api/api-commands/api-command-reference">here.</a>
 *
 * APIClient is a class that represents an API client for making requests to an
 * API server.
 * It provides methods for setting up the client, performing API requests, and
 * managing the API session.
 *
 * The APIClient class has the following features:
 * - Ability to set a custom logger for debug mode
 * - Ability to enable or disable debug output
 * - Methods for encoding data before sending it to the API server
 * - Methods for setting and getting the API connection URL
 * - Methods for setting and getting credentials for API communication
 * - Methods for starting and ending a session-based communication
 * - Methods for making API requests and retrieving the API response
 * - Methods for handling pagination in list queries
 *
 * This class is designed to be used in conjunction with an API server that
 * supports the provided API commands.
 * It is recommended to consult the API server documentation for more
 * information on the available commands and their usage.
 *
 * @author Team Internet Group PLC
 * @version %I%, %G%
 * @since 2.0
 */
public final class APIClient {

    /**
     * high performance proxy setup API endpoint url
     */
    public static final String CNR_CONNECTION_URL_PROXY = "http://127.0.0.1/api/call.cgi";
    /**
     * common API endpoint url production environment
     */
    public static final String CNR_CONNECTION_URL_LIVE = "https://api.rrpproxy.net/api/call.cgi";
    /**
     * common API endpoint url OTE environment
     */
    public static final String CNR_CONNECTION_URL_OTE = "https://api-ote.rrpproxy.net/api/call.cgi";

    /**
     * represents default http socket timeout
     */
    private static final int SOCKET_TIMEOUT = 300 * 1000;
    /**
     * represents default api url to communicate with
     */
    private String socketURL;
    /**
     * used for sessionbased communication (reuse of socket configuration after
     * login)
     */
    private final SocketConfig socketConfig;
    /**
     * debug mode flag
     */
    private boolean debugMode;
    /**
     * role seperator
     */
    private final String roleSeparator = ":";
    /**
     * user agent string
     */
    private String ua;
    /**
     * sub user account name
     */
    private String subUser = "";
    /**
     * additional connection settings
     */
    private final Map<String, String> curlopts;
    /**
     * logger instance
     */
    private Logger logger;

    /**
     * Class constructor. Creates an API Client ready to use.
     */
    public APIClient() {
        this.ua = "";
        this.debugMode = false;
        this.setURL(CNR_CONNECTION_URL_LIVE);
        this.socketConfig = new SocketConfig();
        this.useLIVESystem();
        this.curlopts = new HashMap<>();
        this.setDefaultLogger();
    }

    /**
     * Set a custom logger for debug mdoe
     *
     * @param logger your custom logger class instance
     * @return Current APIClient instance for method chaining
     */
    public APIClient setCustomLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    /**
     * Activate the default logger for debug mode
     *
     * @return Current APIClient instance for method chaining
     */
    public APIClient setDefaultLogger() {
        this.logger = new Logger();
        return this;
    }

    /**
     * Enable Debug Output
     *
     * @return Current APIClient instance for method chaining
     */
    public APIClient enableDebugMode() {
        this.debugMode = true;
        return this;
    }

    /**
     * Disable Debug Output
     *
     * @return Current APIClient instance for method chaining
     */
    public APIClient disableDebugMode() {
        this.debugMode = false;
        return this;
    }

    /**
     * Method to use to encode data before sending it to the API Server
     *
     * @param cmd The command to request
     * @return the ready to use, encoded request payload
     */
    public String getPOSTData(Map<String, String> cmd) {
        return this.getPOSTData(cmd, false);
    }

    /**
     * Method to use to encode data before sending it to the API server
     *
     * @param cmd     The command to request
     * @param secured if password data shall be secured for output purposes
     * @return the ready to use, encoded and secured request payload
     */
    public String getPOSTData(Map<String, String> cmd, boolean secured) {
        String pd;
        if (secured) {
            pd = this.socketConfig.getPOSTData().replaceAll("s_pw=[^&]+", "s_pw=***");
        } else {
            pd = this.socketConfig.getPOSTData();
        }
        StringBuilder data = new StringBuilder(pd);
        try {
            StringBuilder tmp = new StringBuilder("");
            Iterator<Map.Entry<String, String>> it = cmd.entrySet().iterator();
            boolean hasNext = it.hasNext();
            while (hasNext) {
                Map.Entry<String, String> pair = it.next();
                String val = pair.getValue();
                if (val != null) {
                    val = val.replaceAll("[\r\n]", "");
                    if (!"".equals(val)) { // null check included
                        tmp.append(pair.getKey());
                        tmp.append("=");
                        tmp.append(val);
                        hasNext = it.hasNext();
                        if (hasNext) {
                            tmp.append("\n");
                        }
                    }
                }
            }
            pd = tmp.toString().replaceAll("PASSWORD=[^\n]+", "PASSWORD=***");
            if (!cmd.isEmpty()) {
                data.append(URLEncoder.encode("s_command", "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(pd, "UTF-8").replace("*", "%2A"));
            }
            return data.toString().replaceAll("&$", "");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Get the API connection url that is currently set
     *
     * @return API connection url currently in use
     */
    public String getURL() {
        return this.socketURL;
    }

    /**
     * Set one time password to be used for API communication
     *
     * @return Current APIClient instance for method chaining
     */
    public APIClient setPersistent() {
        this.socketConfig.setPersistent();
        return this;
    }

    /**
     * Set an API user id
     *
     * @param value API user id
     * @return Current APIClient instance for method chaining
     */
    public APIClient setLogin(String value) {
        this.socketConfig.setLogin(value);
        return this;
    }

    /**
     * Apply session data (session id and system entity) to given client request
     * session
     *
     * @param session ClientRequest session instance
     * @return Current APIClient instance for method chaining
     */
    public APIClient saveSession(Map<String, Object> session) {
        session.put("CNRUID", this.socketConfig.getLogin());
        session.put("CNRSESSION", this.socketConfig.getSession());
        return this;
    }

    /**
     * Use existing configuration out of ClientRequest session to rebuild and reuse
     * connection
     * settings
     *
     * @param session ClientRequest session instance
     * @return Current APIClient instance for method chaining
     */
    public APIClient reuseSession(Map<String, Object> session) {
        if (!session.containsKey("CNRUID") || !session.containsKey("CNRSESSION")) {
            return this;
        }
        this.setCredentials((String) session.get("CNRUID"));
        this.socketConfig.setSession((String) session.get("CNRSESSION"));
        return this;
    }

    /**
     * Set a custom user agent header (useful for tools that use our SDK)
     *
     * @param str user agent label
     * @param rv  user agent revision
     * @return Current APIClient instance for method chaining
     */
    public APIClient setUserAgent(String str, String rv) {
        return this.setUserAgent(str, rv, new ArrayList<>());
    }

    /**
     * Set a custom user agent header (useful for tools that use our SDK)
     *
     * @param str     user agent label
     * @param rv      user agent revision
     * @param modules further modules to add to user agent string
     *                ["module/version"]
     * @return Current APIClient instance for method chaining
     */
    public APIClient setUserAgent(String str, String rv, ArrayList<String> modules) {
        StringBuilder mods = new StringBuilder(" ");
        if (!modules.isEmpty()) {
            for (int i = 0; i < modules.size(); i++) {
                mods.append(modules.get(i));
                mods.append(" ");
            }
        }
        String jv = System.getProperty("java.vm.name").toLowerCase().replaceAll(" .+", "");
        String jrv = System.getProperty("java.version");
        String arch = System.getProperty("os.arch");
        String os = System.getProperty("os.name");
        this.ua = (str + " (" + os + "; " + arch + "; rv:" + rv + ")" + mods + "java-sdk/"
                + this.getVersion() + " " + jv + "/" + jrv);
        return this;
    }

    /**
     * Get the user agent string
     *
     * @return user agent string
     */
    public String getUserAgent() {
        if (this.ua.length() == 0) {
            String jv = System.getProperty("java.vm.name").toLowerCase().replaceAll(" .+", "");
            String jrv = System.getProperty("java.version");
            String arch = System.getProperty("os.arch");
            String os = System.getProperty("os.name");
            this.ua = ("JAVA-SDK (" + os + "; " + arch + "; rv:" + this.getVersion() + ") " + jv
                    + "/" + jrv);
        }
        return this.ua;
    }

    /**
     * Set proxy to use for API communication
     *
     * @param proxy proxy to use
     * @return Current APIClient instance for method chaining
     */
    public APIClient setProxy(String proxy) {
        if (this.curlopts.containsKey("PROXY")) {
            this.curlopts.replace("PROXY", proxy);
        } else {
            this.curlopts.put("PROXY", proxy);
        }
        return this;
    }

    /**
     * Get proxy configuration for API communication
     *
     * @return Proxy URL or null if not configured
     */
    public String getProxy() {
        if (this.curlopts.containsKey("PROXY")) {
            return this.curlopts.get("PROXY");
        }
        return null;
    }

    /**
     * Set Referer Header to use for API communication
     *
     * @param referer Referer Header value
     * @return Current APIClient instance for method chaining
     */
    public APIClient setReferer(String referer) {
        if (this.curlopts.containsKey("REFERER")) {
            this.curlopts.replace("REFERER", referer);
        } else {
            this.curlopts.put("REFERER", referer);
        }
        return this;
    }

    /**
     * Get the configured Referer Header Value
     *
     * @return Referer Header Value
     */
    public String getReferer() {
        if (this.curlopts.containsKey("REFERER")) {
            return this.curlopts.get("REFERER");
        }
        return null;
    }

    /**
     * Get the current module version
     *
     * @return module version
     */
    public String getVersion() {
        return "5.0.7";
    }

    /**
     * Set another connection url to be used for API communication
     *
     * @param value API connection url to set
     * @return Current APIClient instance for method chaining
     */
    public APIClient setURL(String value) {
        this.socketURL = value;
        return this;
    }

    /**
     * Set Credentials to be used for API communication
     *
     * @param uid account name
     * @return Current APIClient instance for method chaining
     */
    public APIClient setCredentials(String uid) {
        this.socketConfig.setLogin(uid);
        return this;
    }

    /**
     * Set Credentials to be used for API communication
     *
     * @param uid account name
     * @param pw  account password
     * @return Current APIClient instance for method chaining
     */
    public APIClient setCredentials(String uid, String pw) {
        this.socketConfig.setLogin(uid);
        this.socketConfig.setPassword(pw);
        return this;
    }

    /**
     * Set Credentials to be used for API communication
     *
     * @param uid  account name
     * @param role role user id
     * @return Current APIClient instance for method chaining
     */
    public APIClient setRoleCredentials(String uid, String role) {
        if (role != null && role.length() > 0) {
            return this.setCredentials(uid + this.roleSeparator + role);
        }
        return this.setCredentials(uid);
    }

    /**
     * Set Credentials to be used for API communication
     *
     * @param uid  account name
     * @param role role user id
     * @param pw   role user password
     * @return Current APIClient instance for method chaining
     */
    public APIClient setRoleCredentials(String uid, String role, String pw) {
        if (role != null && role.length() > 0) {
            return this.setCredentials(uid + this.roleSeparator + role, pw);
        }
        return this.setCredentials(uid, pw);
    }

    /**
     * Perform API login to start session-based communication
     *
     * @return API Response
     */
    public Response login() {
        this.setPersistent();
        Map<String, Object> cmd = new HashMap<>();
        Response rr = this.request(cmd, false);
        this.socketConfig.setSession("");
        if (rr.isSuccess()) {
            Column col = rr.getColumn("SESSIONID");
            if (col != null) {
                this.socketConfig.setSession(col.getData().get(0));
            }
        }
        return rr;
    }

    /**
     * Perform API logout to close API session in use
     *
     * @return API Response
     */
    public Response logout() {
        Map<String, Object> cmd = new HashMap<>();
        cmd.put("Command", "StopSession");
        Response rr = this.request(cmd, false);
        if (rr.isSuccess()) {
            this.socketConfig.setSession("");
        }
        return rr;
    }

    /**
     * Perform API request using the given command
     *
     * @param cmd API command to request
     * @return API Response
     */
    public Response request(Map<String, Object> cmd) {
        return request(cmd, true);
    }

    /**
     * Perform API request using the given command
     *
     * @param cmd         API command to request
     * @param setUserView set user view
     * @return API Response
     */
    public Response request(Map<String, Object> cmd, boolean setUserView) {
        // set sub user id if available
        if (setUserView && !this.subUser.isEmpty()) {
            cmd.put("SUBUSER", this.subUser);
        }

        // flatten nested api command bulk parameters
        Map<String, String> newcmd = this.flattenCommand(cmd);
        // auto convert umlaut names to punycode
        newcmd = this.autoIDNConvert(newcmd);

        // request command to API
        String data = this.getPOSTData(newcmd);
        String secured = this.getPOSTData(newcmd, true);
        Map<String, String> cfg = new HashMap<>();
        cfg.put("CONNECTION_URL", this.socketURL);
        StringBuilder response = new StringBuilder("");
        Response r;
        String err = null;
        try {
            URL myurl = (new URI(cfg.get("CONNECTION_URL"))).toURL();
            HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
            con.setRequestMethod("POST");
            if (this.curlopts.containsKey("REFERER")) {
                con.setRequestProperty("REFERER", this.curlopts.get("REFERER"));
            }
            con.setRequestProperty("Content-length", String.valueOf(data.length()));
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", this.getUserAgent());
            con.setRequestProperty("Expect", "");
            con.setConnectTimeout(APIClient.SOCKET_TIMEOUT);
            con.setReadTimeout(APIClient.SOCKET_TIMEOUT);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            try (OutputStream os = con.getOutputStream()) {
                os.write(data.getBytes());
                os.flush();
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (!inputLine.isEmpty()) {
                        response.append(inputLine);
                        response.append("\n");
                    }
                }
            }
            con.disconnect();
        } catch (IOException | URISyntaxException e) {
            err = e.getMessage();
            response.append("httperror");
        }
        r = new Response(response.toString(), newcmd, cfg);
        if (this.debugMode) {
            this.logger.log(secured, r, err);
        }
        return r;
    }

    /**
     * Request the next page of list entries for the current list query Useful
     * for tables
     *
     * @param rr API Response of current page
     * @return API Response or null in case there are no further list entries
     */
    public Response requestNextResponsePage(Response rr) {
        Map<String, Object> mycmd = new HashMap<>();
        mycmd.putAll(rr.getCommand());
        if (mycmd.get("LAST") != null) {
            throw new Error(
                    "Parameter LAST in use. Please remove it to avoid issues in requestNextPage.");
        }
        int first = 0;
        if (mycmd.get("FIRST") != null) {
            first = Integer.parseInt((String) mycmd.get("FIRST"));
        }
        int total = rr.getRecordsTotalCount();
        int limit = rr.getRecordsLimitation();
        first += limit;
        if (first < total) {
            mycmd.put("FIRST", Integer.toString(first));
            mycmd.put("LIMIT", Integer.toString(limit));
            return this.request(mycmd);
        } else {
            return null;
        }
    }

    /**
     * Request all pages/entries for the given query command
     *
     * @param cmd API list command to use
     * @return List of API Responses
     */
    public ArrayList<Response> requestAllResponsePages(Map<String, String> cmd) {
        ArrayList<Response> responses = new ArrayList<>();
        Map<String, Object> mycmd = new HashMap<>(cmd);
        cmd.put("FIRST", "0");
        Response rr = this.request(mycmd);
        Response tmp = rr;
        do {
            responses.add(tmp);
            tmp = this.requestNextResponsePage(tmp);
        } while (tmp != null);
        return responses;
    }

    /**
     * Set a data view to a given subuser
     *
     * @param uid subuser account name
     * @return Current APIClient instance for method chaining
     */
    public APIClient setUserView(String uid) {
        this.subUser = uid;
        return this;
    }

    /**
     * Reset data view back from subuser to user
     *
     * @return Current APIClient instance for method chaining
     */
    public APIClient resetUserView() {
        this.subUser = "";
        return this;
    }

    /**
     * Activate High Performance Connection Setup
     *
     * @return Current APIClient instance for method chaining
     */
    public APIClient useHighPerformanceConnectionSetup() {
        this.setURL(CNR_CONNECTION_URL_PROXY);
        return this;
    }

    /**
     * Activate Default Connection Setup
     *
     * @return Current APIClient instance for method chaining
     */
    public APIClient useDefaultConnectionSetup() {
        this.setURL(CNR_CONNECTION_URL_LIVE);
        return this;
    }

    /**
     * Set OT&amp;E System for API communication
     *
     * @return Current APIClient instance for method chaining
     */
    public APIClient useOTESystem() {
        this.setURL(CNR_CONNECTION_URL_OTE);
        return this;
    }

    /**
     * Set LIVE System for API communication (this is the default setting)
     *
     * @return Current APIClient instance for method chaining
     */
    public APIClient useLIVESystem() {
        this.setURL(CNR_CONNECTION_URL_LIVE);
        return this;
    }

    /**
     * Flatten API command's nested arrays for easier handling
     *
     * @param cmd API Command
     * @return flattened API Command
     */
    private Map<String, String> flattenCommand(Map<String, Object> cmd) {
        Map<String, String> newcmd = new HashMap<>();
        if (cmd.isEmpty()) {
            return newcmd;
        }
        for (Map.Entry<String, Object> pair : cmd.entrySet()) {
            Object val = pair.getValue();
            if (val != null) {
                String key = pair.getKey().toUpperCase();
                if (val instanceof String[] param) {
                    int a = 0;
                    for (String param1 : param) {
                        String entry = param1.replaceAll("[\r\n]", "");
                        if (!"".equals(entry)) { // null check included
                            newcmd.put(key + a, entry);
                            a++;
                        }
                    }
                } else {
                    newcmd.put(key, val.toString().replaceAll("[\r\n]", ""));
                }
            }
        }
        return newcmd;
    }

    private Map<String, String> autoIDNConvert(Map<String, String> cmd) {
        if (cmd.isEmpty()) {
            return cmd;
        }
        // Define the regex patterns with case insensitivity
        String keyPattern = "^(?i)(NAMESERVER|NS|DNSZONE)([0-9]*)$";
        String objClassPattern = "^(?i)(DOMAIN(APPLICATION|BLOCKING)?|NAMESERVER|NS|DNSZONE)$";
        String asciiPattern = "^[A-Za-z0-9.\\-]+$"; // Matches alphanumeric, dot, and hyphen

        // Lists to track values to convert and their corresponding keys
        ArrayList<String> toConvert = new ArrayList<>();
        ArrayList<String> idxs = new ArrayList<>();

        // Loop through the command map entries
        for (Map.Entry<String, String> entry : cmd.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();

            // Check if the key matches the keyPattern or it's "OBJECTID" with a valid
            // OBJECTCLASS
            if ((key.matches(keyPattern)
                    || ("OBJECTID".equalsIgnoreCase(key) && cmd.containsKey("OBJECTCLASS")
                            && cmd.get("OBJECTCLASS").matches(objClassPattern)))
                    && !val.matches(asciiPattern)) {

                // Add to conversion list and track the key
                toConvert.add(val);
                idxs.add(key);
            }
        }

        // Perform conversion if there are values to convert
        if (!toConvert.isEmpty()) {
            IDNAConverter result = IDNAConverter.convert(toConvert); // Call the conversion method
            List<String> data = result.getPcList(); // Get the punycode list from conversion

            // Replace original values in cmd with their converted counterparts
            for (int idx = 0; idx < data.size(); idx++) {
                String convertedValue = data.get(idx);
                cmd.put(idxs.get(idx), convertedValue);
            }
        }

        return cmd; // Return the modified command map
    }
}
