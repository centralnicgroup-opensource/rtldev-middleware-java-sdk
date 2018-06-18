package net.ispapi.apiconnector;

import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for apiconnector client
 */
public class ClientTest {
    /**
     * Test README.md example for session based API communication
     */
    @Test
    public void voidREADME1() {
        // perform an api login and create an api session
        Map<String, String> cfg = new HashMap<String, String>();
        cfg.put("login", "test.user");
        cfg.put("pw", "test.passw0rd");
        cfg.put("entity", "1234");
        // --- use this for 2-Factor Auth ---
        // cfg.put("otp", "my_otp_code"); 
        // --- use this if you have active ip filter settings ---
        // cfg.put("remoteaddr", "client's remote ip address");
        Client cl = new Client();
        ListResponse r = cl.login(cfg);
        boolean allfine = true;

        if (r.isSuccess()){
            // perform further api request reusing the generated api session
            Map<String, String> cmd = new HashMap<String, String>();
            cmd.put("COMMAND", "StatusAccount");
            r = cl.request(cmd);
            if (!r.isSuccess()){
                allfine = false;
            }
            // perform api logout and destroy api session
            r = cl.logout();
            if (!r.isSuccess()){
                allfine = false;
            }
        }
        else {
            allfine = false;
        }
        assertTrue(allfine);
    }

    /**
     * Test README.md example for sessionless API communication
     */
    @Test
    public void voidREADME2() {
        boolean allfine = true;
        Map<String, String> cfg = new HashMap<String, String>();
        cfg.put("login", "test.user");
        cfg.put("pw", "test.passw0rd");
        cfg.put("entity", "1234");
        // --- use this if you have active ip filter settings ---
        // cfg.put("remoteaddr", "client's remote ip address");
        Client cl = new Client();
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        ListResponse r = cl.request(cmd, cfg);
        if (!r.isSuccess()){
            allfine = false;
        }
        assertTrue(allfine);
    }

    /**
     * Test Session Login / Logout and reuse of API Session ID
     */
    @Test
    public void testApiSession() {
        Map<String, String> cfg = new HashMap<String, String>();
        cfg.put("login", "test.user");
        cfg.put("pw", "test.passw0rd");
        cfg.put("entity", "1234");
        Client cl = new Client();
        ListResponse r = cl.login(cfg);
        assertTrue(r.isSuccess());

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "GetUserIndex");
        r = cl.request(cmd);
        assertTrue(r.isSuccess());
        assertEquals("199", r.getColumnIndex("PARENTUSERINDEX", 0));
        assertEquals("659", r.getColumnIndex("USERINDEX", 0));

        r = cl.logout();
        assertTrue(r.isSuccess());
    }

    /**
     * Test sessionless request
     */
    @Test
    public void testSessionlessRequest() {
        //not providing a socket config (so no login data)
        Client cl = new Client();
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "GetUserIndex");
        ListResponse r = cl.request(cmd);
        assertTrue(r.isError());
        assertEquals(530, r.code());
        assertEquals("SESSION NOT FOUND", r.description());
    
        //providing socket config
        Map<String, String> cfg = new HashMap<String, String>();
        cfg.put("login", "test.user");
        cfg.put("pw", "test.passw0rd");
        cfg.put("entity", "1234");
        r = cl.request(cmd, cfg);
        assertTrue(r.isSuccess());
        assertEquals("199", r.getColumnIndex("PARENTUSERINDEX", 0));
        assertEquals("659", r.getColumnIndex("USERINDEX", 0));
    }

    /**
     * Test result of encodeData method 
     */

    @Test
    public void testEncodeData() {
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "GetUserIndex");
        Map<String, String> data = new HashMap<String, String>();
        data.put("login", "test.user");
        data.put("pw", "test.passw0rd");
        data.put("entity", "1234");
        assertEquals("s_pw=test.passw0rd&s_login=test.user&s_entity=1234&s_command=COMMAND%3DGetUserIndex%0A",  Client.encodeData(data, cmd));
    }

    /**
     * Test result of encodeCommand method
     */
    @Test
    public void testEncodeCommand() {
        // Map as parameter type
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "AddDomain");
        cmd.put("DOMAIN", "friendlydomain.net");
        cmd.put("OWNERCONTACT0", "USER");
        assertEquals("DOMAIN=friendlydomain.net\nOWNERCONTACT0=USER\nCOMMAND=AddDomain\n",  Client.encodeCommand(cmd));
        // String as parameter type
        String cmdStr = "DOMAIN=friendlydomain.net\nOWNERCONTACT0=USER\nCOMMAND=AddDomain\n";
        assertEquals(cmdStr,  Client.encodeCommand(cmdStr));
    }

    /**
     * test getter and setter method for private instance var apiurl
     */
    @Test
    public void testApiUrlGetterAndSetter() {
        Client cl = new Client();
        String url = cl.getapiurl();
        assertEquals("https://coreapi.1api.net/api/call.cgi",  url);
        cl.setapiurl("http://1api.de");
        assertEquals("http://1api.de", cl.getapiurl());
        cl.setapiurl(url);
        assertEquals("https://coreapi.1api.net/api/call.cgi",  url);
    }
}
