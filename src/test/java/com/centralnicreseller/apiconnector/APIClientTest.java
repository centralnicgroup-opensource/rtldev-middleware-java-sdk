package com.centralnicreseller.apiconnector;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.centralnicreseller.apiconnector.Record;

/**
 * Unit test for apiconnector client
 */
public class APIClientTest {
    /**
     * Test getPOSTData method #1
     */
    @Test
    public void getPOSTData1() {
        APIClient cl = new APIClient();
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "ModifyDomain");
        cmd.put("AUTH", "gwrgwqg%&\\44t3*");
        String validate = "s_command=AUTH%3Dgwrgwqg%25%26%5C44t3%2A%0ACOMMAND%3DModifyDomain";
        String enc = cl.getPOSTData(cmd);
        assertEquals(validate, enc);
    }

    /**
     * Test getPOSTData method #2
     */
    @Test
    public void getPOSTData2() {
        APIClient cl = new APIClient();
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "ModifyDomain");
        cmd.put("AUTH", null);
        String validate = "s_command=COMMAND%3DModifyDomain";
        String enc = cl.getPOSTData(cmd);
        assertEquals(validate, enc);
    }

    /**
     * Test getPOSTData method #3
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getPOSTDataSecured() throws UnsupportedEncodingException {
        APIClient cl = new APIClient();
        cl.setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"));
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "CheckAuthentication");
        cmd.put("SUBUSER", System.getenv("CNR_TEST_USER"));
        cmd.put("PASSWORD", System.getenv("CNR_TEST_PASSWORD"));
        String enc = cl.getPOSTData(cmd, true);
        String encodedUser = URLEncoder.encode(System.getenv("CNR_TEST_USER"), "UTF-8");
        String str = "s_login=" + encodedUser + "&s_pw=***&s_command=SUBUSER%3D" + encodedUser
                + "%0APASSWORD%3D%2A%2A%2A%0ACOMMAND%3DCheckAuthentication";
        assertEquals(str, enc);
    }

    /**
     * Test enableDebugMode method
     */
    @Test
    public void enableDebugMode() {
        APIClient cl = new APIClient();
        cl.enableDebugMode();
    }

    /**
     * Test disableDebugMode method
     */
    @Test
    public void disableDebugMode() {
        APIClient cl = new APIClient();
        cl.disableDebugMode();
    }

    /**
     * Test getURL method
     */
    @Test
    public void getURL() {
        APIClient cl = new APIClient();
        String url = cl.getURL();
        assertEquals(APIClient.CNR_CONNECTION_URL_LIVE, url);
    }

    /**
     * Test getUserAgent method
     */
    @Test
    public void getUserAgent() {
        APIClient cl = new APIClient();
        String jv = System.getProperty("java.vm.name").toLowerCase().replaceAll(" .+", "");
        String jrv = System.getProperty("java.version");
        String arch = System.getProperty("os.arch");
        String os = System.getProperty("os.name");
        String uaexpected = ("JAVA-SDK (" + os + "; " + arch + "; rv:" + cl.getVersion() + ") " + jv
                + "/" + jrv);
        assertEquals(cl.getUserAgent(), uaexpected);
    }

    /**
     * Test setUserAgent method
     */
    @Test
    public void setUserAgent() {
        String pid = "WHMCS";
        String rv = "7.7.0";
        APIClient cl = new APIClient();
        cl.setUserAgent(pid, rv);
        String jv = System.getProperty("java.vm.name").toLowerCase().replaceAll(" .+", "");
        String jrv = System.getProperty("java.version");
        String arch = System.getProperty("os.arch");
        String os = System.getProperty("os.name");
        String uaexpected = (pid + " (" + os + "; " + arch + "; rv:" + rv + ") java-sdk/"
                + cl.getVersion() + " " + jv + "/" + jrv);
        assertEquals(cl.getUserAgent(), uaexpected);
    }

    /**
     * Test setUserAgent method by including additional modules
     */
    @Test
    public void setUserAgentModules() {
        String pid = "WHMCS";
        String rv = "7.7.0";
        ArrayList<String> mods = new ArrayList<>();
        mods.add("reg/2.6.2");
        mods.add("ssl/7.2.2");
        mods.add("dc/8.2.2");

        APIClient cl = new APIClient();
        cl.setUserAgent(pid, rv, mods);
        String jv = System.getProperty("java.vm.name").toLowerCase().replaceAll(" .+", "");
        String jrv = System.getProperty("java.version");
        String arch = System.getProperty("os.arch");
        String os = System.getProperty("os.name");
        String uaexpected = (pid + " (" + os + "; " + arch + "; rv:" + rv
                + ") reg/2.6.2 ssl/7.2.2 dc/8.2.2 java-sdk/" + cl.getVersion() + " " + jv + "/"
                + jrv);
        assertEquals(cl.getUserAgent(), uaexpected);
    }

    /**
     * Test setURL method
     */
    @Test
    public void setURL() {
        APIClient cl = new APIClient();
        String url = cl.setURL(APIClient.CNR_CONNECTION_URL_PROXY).getURL();
        assertEquals(APIClient.CNR_CONNECTION_URL_PROXY, url);
    }

    /**
     * Test setCredentials method #1
     */
    @Test
    public void setCredentials1() {
        APIClient cl = new APIClient();
        cl.setCredentials("myaccountid", "mypassword");
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "StatusAccount");
        String tmp = cl.getPOSTData(cmd);
        String validate = "s_login=myaccountid&s_pw=mypassword&s_command=COMMAND%3DStatusAccount";
        assertEquals(validate, tmp);
    }

    /**
     * Test setCredentials method #2
     */
    @Test
    public void setCredentials2() {
        APIClient cl = new APIClient();
        cl.setCredentials("myaccountid", "mypassword").setCredentials("", "");
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "StatusAccount");
        String tmp = cl.getPOSTData(cmd);
        String validate = "s_command=COMMAND%3DStatusAccount";
        assertEquals(validate, tmp);
    }

    /**
     * Test setCredentials method #3
     */
    @Test
    public void setCredentials3() {
        APIClient cl = new APIClient();
        // create sessionobj with login and session
        Map<String, Object> sessionobj = new HashMap<>();
        sessionobj.put("CNRUID", "myaccountid");
        sessionobj.put("CNRSESSION", "12345678");
        cl.reuseSession(sessionobj);
        cl.setCredentials("myaccountid", "mypassword");
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "StatusAccount");
        String tmp = cl.getPOSTData(cmd);
        String validate = "s_login=myaccountid&s_pw=mypassword&s_command=COMMAND%3DStatusAccount";
        assertEquals(validate, tmp);
    }

    /**
     * Test setRoleCredentials method #1
     */
    @Test
    public void setRoleCredentials1() {
        APIClient cl = new APIClient();
        cl.setRoleCredentials("myaccountid", "myroleid", "mypassword");
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "StatusAccount");
        String tmp = cl.getPOSTData(cmd);
        String validate = "s_login=myaccountid%3Amyroleid&s_pw=mypassword&s_command=COMMAND%3DStatusAccount";
        assertEquals(validate, tmp);
    }

    /**
     * Test setRoleCredentials method #2
     */
    @Test
    public void setRoleCredentials2() {
        APIClient cl = new APIClient();
        cl.setRoleCredentials("myaccountid", "myroleid", "mypassword").setRoleCredentials("", "",
                "");
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "StatusAccount");
        String tmp = cl.getPOSTData(cmd);
        String validate = "s_command=COMMAND%3DStatusAccount";
        assertEquals(validate, tmp);
    }

    /**
     * Test saveSession / reuseSession method
     */
    @Test
    public void saveANDreuseSession() {
        APIClient cl = new APIClient();
        Map<String, Object> sessionobj = new HashMap<>();
        Map<String, Object> sessionobj2 = new HashMap<>();
        sessionobj.put("CNRUID", "myaccountid");
        sessionobj.put("CNRSESSION", "12345678");
        cl.reuseSession(sessionobj);
        cl.saveSession(sessionobj2).enableDebugMode();
        APIClient cl2 = new APIClient();
        cl2.reuseSession(sessionobj2);
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "StatusAccount");
        String tmp = cl2.getPOSTData(cmd);
        String validate = "s_login=myaccountid&s_sessionid=12345678&s_command=COMMAND%3DStatusAccount";
        assertEquals(validate, tmp);
    }

    /**
     * Test login method #1
     */
    @Test
    public void login1() {
        APIClient cl = new APIClient();
        cl.useOTESystem().setCredentials(System.getenv("CNR_TEST_USER"),
                System.getenv("CNR_TEST_PASSWORD"));
        Response r = cl.login();

        assertTrue(r.isSuccess());
        Record rec = r.getRecord(0);
        assertNotNull(rec);
        String sessid = rec.getDataByKey("SESSIONID");
        assertNotNull(sessid);
    }

    // /**
    // * Test login method #2
    // */
    // /*
    // * @Test public void login2() { APIClient cl = new APIClient();
    // * cl.useOTESystem().setRoleCredentials(System.getenv("CNR_TEST_USER"),
    // "testrole", System.getenv("CNR_TEST_PASSWORD"))
    // * .setRemoteIPAddress("1.2.3.4"); Response r = cl.login();
    // assertTrue(r.isSuccess()); Record
    // * rec = r.getRecord(0); assertNotNull(rec); String sessid =
    // rec.getDataByKey("SESSIONID");
    // * assertNotNull(sessid); assertEquals(sessid, cl.getSession()); }
    // */

    /**
     * Test login method #3
     */
    @Test
    public void login3() {
        APIClient cl = new APIClient();
        cl.useOTESystem().setCredentials(System.getenv("CNR_TEST_USER"),
                "WRONGPASSWORD");
        Response r = cl.login();
        assertTrue(r.isError());
    }

    // // login4 - http timeout
    // // login5 - no SESSION Property returned

    /**
     * Test logout method #1
     */

    @Test
    public void logout1() {
        APIClient cl = new APIClient();
        cl.useOTESystem().setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"));
        Response r = cl.login();
        assertTrue(r.isSuccess());
        cl.enableDebugMode();
        r = cl.logout();
        assertTrue(r.isSuccess());
    }

    /**
     * Test logout method #2
     */
    @Test
    public void logout2() {
        Map<String, Object> sessionobj = new HashMap<>();
        APIClient cl = new APIClient();
        cl.useOTESystem().setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"));
        Response r = cl.login();
        assertTrue(r.isSuccess());
        cl.saveSession(sessionobj);
        APIClient cl2 = new APIClient();
        cl2.reuseSession(sessionobj);
        Response r2 = cl2.logout();
        assertTrue(r2.isError());
    }

    /**
     * Test request method #1
     */
    @Test
    public void request1() {
        APIClient cl = new APIClient();
        cl.enableDebugMode().useOTESystem().setURL(cl.getURL().replace("api", "wrongcoreapi"))
                .setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"));

        Map<String, Object> cmd = new HashMap<>();
        cmd.put("COMMAND", "GetUserIndex");
        Response r = cl.request(cmd);
        assertTrue(r.isTmpError());
        Response tpl = ResponseTemplateManager.getTemplate("httperror");
        assertEquals(tpl.getCode(), r.getCode());
        assertEquals(tpl.getDescription(), r.getDescription());
    }

    /**
     * Test request method #2
     */
    @Test
    public void request2() {
        APIClient cl = new APIClient();
        cl.useOTESystem().setURL(cl.getURL().replace("api", "wrongcoreapi"))
                .setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"));
        Map<String, Object> cmd = new HashMap<>();
        cmd.put("COMMAND", "GetUserIndex");
        Response r = cl.request(cmd);
        assertTrue(r.isTmpError());
        Response tpl = ResponseTemplateManager.getTemplate("httperror");
        assertEquals(tpl.getCode(), r.getCode());
        assertEquals(tpl.getDescription(), r.getDescription());
    }

    /**
     * Test request method #3 (flattenCommand and autoIDNConvert)
     */
    @Test
    public void request3() {
        APIClient cl = new APIClient();
        cl.setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"))
                .useOTESystem();
        String[] domains = { "example.com", "example.net", "dömäin.example" };
        Map<String, Object> cmd = new HashMap<>();
        cmd.put("COMMAND", "CheckDomains");
        cmd.put("DOMAIN", domains);
        Response r = cl.request(cmd);

        assertTrue(r.isSuccess());
        assertEquals(200, r.getCode());
        assertEquals("Command completed successfully", r.getDescription());

        Map<String, String> newcmd = r.getCommand();
        assertTrue(newcmd.containsKey("DOMAIN0"));
        assertTrue(newcmd.containsKey("DOMAIN1"));
        assertTrue(newcmd.containsKey("DOMAIN2"));
        assertFalse(newcmd.containsKey("DOMAIN"));
        assertEquals("example.com", newcmd.get("DOMAIN0"));
        assertEquals("example.net", newcmd.get("DOMAIN1"));
        assertEquals("dömäin.example", newcmd.get("DOMAIN2"));
    }

    /**
     * Test request method #4 (autoIDNConvert)
     */
    @Test
    public void request4() {
        APIClient cl = new APIClient();
        cl.setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"))
                .useOTESystem()
                .enableDebugMode();
        Map<String, Object> cmd = new HashMap<>();
        cmd.put("COMMAND", "StatusDNSZone");
        cmd.put("DNSZONE", "hallööö.com");
        Response r = cl.request(cmd);
        assertTrue(r.isSuccess());
        assertEquals(200, r.getCode());
        assertEquals("Command completed successfully", r.getDescription());
        Map<String, String> newcmd = r.getCommand();
        assertTrue(newcmd.containsKey("DNSZONE"));
        assertEquals("xn--hall-8qaaa.com", newcmd.get("DNSZONE"));
    }

    /**
     * Test requestNextResponsePage method #1
     */
    @Test
    public void requestNextResponsePage1() {
        // Disable debug mode if applicable
        APIClient cl = new APIClient();

        cl.disableDebugMode(); // Assuming there's such a method in APIClient

        // Prepare request parameters
        Map<String, Object> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        cmd.put("LIMIT", "2");

        // Set credentials and system
        cl.setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"))
                .useOTESystem();

        // Request the initial response
        Response r = cl.request(cmd);
        assertNotNull(r);
        assertTrue(r.isSuccess());

        // Request the next response page
        Response nr = cl.requestNextResponsePage(r);
        assertNotNull(nr);
        assertTrue(nr.isSuccess());

        // Assertions for initial response
        assertEquals(2, r.getRecordsLimitation());
        assertEquals(2, r.getRecordsCount());
        assertEquals(0, r.getFirstRecordIndex());
        assertEquals(1, r.getLastRecordIndex());

        // Assertions for next response page
        assertEquals(2, nr.getRecordsLimitation());
        assertEquals(2, nr.getRecordsCount());
        assertEquals(2, nr.getFirstRecordIndex());
        assertEquals(3, nr.getLastRecordIndex());
    }

    /**
     * Test requestNextResponsePage method #2
     */
    @Test
    public void testRequestNextResponsePageLast() {
        APIClient cl = new APIClient();

        // Prepare the request parameters
        Map<String, Object> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        cmd.put("LIMIT", "2");
        cmd.put("FIRST", "0");
        cmd.put("LAST", "1");

        // Set credentials and system
        cl.setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"))
                .useOTESystem();

        // Create a response object
        Response r = cl.request(cmd);

        // Expect an error to be thrown when requesting the next page
        Error thrownError = assertThrows(Error.class, () -> {
            cl.requestNextResponsePage(r);
        });

        // Verify the exception message
        assertEquals("Parameter LAST in use. Please remove it to avoid issues in requestNextPage.",
                thrownError.getMessage());

    }

    /**
     * Test requestNextResponsePage method #3
     */
    @Test
    public void requestNextResponsePage3() {
        APIClient cl = new APIClient();
        cl.setCredentials(System.getenv("CNR_TEST_USER"),
                System.getenv("CNR_TEST_PASSWORD")).useOTESystem()
                .disableDebugMode();
        ResponseTemplateManager.addTemplate("listP0",
                "[RESPONSE]\r\nproperty[total][0] = 4\r\nproperty[first][0] = 0\r\nproperty[domain][0] = cnic-ssl-test1.com\r\nproperty[domain][1] = cnic-ssl-test2.com\r\nproperty[count][0] = 2\r\nproperty[last][0] = 1\r\nproperty[limit][0] = 2\r\ndescription = Command completed successfully\r\ncode = 200\r\nqueuetime = 0\r\nruntime = 0.007\r\nEOF\r\n");
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        cmd.put("LIMIT", "2");
        Response r = new Response("listP0", cmd);
        Response nr = cl.requestNextResponsePage(r);
        assertTrue(r.isSuccess());
        assertTrue(nr.isSuccess());
        assertEquals(2, r.getRecordsLimitation());
        assertEquals(2, nr.getRecordsLimitation());
        assertEquals(2, r.getRecordsCount());
        assertEquals(2, nr.getRecordsCount());
        assertEquals(0, r.getFirstRecordIndex());
        assertEquals(1, r.getLastRecordIndex());
    }

    /**
     * Test requestAllResponsePages method
     */
    @Test
    public void requestAllResponsePages() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        cmd.put("LIMIT", "10000");
        cmd.put("FIRST", "0");
        APIClient cl = new APIClient();
        cl.setCredentials(System.getenv("CNR_TEST_USER"),
                System.getenv("CNR_TEST_PASSWORD"))
                .useOTESystem();
        ArrayList<Response> nr = cl.requestAllResponsePages(cmd);
        assertTrue(!nr.isEmpty());
    }

    /**
     * Test setProxy method
     */
    @Test
    public void setProxy() {
        APIClient cl = new APIClient();
        cl.setProxy("127.0.0.1");
        assertEquals("127.0.0.1", cl.getProxy());
    }

    /**
     * Test setReferer method
     */
    @Test
    public void setReferer() {
        APIClient cl = new APIClient();
        cl.setReferer("https://www.centralnicreseller.com/");
        assertEquals("https://www.centralnicreseller.com/", cl.getReferer());
    }

    /**
     * Test useHighPerformanceConnectionSetup method
     */
    @Test
    public void useHighPerformanceConnectionSetup() {
        APIClient cl = new APIClient();
        cl.useHighPerformanceConnectionSetup();
        assertEquals(APIClient.CNR_CONNECTION_URL_PROXY, cl.getURL());
    }

    /**
     * Test useDefaultConnectionSetup method
     */
    @Test
    public void useDefaultConnectionSetup() {
        APIClient cl = new APIClient();
        cl.useHighPerformanceConnectionSetup();
        cl.useDefaultConnectionSetup();
        assertEquals(APIClient.CNR_CONNECTION_URL_LIVE, cl.getURL());
    }

    /**
     * Test setUserView method
     */
    @Test
    public void setUserView() {
        Map<String, Object> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryUserList");
        APIClient cl = new APIClient();
        cl.setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"))
                .useOTESystem()
                .setUserView("julia");

        Response r = cl.request(cmd);
        assertTrue(r.isSuccess());

        String pd = cl.getPOSTData(r.getCommand());
        assertTrue(pd.contains("julia"));
    }

    /**
     * Test setUserView method for sessions
     */
    @Test
    public void setUserView2() {
        APIClient cl = new APIClient();
        cl.setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"))
                .useOTESystem()
                .setUserView("julia");
        Response r = cl.login();

        String pd = cl.getPOSTData(r.getCommand());
        assertFalse(pd.contains("julia"));
    }

    /**
     * Test resetUserView method
     */
    @Test
    public void resetUserView() {
        Map<String, Object> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryUserList");

        APIClient cl = new APIClient();
        cl.setCredentials(System.getenv("CNR_TEST_USER"), System.getenv("CNR_TEST_PASSWORD"))
                .useOTESystem()
                .setUserView("julia")
                .resetUserView();
        Response r = cl.request(cmd);
        assertTrue(r.isSuccess());

        String pd = cl.getPOSTData(r.getCommand());
        assertFalse(pd.contains("julia"));
    }
}
