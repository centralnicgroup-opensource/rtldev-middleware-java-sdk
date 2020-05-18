package net.hexonet.apiconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class ResponseTranslatorTest {
    /**
     * Test place holder vars replacement mechanism
     */
    @Test
    public void placeHolderReplacements() {
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");

        // ensure no vars are returned in response, just in case no place holder replacements are
        // provided
        Response r = new Response("", cmd);
        String regex = "\\{[^}]+\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(r.getDescription());
        assertFalse(matcher.find());

        // ensure variable replacements are correctly handled in case place holder replacements are
        // provided
        Map<String, String> ph = Map.ofEntries(Map.entry("CONNECTION_URL", "123HXPHFOUND123"));
        r = new Response("", cmd, ph);
        regex = "123HXPHFOUND123";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(r.getDescription());
        assertTrue(matcher.find());
    }

    /**
     * Test isTemplateMatchHash method
     */
    @Test
    public void isTemplateMatchHash() {
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        Response tpl = new Response("");
        assertTrue(ResponseTemplateManager.isTemplateMatchHash(tpl.getHash(), "empty"));
    }

    /**
     * Test isTemplateMatchPlain method
     */
    @Test
    public void isTemplateMatchPlain() {
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        Response tpl = new Response("");
        assertTrue(ResponseTemplateManager.isTemplateMatchPlain(tpl.getPlain(), "empty"));
    }

    /**
     * Test constructor
     */
    @Test
    public void constructorVars() {
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        Response tpl = new Response("");
        assertEquals(423, tpl.getCode());
        assertEquals("Empty API response. Probably unreachable API end point",
                tpl.getDescription());
    }

    /**
     * Test constructor with invalid API response
     */
    @Test
    public void invalidResponse() {
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        String raw = ResponseTranslator
                .translate("[RESPONSE]\r\ncode=200\r\nqueuetime=0\r\nEOF\r\n", cmd);

        Response rt = new Response(raw);
        assertEquals(423, rt.getCode());
        assertEquals("Invalid API response. Contact Support", rt.getDescription());
    }

    /**
     * Test getHash method
     */
    @Test
    public void getHash() {
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        Response tpl = new Response("");
        Map<String, Object> h = tpl.getHash();
        assertEquals("423", (String) h.get("CODE"));
        assertEquals("Empty API response. Probably unreachable API end point",
                (String) h.get("DESCRIPTION"));
    }
}
