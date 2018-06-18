package net.hexonet.apiconnector;

import java.util.Map;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.io.OutputStream;

/**
 * Client is the entry point class for communicating with the
 * insanely fast HEXONET backend api.
 * It allows two ways of communication:
 * <ul>
 * <li>session based communication</li>
 * <li>sessionless communication</li>
 * </ul>
 * A session based communication makes sense in case you use it to
 * build your own frontend on top. It allows also to use 2FA
 * (2 Factor Auth) by providing "otp" in the config parameter of
 * the login method.
 * A sessionless communication makes sense in case you do not need
 * to care about the above and you have just to request some commands.
 * 
 * Possible commands can be found <a href="https://github.com/hexonet/hexonet-api-documentation/tree/master/API">here.</a>
 * 
 * @author      Kai Schwarz
 * @version     %I%, %G%
 * @since 1.0
 */
public class Client {
    /** represents default http socket timeout */
    private static int DEFAULT_SOCKET_TIMEMOUT = 300000;
    /** represents default api url to communicate with */
    private String apiurl = "https://coreapi.1api.net/api/call.cgi";
    /** used for sessionbased communication (reuse of socket configuration after login) */
    Map<String, String> socketcfg = null;

    /** 
     * Class constructor. Creates an API Client ready to use.
     */
    public Client() {
    }

    /**
     * Method to use to encode data before sending it to the API server
     * @param p_d The socket configuration provided
     * @param p_cmd The command to request
     * @return the ready to use, encoded request payload
     */
    public static final String encodeData(Map<String, String> p_d, Map<String, String> p_cmd) {
        try {
            StringBuilder data = new StringBuilder("");
            for (Map.Entry<String, String> entry : p_d.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                try {
                    data.append(URLEncoder.encode("s_" + key, "UTF-8"));
                    data.append("=");
                    data.append(URLEncoder.encode(val, "UTF-8"));
                    data.append("&");
                } catch (UnsupportedEncodingException e) {

                }
            }
            data.append(URLEncoder.encode("s_command", "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(encodeCommand(p_cmd), "UTF-8"));
            return data.toString();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Method to use to encode the command to request before sending it to the API server
     * @param p_cmd the api command
     * @return the encoded api command
     */
    public static final String encodeCommand(String p_cmd) {
        return p_cmd;
    }

    /**
     * Method to use to encode the command to request before sending it to the API server
     * @param p_cmd the api command
     * @return the encoded api command
     */
    public static final String encodeCommand(Map<String, String> p_cmd) {
        String tmp = "";
        String val;
        for (Map.Entry<String, String> entry : p_cmd.entrySet()) {
            val = entry.getValue().replaceAll("[\r\n]", "");
            if (!"".equals(val)) {// null check included
                tmp += entry.getKey() + "=" + val + "\n";
            }
        }
        return tmp;
    }

    /**
     * Getter method for apiurl
     * @return the api url as string
     */
    public String getapiurl(){
        return apiurl;
    }

    /**
     * Setter method for apiurl
     * @param p_url api url
     */
    public void setapiurl(String p_url) {
        apiurl = p_url;
    }

    /**
     * request a command to the api server and return the response
     * this method is to be used for session based communication
     * after successful login.
     * @param p_cmd the command to request
     * @return ListRespons representing the api response
     */
    public ListResponse request(Map<String, String> p_cmd) {
        if (socketcfg == null){
            return new ListResponse(DefaultResponse.get("expired"));
        }
        return request(p_cmd, socketcfg);
    }

    /**
     * request a command to the api server and return the response
     * this method is to be used for sessionless communication.
     * @param p_cmd the command to request
     * @param p_cfg socket configuration to use
     * @return ListRespons representing the api response
     */
    public ListResponse request(Map<String, String> p_cmd, Map<String, String> p_cfg) {
        String data = encodeData(p_cfg, p_cmd);
        String response = "";
        try {
            URL myurl = new URL(apiurl);
            HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-length", String.valueOf(data.length()));
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setConnectTimeout(DEFAULT_SOCKET_TIMEMOUT);
            con.setReadTimeout(DEFAULT_SOCKET_TIMEMOUT);
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
                    response += inputLine + "\n";
                }
            }
            in.close();
        } catch (Exception e) {
            response = DefaultResponse.get("commonerror").replace("####ERRMSG####", e.getMessage());
        }
        return new ListResponse(response);
    }

    /**
     * Method to use as entry point for session based communication.
     * This method logs you in with the credentials provided
     * @param p_cfg socket configuration to use
     * @return ListResponse representing the api response
     */
    public ListResponse login(Map<String, String> p_cfg) {
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StartSession");
        return dologin(cmd, p_cfg);
    }

    /**
     * Method to use as entry point for session based communication.
     * This method logs you in with the credentials provided.
     * This method allows to provide further command parameters for startsession command.
     * @param p_cfg socket configuration to use
     * @param p_cmdparams additional command parameters for startsession command
     * @return ListResponse representing the api response
     */
    public ListResponse login(Map<String, String> p_cfg, Map<String, String> p_cmdparams) {
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StartSession");
        for (Map.Entry<String, String> entry : p_cmdparams.entrySet()) {
            cmd.put(entry.getKey(), entry.getValue());
        }
        return dologin(cmd, p_cfg);
    }

    /**
     * internal method used to login
     * @param p_cmd the api command to request
     * @param p_cfg the socket configuration to use
     * @return ListResponse representing the api response
     */
    private ListResponse dologin(Map<String, String> p_cmd, Map<String, String> p_cfg) {
        ListResponse r = request(p_cmd, p_cfg);
        if (r.code() == 200) {
            socketcfg = new HashMap<String, String>(p_cfg);
            socketcfg.remove("pw");
            socketcfg.remove("login");
            socketcfg.put("session", r.getColumnIndex("SESSION", 0));
        }
        return r;
    }

    /**
     * Method to use for session based communication.
     * This method logs you out and destroys the api session.
     * @return ListResponse representing the api response
     */
    public ListResponse logout() {
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "EndSession");
        ListResponse r = request(cmd, socketcfg);
        socketcfg = null;
        return r;
    }
}