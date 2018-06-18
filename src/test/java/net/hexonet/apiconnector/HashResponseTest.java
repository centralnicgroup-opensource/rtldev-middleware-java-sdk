package net.hexonet.apiconnector;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for apiconnector HashResponse class
 */
public class HashResponseTest {
    /**
     * Test Session Login / Logout and reuse of API Session ID
     */
    @Test
    public void testRaw() {
        String raw1 = "[RESPONSE]\r\nPROPERTY[TOTAL][0]=100\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[LAST][0]=99\r\nPROPERTY[COUNT][0]=1\r\nPROPERTY[CREATEDDATE][0]=2016-06-07 18:02:02\r\nPROPERTY[CREATEDDATE][1]=2008-03-18 09:37:25\r\nPROPERTY[FINALIZATIONDATE][0]=2017-06-08 18:02:02\r\nPROPERTY[FINALIZATIONDATE][1]=2017-03-19 09:37:25\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n";
        String raw2 = "[RESPONSE]\r\nCODE=200\r\nDESCRIPTION=Command completed successfully\r\nRUNTIME=0.12\r\nQUEUETIME=0\r\nPROPERTY[CREATEDDATE][0]=2016-06-07 18:02:02\r\nPROPERTY[CREATEDDATE][1]=2008-03-18 09:37:25\r\nEOF\r\n";
        HashResponse r = new HashResponse(raw1);
        assertEquals(raw1, r.raw(true));
        r.useColumns("^CREATEDDATE$");
        assertEquals(raw2, r.raw());
        r.useColumnsReset();
        assertEquals(raw1, r.raw());
    }

    /**
     * Test parse method
     */
    @Test
    public void testHash() {
        String raw = "[RESPONSE]\r\nCODE=200\r\nDESCRIPTION=Command completed successfully\r\nRUNTIME=0.12\r\nQUEUETIME=0\r\nPROPERTY[CREATEDDATE][0]=2016-06-07 18:02:02\r\nPROPERTY[CREATEDDATE][1]=2008-03-18 09:37:25\r\nPROPERTY[FINALIZATIONDATE][0]=2017-06-08 18:02:02\r\nPROPERTY[FINALIZATIONDATE][1]=2017-03-19 09:37:25\r\nEOF\r\n";
        HashResponse r = new HashResponse(raw);
        // check parsed unfiltered result
        Map<String, Object> h = r.hash();
        assertEquals("200", h.get("CODE"));
        assertEquals("0", h.get("QUEUETIME"));
        assertEquals("0.12", h.get("RUNTIME"));
        assertEquals("Command completed successfully", h.get("DESCRIPTION"));
        Map<?, ?> properties = (HashMap<?, ?>) h.get("PROPERTY");
        assertNotNull(properties);
        ArrayList<?> col = (ArrayList<?>) properties.get("CREATEDDATE");
        assertNotNull(col);
        assertTrue(col.size() == 2);
        assertEquals("2016-06-07 18:02:02", col.get(0));
        assertEquals("2008-03-18 09:37:25", col.get(1));
        col = (ArrayList<?>) properties.get("FINALIZATIONDATE");
        assertNotNull(col);
        assertTrue(col.size() == 2);
        assertEquals("2017-06-08 18:02:02", col.get(0));
        assertEquals("2017-03-19 09:37:25", col.get(1));
        // filter columns
        r.useColumns("^CREATEDDATE$");
        h = r.hash();
        assertEquals("200", h.get("CODE"));
        assertEquals("0", h.get("QUEUETIME"));
        assertEquals("0.12", h.get("RUNTIME"));
        assertEquals("Command completed successfully", h.get("DESCRIPTION"));
        properties = (HashMap<?, ?>) h.get("PROPERTY");
        assertNotNull(properties);
        col = (ArrayList<?>) properties.get("CREATEDDATE");
        assertNotNull(col);
        assertTrue(col.size() == 2);
        assertEquals("2016-06-07 18:02:02", col.get(0));
        assertEquals("2008-03-18 09:37:25", col.get(1));
        col = (ArrayList<?>) properties.get("FINALIZATIONDATE");
        assertEquals(null, col);
        // reset column filter
        r.useColumnsReset();
        h = r.hash();
        assertEquals("200", h.get("CODE"));
        assertEquals("0", h.get("QUEUETIME"));
        assertEquals("0.12", h.get("RUNTIME"));
        assertEquals("Command completed successfully", h.get("DESCRIPTION"));
        properties = (HashMap<?, ?>) h.get("PROPERTY");
        assertNotNull(properties);
        col = (ArrayList<?>) properties.get("CREATEDDATE");
        assertNotNull(col);
        assertTrue(col.size() == 2);
        assertEquals("2016-06-07 18:02:02", col.get(0));
        assertEquals("2008-03-18 09:37:25", col.get(1));
        col = (ArrayList<?>) properties.get("FINALIZATIONDATE");
        assertNotNull(col);
        assertTrue(col.size() == 2);
        assertEquals("2017-06-08 18:02:02", col.get(0));
        assertEquals("2017-03-19 09:37:25", col.get(1));
    }

