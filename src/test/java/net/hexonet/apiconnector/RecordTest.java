package net.hexonet.apiconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Unit test for class Record
 */
public class RecordTest {
    /**
     * Test getData method
     */
    @Test
    public void getData() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("DOMAIN", "mydomain.com");
        data.put("RATING", "1");
        data.put("RNDINT", "321");
        data.put("SUM", "1");
        Record rec = new Record(data);
        assertEquals(data, rec.getData());
    }

    /**
     * Test getDataByKey method
     */
    @Test
    public void getDataByKey() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("DOMAIN", "mydomain.com");
        data.put("RATING", "1");
        data.put("RNDINT", "321");
        data.put("SUM", "1");
        Record rec = new Record(data);
        assertNull(rec.getDataByKey("KEYNOTEXISTING"));
    }
}
