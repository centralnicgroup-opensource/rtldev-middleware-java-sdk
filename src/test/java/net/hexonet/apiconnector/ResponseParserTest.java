package net.hexonet.apiconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Unit test for class ResponseParser
 */
public class ResponseParserTest {
    /**
     * Test parse method
     */
    @Test
    public void parse() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        String plain = rtm.generateTemplate("421", "");
        plain = plain.replace("\r\nDESCRIPTION=", "");
        Map<String, Object> h = ResponseParser.parse(plain);
        assertNull(h.get("DESCRIPTION"));
    }

    /**
     * Test serialize method #1
     */
    @Test
    public void serialize1() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));

        Map<String, Object> h = rtm.getTemplate("OK").getHash();
        Map<String, ArrayList<String>> hm = new HashMap<String, ArrayList<String>>();
        hm.put("DOMAIN", new ArrayList<String>(
                Arrays.asList("mydomain1.com", "mydomain2.com", "mydomain3.com")));
        hm.put("RATING", new ArrayList<String>(Arrays.asList("1", "2", "3")));
        hm.put("SUM", new ArrayList<String>(Arrays.asList("3")));
        h.put("PROPERTY", hm);
        assertEquals(
                "[RESPONSE]\r\nCODE=200\r\nDESCRIPTION=Command completed successfully\r\nPROPERTY[DOMAIN][0]=mydomain1.com\r\nPROPERTY[DOMAIN][1]=mydomain2.com\r\nPROPERTY[DOMAIN][2]=mydomain3.com\r\nPROPERTY[RATING][0]=1\r\nPROPERTY[RATING][1]=2\r\nPROPERTY[RATING][2]=3\r\nPROPERTY[SUM][0]=3\r\nEOF\r\n",
                ResponseParser.serialize(h));
    }

    /**
     * Test serialize method #2
     */
    @Test
    public void serialize2() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));

        ResponseTemplate tpl = rtm.getTemplate("OK");
        assertEquals(ResponseParser.serialize(tpl.getHash()), tpl.getPlain());
    }

    /**
     * Test serialize method #3
     */
    @Test
    public void serialize3() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));

        // this case shouldn't happen, otherwise we have an API-side issue
        Map<String, Object> h = rtm.getTemplate("OK").getHash();
        h.remove("CODE");
        h.remove("DESCRIPTION");
        assertEquals("[RESPONSE]\r\nEOF\r\n", ResponseParser.serialize(h));
    }

    /**
     * Test serialize method #4
     */
    @Test
    public void serialize4() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));

        Map<String, Object> h = rtm.getTemplate("OK").getHash();
        h.put("QUEUETIME", "0");
        h.put("RUNTIME", "0.12");
        assertEquals(
                "[RESPONSE]\r\nCODE=200\r\nDESCRIPTION=Command completed successfully\r\nRUNTIME=0.12\r\nQUEUETIME=0\r\nEOF\r\n",
                ResponseParser.serialize(h));
    }
}
