package com.centralnicreseller.apiconnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for class ResponseTemplateManager
 */
public class ResponseTemplateManagerTest {
    /**
     * Test getTemplate method
     */
    @Test
    public void getTemplate() {
        Response tpl = ResponseTemplateManager.getTemplate("IwontExist");
        assertEquals(500, tpl.getCode());
        assertEquals("Response Template not found", tpl.getDescription());
    }

    /**
     * Test getTemplates method
     */
    @Test
    public void getTemplates() {
        ArrayList<String> defaultones = new ArrayList<>(Arrays.asList("404", "500", "empty",
                "error", "expired", "httperror", "invalid", "unauthorized"));
        Map<String, Response> tpls = ResponseTemplateManager.getTemplates();
        for (String key : defaultones) {
            assertTrue(tpls.containsKey(key));
        }
    }
}
