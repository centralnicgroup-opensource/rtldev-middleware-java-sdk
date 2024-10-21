package com.centralnicreseller.apiconnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.centralnicreseller.apiconnector.Record;

/**
 * Unit test for class Response
 */
public class ResponseTest {
    static {
        ResponseTemplateManager.addTemplate("listP0",
                "[RESPONSE]\r\nproperty[total][0] = 4\r\nproperty[first][0] = 0\r\nproperty[domain][0] = cnic-ssl-test1.com\r\nproperty[domain][1] = cnic-ssl-test2.com\r\nproperty[count][0] = 2\r\nproperty[last][0] = 1\r\nproperty[limit][0] = 2\r\ndescription = Command completed successfully\r\ncode = 200\r\nqueuetime = 0\r\nruntime = 0.007\r\nEOF\r\n");
        ResponseTemplateManager.addTemplate("OK", "200", "Command completed successfully");
    }

    /**
     * Test getHash method
     */
    @Test
    public void getHash() {
        Response tpl = new Response("");
        Map<String, Object> h = tpl.getHash();
        assertEquals("423", (String) h.get("CODE"));
        assertEquals("Empty API response. Probably unreachable API end point",
                (String) h.get("DESCRIPTION"));
    }

    /**
     * Test getQueuetime method #1
     */
    @Test
    public void getQueuetime1() {
        Response tpl = new Response("");
        assertEquals(0, tpl.getQueuetime(), 0);
    }

    /**
     * Test getQueuetime method #2
     */
    @Test
    public void getQueuetime2() {
        Response tpl = new Response(
                "[RESPONSE]\r\ncode=423\r\ndescription=Empty API response. Probably unreachable API end point\r\nqueuetime=0\r\nEOF\r\n");
        assertEquals(0, tpl.getQueuetime(), 0);
    }

    /**
     * Test getRuntime method #1
     */
    @Test
    public void getRuntime1() {
        Response tpl = new Response("");
        assertEquals(0, tpl.getRuntime(), 0);
    }

    /**
     * Test getRuntime method #2
     */
    @Test
    public void getRuntime2() {
        Response tpl = new Response(
                "[RESPONSE]\r\ncode=423\r\ndescription=Empty API response. Probably unreachable API end point\r\nruntime=0.12\r\nEOF\r\n");
        assertEquals(0.12, tpl.getRuntime(), 0);
    }

