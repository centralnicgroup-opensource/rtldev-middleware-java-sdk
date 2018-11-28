package net.hexonet.apiconnector;

import static org.junit.Assert.assertEquals;
import java.util.Map;
import org.junit.Test;

/**
 * Unit test for class ResponseTemplate
 */
public class ResponseTemplateTest {
    /**
     * Test constructor
     */
    @Test
    public void constructorVars() {
        ResponseTemplate tpl = new ResponseTemplate("");
        assertEquals(423, tpl.getCode());
        assertEquals("Empty API response", tpl.getDescription());
    }

    /**
     * Test getHash method
     */
    @Test
    public void getHash() {
        ResponseTemplate tpl = new ResponseTemplate("");
        Map<String, Object> h = tpl.getHash();
        assertEquals("423", (String) h.get("CODE"));
        assertEquals("Empty API response", (String) h.get("DESCRIPTION"));
    }

    /**
     * Test getQueuetime method #1
     */
    @Test
    public void getQueuetime1() {
        ResponseTemplate tpl = new ResponseTemplate("");
        assertEquals(0, tpl.getQueuetime(), 0);
    }

    /**
     * Test getQueuetime method #2
     */
    @Test
    public void getQueuetime2() {
        ResponseTemplate tpl = new ResponseTemplate(
                "[RESPONSE]\r\ncode=423\r\ndescription=Empty API response\r\nqueuetime=0\r\nEOF\r\n");
        assertEquals(0, tpl.getQueuetime(), 0);
    }

    /**
     * Test getRuntime method #1
     */
    @Test
    public void getRuntime1() {
        ResponseTemplate tpl = new ResponseTemplate("");
        assertEquals(0, tpl.getRuntime(), 0);
    }

    /**
     * Test getRuntime method #2
     */
    @Test
    public void getRuntime2() {
        ResponseTemplate tpl = new ResponseTemplate(
                "[RESPONSE]\r\ncode=423\r\ndescription=Empty API response\r\nruntime=0.12\r\nEOF\r\n");
        assertEquals(0.12, tpl.getRuntime(), 0);
    }
}
