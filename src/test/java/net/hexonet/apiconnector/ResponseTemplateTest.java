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
        assertEquals("Empty API response. Probably unreachable API end point {CONNECTION_URL}",
                tpl.getDescription());
    }

    /**
     * Test constructor with invalid API response
     */
    @Test
    public void invalidResponse() {
        ResponseTemplate rt =
                new ResponseTemplate("[RESPONSE]\r\ncode=200\r\nqueuetime=0\r\nEOF\r\n");
        assertEquals(423, rt.getCode());
        assertEquals("Invalid API response. Contact Support", rt.getDescription());
    }

    /**
     * Test getHash method
     */
    @Test
    public void getHash() {
        ResponseTemplate tpl = new ResponseTemplate("");
        Map<String, Object> h = tpl.getHash();
        assertEquals("423", (String) h.get("CODE"));
        assertEquals("Empty API response. Probably unreachable API end point {CONNECTION_URL}",
                (String) h.get("DESCRIPTION"));
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
                "[RESPONSE]\r\ncode=423\r\ndescription=Empty API response. Probably unreachable API end point\r\nqueuetime=0\r\nEOF\r\n");
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
                "[RESPONSE]\r\ncode=423\r\ndescription=Empty API response. Probably unreachable API end point\r\nruntime=0.12\r\nEOF\r\n");
        assertEquals(0.12, tpl.getRuntime(), 0);
    }

    /**
     * Test isPending method #1
     */
    @Test
    public void isPending1() {
        ResponseTemplate tpl = new ResponseTemplate("");
        assertEquals(false, tpl.isPending());
    }

    /**
     * Test isPending method #2
     */
    @Test
    public void isPending2() {
        ResponseTemplate tpl = new ResponseTemplate(
                "[RESPONSE]\r\ncode=423\r\ndescription=Empty API response. Probably unreachable API end point\r\npending=1\r\nEOF\r\n");
        assertEquals(true, tpl.isPending());
    }
}
