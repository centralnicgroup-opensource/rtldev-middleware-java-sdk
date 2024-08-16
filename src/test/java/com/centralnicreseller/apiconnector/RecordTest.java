package com.centralnicreseller.apiconnector;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import com.centralnicreseller.apiconnector.Record;

/**
 * Unit test for class Record
 */
public class RecordTest {
    /**
     * Test getData method
     */
    @Test
    public void getData() {
        Map<String, String> data = new HashMap<>();
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
        Map<String, String> data = new HashMap<>();
        data.put("DOMAIN", "mydomain.com");
        data.put("RATING", "1");
        data.put("RNDINT", "321");
        data.put("SUM", "1");
        Record rec = new Record(data);
        assertNull(rec.getDataByKey("KEYNOTEXISTING"));
    }
}
