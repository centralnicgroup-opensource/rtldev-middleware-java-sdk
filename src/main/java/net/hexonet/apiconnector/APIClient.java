package net.hexonet.apiconnector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/**
 * APIClient is the entry point class for communicating with the insanely fast HEXONET backend api.
 * It allows two ways of communication:
 * <ul>
 * <li>session based communication</li>
 * <li>sessionless communication</li>
 * </ul>
 * A session based communication makes sense in case you use it to build your own frontend on top.
 * It allows also to use 2FA (2 Factor Auth) by providing "otp" in the config parameter of the login
 * method. A sessionless communication makes sense in case you do not need to care about the above
 * and you have just to request some commands.
 * 
 * Possible commands can be found
 * <a href= "https://github.com/hexonet/hexonet-api-documentation/tree/master/API">here.</a>
 * 
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public class APIClient {
    public static final String ISPAPI_CONNECTION_URL_PROXY = "http://127.0.0.1/api/call.cgi";
    public static final String ISPAPI_CONNECTION_URL = "https://api.ispapi.net/api/call.cgi";

    /** represents default http socket timeout */
    private static int socketTimeout = 300000;
    /** represents default api url to communicate with */
    private String socketURL;
    /**
     * used for sessionbased communication (reuse of socket configuration after login)
     */
    private SocketConfig socketConfig;
    /** debug mode flag */
    private boolean debugMode;
    /** user agent string */
    private String ua;
    /** additional connection settings */
    private Map<String, String> curlopts;
    /** logger instance */
    private Logger logger;

    /**
     * Class constructor. Creates an API Client ready to use.
     */
    public APIClient() {
        this.ua = "";
        this.debugMode = false;
        this.setURL(ISPAPI_CONNECTION_URL);
        this.socketConfig = new SocketConfig();
        this.useLIVESystem();
        this.curlopts = new HashMap<String, String>();
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
                    if (!"".equals(val)) {// null check included
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
            data.append(URLEncoder.encode("s_command", "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(pd, "UTF-8").replace("*", "%2A"));
            return data.toString();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Get the API Session that is currently set
     * 
     * @return API Session or null
     */
    public String getSession() {
        String sessid = this.socketConfig.getSession();
        return (sessid == "") ? null : sessid;
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
     * Set a custom user agent header (useful for tools that use our SDK)
     * 
     * @param str user agent label
     * @param rv  user agent revision
     * @return Current APIClient instance for method chaining
     */
    public APIClient setUserAgent(String str, String rv) {
        return this.setUserAgent(str, rv, new ArrayList<String>());
    }

    /**
     * Set a custom user agent header (useful for tools that use our SDK)
     * 
     * @param str     user agent label
     * @param rv      user agent revision
     * @param modules further modules to add to user agent string ["module/version"]
     * @return Current APIClient instance for method chaining
     */
    public APIClient setUserAgent(String str, String rv, ArrayList<String> modules) {
        StringBuilder mods = new StringBuilder(" ");
        if (modules.size() > 0) {
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
        return "4.0.2";
    }

    /**
     * Apply session data (session id and system entity) to given client request session
     * 
     * @param session ClientRequest session instance
     * @return Current APIClient instance for method chaining
     */
    public APIClient saveSession(Map<String, Object> session) {
        session.put("HXAPIentity", this.socketConfig.getSystemEntity());
        session.put("HXAPIsession", this.socketConfig.getSession());
        return this;
    }

    /**
     * Use existing configuration out of ClientRequest session to rebuild and reuse connection
     * settings
     * 
     * @param session ClientRequest session instance
     * @return Current APIClient instance for method chaining
     */
    public APIClient reuseSession(Map<String, Object> session) {
        this.socketConfig.setSystemEntity((String) session.get("HXAPIentity"));
        this.setSession((String) session.get("HXAPIsession"));
        return this;
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
     * Set one time password to be used for API communication
     * 
     * @param value one time password
     * @return Current APIClient instance for method chaining
     */
    public APIClient setOTP(String value) {
        this.socketConfig.setOTP(value);
        return this;
    }

    /**
     * Set an API session id to be used for API communication
     * 
     * @param value API session id
     * @return Current APIClient instance for method chaining
     */
    public APIClient setSession(String value) {
        this.socketConfig.setSession(value);
        return this;
    }

    /**
     * Set an Remote IP Address to be used for API communication To be used in case you have an
     * active ip filter setting.
     * 
     * @param value Remote IP Address
     * @return Current APIClient instance for method chaining
     */
    public APIClient setRemoteIPAddress(String value) {
        this.socketConfig.setRemoteAddress(value);
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
     * @param pw   role user password
     * @return Current APIClient instance for method chaining
     */
    public APIClient setRoleCredentials(String uid, String role, String pw) {
        if (role != null && role.length() > 0) {
            return this.setCredentials(uid + "!" + role, pw);
        }
        return this.setCredentials(uid, pw);
    }

    /**
     * Perform API login to start session-based communication
     * 
     * @param otp optional one time password
     * @return API Response
     */
    public Response login(String otp) {
        this.setOTP(otp);
        return this.login();
    }

    /**
     * Perform API login to start session-based communication
     * 
     * @return API Response
     */
    public Response login() {
        Map<String, Object> cmd = new HashMap<String, Object>();
        cmd.put("COMMAND", "StartSession");
        Response rr = this.request(cmd);
        if (rr.isSuccess()) {
            Column col = rr.getColumn("SESSION");
            if (col != null) {
                this.setSession(col.getData().get(0));
            } else {
                this.setSession("");
            }
        }
        return rr;
    }

    /**
     * Perform API login to start session-based communication. Use given specific command
     * parameters.
     * 
     * @param params given specific command parameters
     * @param otp    optional one time password
     * @return API Response
     */
    public Response loginExtended(Map<String, String> params, String otp) {
        this.setOTP(otp);
        return this.loginExtended(params);
    }

    /**
     * Perform API login to start session-based communication. Use given specific command
     * parameters.
     * 
     * @param params given specific command parameters
     * @return API Response
     */
    public Response loginExtended(Map<String, String> params) {
        Map<String, Object> cmd = new HashMap<String, Object>(params);
        cmd.put("COMMAND", "StartSession");
        Response rr = this.request(cmd);
        if (rr.isSuccess()) {
            Column col = rr.getColumn("SESSION");
            if (col != null) {
                this.setSession(col.getData().get(0));
            } else {
                this.setSession("");
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
        Map<String, Object> cmd = new HashMap<String, Object>();
        cmd.put("Command", "EndSession");
        Response rr = this.request(cmd);
        if (rr.isSuccess()) {
            this.setSession("");
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
        // flatten nested api command bulk parameters
        Map<String, String> newcmd = this.flattenCommand(cmd);
        // auto convert umlaut names to punycode
        newcmd = this.autoIDNConvert(newcmd);

        // request command to API
        String data = this.getPOSTData(newcmd);
        String secured = this.getPOSTData(newcmd, true);
        Map<String, String> cfg = new HashMap<String, String>();
        cfg.put("CONNECTION_URL", this.socketURL);

        StringBuilder response = new StringBuilder("");
        Response r;
        String err = null;
        try {
            URL myurl = new URL(cfg.get("CONNECTION_URL"));
            HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
            if (this.curlopts.containsKey("PROXY")) {
                URL proxyurl = new URL(this.curlopts.get("PROXY"));
                Proxy proxy = new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(proxyurl.getHost(), proxyurl.getPort()));
                con = (HttpsURLConnection) myurl.openConnection(proxy);
            } else {
                con = (HttpsURLConnection) myurl.openConnection();
            }
            con.setRequestMethod("POST");
            if (this.curlopts.containsKey("REFERER")) {
                con.setRequestProperty("REFERER", this.curlopts.get("REFERER"));
            }
            con.setRequestProperty("Content-length", String.valueOf(data.length()));
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", this.getUserAgent());
            con.setRequestProperty("Expect", "");
            con.setConnectTimeout(APIClient.socketTimeout);
            con.setReadTimeout(APIClient.socketTimeout);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            OutputStream os = con.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (!inputLine.isEmpty()) {
                    response.append(inputLine);
                    response.append("\n");
                }
            }
            in.close();
            con.disconnect();
        } catch (Exception e) {
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
     * Request the next page of list entries for the current list query Useful for tables
     * 
     * @param rr API Response of current page
     * @return API Response or null in case there are no further list entries
     */
    public Response requestNextResponsePage(Response rr) {
        Map<String, Object> mycmd = new HashMap<String, Object>();
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
        ArrayList<Response> responses = new ArrayList<Response>();
        Map<String, Object> mycmd = new HashMap<String, Object>(cmd);
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
        this.socketConfig.setUser(uid);
        return this;
    }

    /**
     * Reset data view back from subuser to user
     * 
     * @return Current APIClient instance for method chaining
     */
    public APIClient resetUserView() {
        this.socketConfig.setUser("");
        return this;
    }

    /**
     * Activate High Performance Connection Setup (see README.md)
     * 
     * @return Current APIClient instance for method chaining
     */
    public APIClient useHighPerformanceConnectionSetup() {
        this.setURL(ISPAPI_CONNECTION_URL_PROXY);
        return this;
    }

    /**
     * Activate Default Connection Setup
     * 
     * @return Current APIClient instance for method chaining
     */
    public APIClient useDefaultConnectionSetup() {
        this.setURL(ISPAPI_CONNECTION_URL);
        return this;
    }

    /**
     * Set OT&amp;E System for API communication
     * 
     * @return Current APIClient instance for method chaining
     */
    public APIClient useOTESystem() {
        this.socketConfig.setSystemEntity("1234");
        return this;
    }

    /**
     * Set LIVE System for API communication (this is the default setting)
     * 
     * @return Current APIClient instance for method chaining
     */
    public APIClient useLIVESystem() {
        this.socketConfig.setSystemEntity("54cd");
        return this;
    }

    /**
     * Flatten API command's nested arrays for easier handling
     * 
     * @param cmd API Command
     * @return flattened API Command
     */
    private Map<String, String> flattenCommand(Map<String, Object> cmd) {
        Map<String, String> newcmd = new HashMap<String, String>();
        Iterator<Map.Entry<String, Object>> it = cmd.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pair = it.next();
            Object val = pair.getValue();
            if (val != null) {
                String key = pair.getKey().toUpperCase();
                if (val instanceof String[]) {
                    String[] param = (String[]) val;
                    int a = 0;
                    for (int i = 0; i < param.length; i++) {
                        String entry = param[i].replaceAll("[\r\n]", "");
                        if (!"".equals(entry)) {// null check included
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
        if (cmd.get("COMMAND").equalsIgnoreCase("ConvertIDN")) {
            return cmd;
        }
        ArrayList<String> toconvert = new ArrayList<String>();
        ArrayList<String> keys = new ArrayList<String>();
        for (Map.Entry<String, String> entry : cmd.entrySet()) {
            String key = entry.getKey();
            if (key.matches("^(DOMAIN|NAMESERVER|DNSZONE)([0-9]*)$")) {
                keys.add(key);
            }
        }
        if (keys.isEmpty()) {
            return cmd;
        }
        ArrayList<String> idxs = new ArrayList<String>();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String val = cmd.get(key);
            if (!val.matches("^[A-Za-z0-9. -]+$")) {
                idxs.add(key);
                toconvert.add(val);
            }
        }
        if (toconvert.isEmpty()) {
            return cmd;
        }
        Map<String, Object> convertcmd = new HashMap<String, Object>();
        convertcmd.put("COMMAND", "ConvertIDN");
        convertcmd.put("DOMAIN", toconvert.toArray(new String[toconvert.size()]));
        Response r = this.request(convertcmd);
        if (r.isSuccess()) {
            Column col = r.getColumn("ACE");
            if (col != null) {
                ArrayList<String> data = col.getData();
                for (int idx = 0; idx < data.size(); idx++) {
                    String pc = data.get(idx);
                    cmd.replace(idxs.get(idx), pc);
                }
            }
        }
        return cmd;
    }
}
