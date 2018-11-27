package net.hexonet.apiconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
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
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        ResponseTemplate tpl = rtm.getTemplate("IwontExist");
        assertEquals(500, tpl.getCode());
        assertEquals("Response Template not found", tpl.getDescription());
    }

    /**
     * Test getTemplates method
     */
    @Test
    public void getTemplates() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        ArrayList<String> defaultones = new ArrayList<String>(Arrays.asList("404", "500", "empty",
                "error", "expired", "httperror", "unauthorized"));
        Map<String, ResponseTemplate> tpls = rtm.getTemplates();
        for (String key : defaultones) {
            assertTrue(tpls.containsKey(key));
        }
    }

    /**
     * Test isTemplateMatchHash method
     */
    @Test
    public void isTemplateMatchHash() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        ResponseTemplate tpl = new ResponseTemplate("");
        assertTrue(rtm.isTemplateMatchHash(tpl.getHash(), "empty"));
    }

    /**
     * Test isTemplateMatchPlain method
     */
    @Test
    public void isTemplateMatchPlain() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        ResponseTemplate tpl = new ResponseTemplate("");
        assertTrue(rtm.isTemplateMatchPlain(tpl.getPlain(), "empty"));
    }
}
