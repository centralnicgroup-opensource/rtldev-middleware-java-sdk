package com.centralnicreseller.apiconnector;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for class Column
 */
public class ColumnTest {
    /**
     * Test getKey method
     */
    @Test
    public void getKey() {
        ArrayList<String> list = new ArrayList<>();
        list.add("mydomain1.com");
        list.add("mydomain2.com");
        list.add("mydomain3.com");
        Column col = new Column("DOMAIN", list);
        assertEquals("DOMAIN", col.getKey());
    }
}
