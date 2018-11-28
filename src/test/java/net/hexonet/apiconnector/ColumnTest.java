package net.hexonet.apiconnector;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
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
        ArrayList<String> list = new ArrayList<String>();
        list.add("mydomain1.com");
        list.add("mydomain2.com");
        list.add("mydomain3.com");
        Column col = new Column("DOMAIN", list);
        assertEquals("DOMAIN", col.getKey());
    }
}