    /**
     * Test pagination methods
     */
    @Test
    public void testPaginationMethods() {
        String raw = "[RESPONSE]\r\nPROPERTY[LIMIT][0]=2\r\nPROPERTY[TOTAL][0]=100\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[LAST][0]=99\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[CREATEDDATE][0]=2016-06-07 18:02:02\r\nPROPERTY[CREATEDDATE][1]=2008-03-18 09:37:25\r\nPROPERTY[FINALIZATIONDATE][0]=2017-06-08 18:02:02\r\nPROPERTY[FINALIZATIONDATE][1]=2017-03-19 09:37:25\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n";
        HashResponse r = new HashResponse(raw);
        assertEquals(200, r.code());
        assertEquals(0, r.first());
        assertEquals(100, r.total());
        assertEquals(99, r.last());
        assertEquals(2, r.limit());
        assertEquals(2, r.count());
        assertEquals("Command completed successfully", r.description());
        assertEquals(0.12, r.runtime(), 0.001);
        assertEquals(0.00, r.queuetime(), 0.001);
        assertEquals(50, r.pages());
        assertEquals(1, r.page());
        assertEquals(1, r.prevpage());
        assertEquals(2, r.nextpage());
        Map<String, Integer> pager = r.getPagination();
        assertEquals(0, (int) pager.get("FIRST"));
        assertEquals(100, (int) pager.get("TOTAL"));
        assertEquals(99, (int) pager.get("LAST"));
        assertEquals(2, (int) pager.get("LIMIT"));
        assertEquals(2, (int) pager.get("COUNT"));
        assertEquals(50, (int) pager.get("PAGES"));
        assertEquals(1, (int) pager.get("PAGE"));
        assertEquals(1, (int) pager.get("PAGEPREV"));
        assertEquals(2, (int) pager.get("PAGENEXT"));
    }

    /**
     * test response code check methods
     */
    @Test
    public void testCodeValidation() {
        HashResponse r = new HashResponse(
                "[RESPONSE]\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n");
        assertTrue(r.isSuccess());
        assertTrue(!r.isTmpError());
        assertTrue(!r.isError());
        r = new HashResponse(
                "[RESPONSE]\r\nDESCRIPTION=Command completed successfully\r\nCODE=421\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n");
        assertTrue(!r.isSuccess());
        assertTrue(r.isTmpError());
        assertTrue(!r.isError());
        r = new HashResponse(
                "[RESPONSE]\r\nDESCRIPTION=Command completed successfully\r\nCODE=500\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n");
        assertTrue(!r.isSuccess());
        assertTrue(!r.isTmpError());
        assertTrue(r.isError());
    }

    /**
     * test getColumnKeys method
     */
    @Test
    public void testGetColumnKeys() {
        String raw = "[RESPONSE]\r\nPROPERTY[TOTAL][0]=100\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[LAST][0]=99\r\nPROPERTY[COUNT][0]=1\r\nPROPERTY[CREATEDDATE][0]=2016-06-07 18:02:02\r\nPROPERTY[CREATEDDATE][1]=2008-03-18 09:37:25\r\nPROPERTY[FINALIZATIONDATE][0]=2017-06-08 18:02:02\r\nPROPERTY[FINALIZATIONDATE][1]=2017-03-19 09:37:25\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n";
        HashResponse r = new HashResponse(raw);
        ArrayList<?> colKeys = r.getColumnKeys();
        assertEquals(2, colKeys.size());
        assertEquals("CREATEDDATE", colKeys.get(0));
        assertEquals("FINALIZATIONDATE", colKeys.get(1));
    }

    /**
     * test getColumn method
     */
    @Test
    public void testGetColumn() {
        String raw = "[RESPONSE]\r\nPROPERTY[TOTAL][0]=100\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[LAST][0]=99\r\nPROPERTY[COUNT][0]=1\r\nPROPERTY[CREATEDDATE][0]=2016-06-07 18:02:02\r\nPROPERTY[CREATEDDATE][1]=2008-03-18 09:37:25\r\nPROPERTY[FINALIZATIONDATE][0]=2017-06-08 18:02:02\r\nPROPERTY[FINALIZATIONDATE][1]=2017-03-19 09:37:25\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n";
        HashResponse r = new HashResponse(raw);
        ArrayList<?> col = r.getColumn("CREATEDDATE");
        assertEquals(2, col.size());
        assertEquals("2016-06-07 18:02:02", col.get(0));
        assertEquals("2008-03-18 09:37:25", col.get(1));
        col = r.getColumn("IDONOTEXIST");
        assertEquals(null, col);
    }

    /**
     * test getColumnIndex method
     */
    @Test
    public void testGetColumnIndex() {
        String raw = "[RESPONSE]\r\nPROPERTY[TOTAL][0]=100\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[LAST][0]=99\r\nPROPERTY[COUNT][0]=1\r\nPROPERTY[CREATEDDATE][0]=2016-06-07 18:02:02\r\nPROPERTY[CREATEDDATE][1]=2008-03-18 09:37:25\r\nPROPERTY[FINALIZATIONDATE][0]=2017-06-08 18:02:02\r\nPROPERTY[FINALIZATIONDATE][1]=2017-03-19 09:37:25\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n";
        HashResponse r = new HashResponse(raw);
        String val = r.getColumnIndex("CREATEDDATE", 1);
        assertEquals("2008-03-18 09:37:25", val);
        val = r.getColumnIndex("CREATEDDATE", 2);// index n/a
        assertEquals(null, val);
    }

    // for now we leave tests for parse and serialize out as above methods won't work in case these methods break
}