package net.ispapi.apiconnector;

import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for DefaultResponse Class
 */
public class DefaultResponseTest {
    /**
     * Test Template Getter method
     */
    @Test
    public void testTemplateGetter() {
        HashResponse r = new HashResponse(DefaultResponse.get("expired"));
        assertEquals(530, r.code());
        assertEquals("SESSION NOT FOUND", r.description());
    }

    /**
     * Test Template Getter method (parsed response)
     */
    @Test
    public void testTemplateGetterParsed() {
        Map<String, Object> r = DefaultResponse.getParsed("expired");
        assertEquals("530", r.get("CODE"));
        assertEquals("SESSION NOT FOUND", r.get("DESCRIPTION"));
    }

    /**
     * Test Template Setter method
     */
    @Test
    public void testTemplateSetter() {
        DefaultResponse.set("unauthorized", "[RESPONSE]\r\ncode=530\r\ndescription=Unauthorized\r\nTRANSLATIONKEY=FAPI.530.UNAUTHORIZED\r\nEOF\r\n");
        HashResponse r = new HashResponse(DefaultResponse.get("unauthorized"));
        assertEquals(530, r.code());
        assertEquals("Unauthorized", r.description());

        DefaultResponse.set("unauthorized2", DefaultResponse.getParsed("unauthorized"));
        r = new HashResponse(DefaultResponse.get("unauthorized"));
        assertEquals(530, r.code());
        assertEquals("Unauthorized", r.description());
    }

    /**
     * Test template container strictly
     */
    @Test
    public void testTemplateContainer() {
        Map<String, String> tpls = DefaultResponse.getAll();
        assertEquals(4, tpls.size());
        assertTrue(tpls.keySet().contains("empty"));
        assertTrue(tpls.keySet().contains("error"));
        assertTrue(tpls.keySet().contains("expired"));
        assertTrue(tpls.keySet().contains("commonerror"));
    }

    /**
     * Test template match method
     */
    @Test
    public void testTemplateMatch() {
        Map<String, Object> r1 = DefaultResponse.getParsed("unauthorized");
        assertTrue(DefaultResponse.match(r1, "unauthorized"));

        String r2 = DefaultResponse.get("unauthorized");
        assertTrue(DefaultResponse.match(r2, "unauthorized"));
    }
}