    /**
     * Test isPending method
     */
    @Test
    public void isPending() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "AddDomain");
        cmd.put("DOMAIN", "example.com");
        Response tpl = new Response(
                "[RESPONSE]\r\ncode = 200\r\ndescription = Command completed successfully\r\nruntime = 0.44\r\nqueuetime = 0\r\n\r\nproperty[status][0] = REQUESTED\r\nproperty[updated date][0] = 2023-05-22 12:14:31.0\r\nproperty[zone][0] = se\r\nEOF\r\n",
                cmd);
        assertEquals(true, tpl.isPending());
    }

    /**
     * Test isPending method #2
     */
    @Test
    public void isPending2() {
        Response tpl = new Response("");
        assertEquals(false, tpl.isPending());
    }

    /**
     * Test getCommandPlain method
     */
    @Test
    public void getCommandPlain() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainOptions");
        cmd.put("DOMAIN0", "example.com");
        cmd.put("DOMAIN1", "example.net");
        Response r = new Response("", cmd);
        String str = "COMMAND = QueryDomainOptions\nDOMAIN0 = example.com\nDOMAIN1 = example.net\n";
        assertEquals(str, r.getCommandPlain());
    }

    /**
     * Test getCommandPlain method: secured passwords
     */
    @Test
    public void getCommandPlainSecure() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "CheckAuthentication");
        cmd.put("USER", System.getenv("CNR_TEST_USER"));
        cmd.put("PASSWORD", System.getenv("CNR_TEST_PASSWORD"));
        Response r = new Response("", cmd);
        String str = "COMMAND = CheckAuthentication\nPASSWORD = ***\nUSER = " + System.getenv("CNR_TEST_USER") + "\n";
        assertEquals(str, r.getCommandPlain());
    }

    /**
     * Test getCurrentPageNumber method #1
     */
    @Test
    public void getCurrentPageNumber1() {

        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        assertEquals(1, r.getCurrentPageNumber());
    }

    /**
     * Test getCurrentPageNumber method #2
     */
    @Test
    public void getCurrentPageNumber2() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("OK", cmd);
        assertEquals(-1, r.getCurrentPageNumber());
    }

    /**
     * Test getFirstRecordIndex method #1
     */
    @Test
    public void getFirstRecordIndex1() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("OK", cmd);
        assertEquals(-1, r.getFirstRecordIndex());
    }

    /**
     * Test getColumns method
     */
    @Test
    public void getColumns() {

        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        ArrayList<Column> cols = r.getColumns();
        assertEquals(6, cols.size());
    }

    /**
     * Test getColumnIndex method #1
     */
    @Test
    public void getColumnIndex1() {

        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        String data = r.getColumnIndex("DOMAIN", 0);
        assertEquals("cnic-ssl-test1.com", data);
    }

    /**
     * Test getColumnIndex method #2
     */
    @Test
    public void getColumnIndex2() {

        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        String data = r.getColumnIndex("COLUMN_NOT_EXISTS", 0);
        assertNull(data);
    }

    /**
     * Test getColumnKeys method
     */
    @Test
    public void getColumnKeys() {

        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        ArrayList<String> colkeys = r.getColumnKeys();
        assertEquals(6, colkeys.size());
        assertTrue(colkeys
                .containsAll(Arrays.asList("COUNT", "DOMAIN", "FIRST", "LAST", "LIMIT", "TOTAL")));
    }

    /**
     * Test getCurrentRecord method #1
     */
    @Test
    public void getCurrentRecord1() {

        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        Record rec = r.getCurrentRecord();
        Map<String, String> m = new HashMap<>();
        m.put("COUNT", "2");
        m.put("DOMAIN", "cnic-ssl-test1.com");
        m.put("FIRST", "0");
        m.put("LAST", "1");
        m.put("LIMIT", "2");
        m.put("TOTAL", "4");
        assertEquals(m, rec.getData());
    }

    /**
     * Test getCurrentRecord method #2
     */
    @Test
    public void getCurrentRecord2() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "StatusAccount");
        Response r = new Response("OK", cmd);
        assertNull(r.getCurrentRecord());
    }

    /**
     * Test getListHash method
     */
    @Test
    @SuppressWarnings("unchecked")
    public void getListHash() {

        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        Map<String, Object> lh = r.getListHash();
        ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) lh.get("LIST");
        assertEquals(2, list.size());
        Map<String, Object> meta = (HashMap<String, Object>) lh.get("meta");
        ArrayList<String> columns = (ArrayList<String>) meta.get("columns");
        assertEquals(r.getColumnKeys(), columns);
        Map<String, Object> pg = (Map<String, Object>) meta.get("pg");
        assertEquals(r.getPagination(), pg);
    }

    /**
     * Test getNextRecord method
     */
    @Test
    public void getNextRecord() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        Record rec = r.getNextRecord();
        Map<String, String> hm = new HashMap<>();
        hm.put("DOMAIN", "cnic-ssl-test2.com");
        assertEquals(hm, rec.getData());
        rec = r.getNextRecord();
        assertNull(rec);
    }

    /**
     * Test getPagination method
     */
    @Test
    public void getPagination() {

        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        Map<String, Object> pager = r.getPagination();
        assertTrue(pager.containsKey("COUNT"));
        assertTrue(pager.containsKey("CURRENTPAGE"));
        assertTrue(pager.containsKey("FIRST"));
        assertTrue(pager.containsKey("LAST"));
        assertTrue(pager.containsKey("LIMIT"));
        assertTrue(pager.containsKey("NEXTPAGE"));
        assertTrue(pager.containsKey("PAGES"));
        assertTrue(pager.containsKey("PREVIOUSPAGE"));
        assertTrue(pager.containsKey("TOTAL"));
    }

    /**
     * Test getPreviousRecord method
     */
    @Test
    public void getPreviousRecord() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        r.getNextRecord();
        Map<String, String> hm = new HashMap<>();
        hm.put("COUNT", "2");
        hm.put("DOMAIN", "cnic-ssl-test1.com");
        hm.put("FIRST", "0");
        hm.put("LAST", "1");
        hm.put("LIMIT", "2");
        hm.put("TOTAL", "4");
        assertEquals(hm, r.getPreviousRecord().getData());
        assertNull(r.getPreviousRecord());
    }

    /**
     * Test hasNextPage method #1
     */
    @Test
    public void hasNextPage1() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("OK", cmd);
        assertFalse(r.hasNextPage());
    }

    /**
     * Test hasNextPage method #2
     */
    @Test
    public void hasNextPage2() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        assertTrue(r.hasNextPage());
    }

    /**
     * Test hasPreviousPage method #1
     */
    @Test
    public void hasPreviousPage1() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("OK", cmd);
        assertFalse(r.hasPreviousPage());
    }

    /**
     * Test hasPreviousPage method #2
     */
    @Test
    public void hasPreviousPage2() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        assertFalse(r.hasPreviousPage());
    }

    /**
     * Test getLastRecordIndex method #1
     */
    @Test
    public void getLastRecordIndex1() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("OK", cmd);
        assertEquals(-1, r.getLastRecordIndex());
    }

    /**
     * Test getNextPageNumber method #1
     */
    @Test
    public void getNextPageNumber1() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("OK", cmd);
        assertEquals(-1, r.getNextPageNumber());
    }

    /**
     * Test getNextPageNumber method #2
     */
    @Test
    public void getNextPageNumber2() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        assertEquals(2, r.getNextPageNumber());
    }

    /**
     * Test getNumberOfPages method
     */
    @Test
    public void getNumberOfPages() {

        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("OK", cmd);
        assertEquals(0, r.getNumberOfPages());
    }

    /**
     * Test getPreviousPageNumber method #1
     */
    @Test
    public void getPreviousPageNumber1() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("OK", cmd);
        assertEquals(-1, r.getPreviousPageNumber());
    }

    /**
     * Test getPreviousPageNumber method #2
     */
    @Test
    public void getPreviousPageNumber2() {
        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        assertEquals(-1, r.getPreviousPageNumber());
    }

    /**
     * Test rewindRecordList method
     */
    @Test
    public void rewindRecordList() {

        Map<String, String> cmd = new HashMap<>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response("listP0", cmd);
        assertNull(r.getPreviousRecord());
        assertNotNull(r.getNextRecord());
        assertNull(r.getNextRecord());
        r.rewindRecordList();
        assertNull(r.getPreviousRecord());
    }
}
