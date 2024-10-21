package com.centralnicreseller.apiconnector;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for class SocketConfig
 */
public class SocketConfigTest {
    /**
     * Test initial value of getPOSTData of a freshly instantiated SocketConfig
     */
    @Test
    public void getPOSTData() {
        String d = new SocketConfig().getPOSTData();
        assertEquals("", d);
    }
}
