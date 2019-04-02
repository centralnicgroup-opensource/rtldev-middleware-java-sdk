package net.hexonet.apiconnector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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

    /**
     * Class constructor. Creates an API Client ready to use.
     */
    public APIClient() {
        this.ua = "";
        this.debugMode = false;
        this.setURL("https://coreapi.1api.net/api/call.cgi");
        this.socketConfig = new SocketConfig();
        this.useLIVESystem();
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
     * Method to use to encode data before sending it to the API server
     * 
     * @param cmd The command to request
     * @return the ready to use, encoded request payload
     */
    public String getPOSTData(Map<String, String> cmd) {
        StringBuilder data = new StringBuilder(this.socketConfig.getPOSTData());
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
            data.append(URLEncoder.encode("s_command", "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(tmp.toString(), "UTF-8").replace("*", "%2A"));
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
     */
    public void setUserAgent(String str, String rv) {
        String jv = System.getProperty("java.vm.name").toLowerCase().replaceAll(" .+", "");
        String jrv = System.getProperty("java.version");
        String arch = System.getProperty("os.arch");
        String os = System.getProperty("os.name");
        this.ua = (str + " (" + os + "; " + arch + "; rv:" + rv + ") java-sdk/" + this.getVersion()
                + " " + jv + "/" + jrv);
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
     * Get the current module version
     * 
     * @return module version
     */
    public String getVersion() {
        return "1.3.20";
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
        Map<String, String> cmd = new HashMap<String, String>();
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
        Map<String, String> cmd = new HashMap<String, String>(params);
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
        Map<String, String> cmd = new HashMap<String, String>();
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
    public Response request(Map<String, String> cmd) {
        String data = this.getPOSTData(cmd);

        StringBuilder response;
        try {
            response = new StringBuilder("");
            URL myurl = new URL(this.socketURL);
            HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
            con.setRequestMethod("POST");
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
        } catch (Exception e) {
            ResponseTemplate tpl = ResponseTemplateManager.getInstance().getTemplate("httperror");
            response = new StringBuilder(tpl.getPlain());
            if (this.debugMode) {
                System.err.println(e);
            }
        }
        Response r = new Response(response.toString(), cmd);
        if (this.debugMode) {
            System.out.println(data);
            System.out.println(cmd);
            System.out.println(r.getPlain());
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
        Map<String, String> mycmd = this.toUpperCaseKeys(rr.getCommand());
        if (mycmd.get("LAST") != null) {
            throw new Error(
                    "Parameter LAST in use. Please remove it to avoid issues in requestNextPage.");
        }
        int first = 0;
        if (mycmd.get("FIRST") != null) {
            first = Integer.parseInt(mycmd.get("FIRST"));
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
        Map<String, String> mycmd = new HashMap<String, String>(cmd);
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
     * Translate all command parameter names to uppercase
     * 
     * @param cmd api command
     * @return api command with uppercase parameter names
     */
    private Map<String, String> toUpperCaseKeys(Map<String, String> cmd) {
        Map<String, String> newcmd = new HashMap<String, String>();
        Iterator<Map.Entry<String, String>> it = cmd.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            newcmd.put(pair.getKey().toUpperCase(), pair.getValue());
        }
        return newcmd;
    }
}
