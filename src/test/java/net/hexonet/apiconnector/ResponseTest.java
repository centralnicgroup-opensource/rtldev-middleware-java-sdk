package net.hexonet.apiconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Unit test for class Response
 */
public class ResponseTest {

    /**
     * Test getCommandPlain method
     */
    @Test
    public void getCommandPlain() {
        Map<String, String> cmd = new HashMap<String, String>();
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
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "CheckAuthentication");
        cmd.put("SUBUSER", "test.user");
        cmd.put("PASSWORD", "test.passw0rd");
        Response r = new Response("", cmd);
        String str = "SUBUSER = test.user\nCOMMAND = CheckAuthentication\nPASSWORD = ***\n";
        assertEquals(str, r.getCommandPlain());
    }

    /**
     * Test getCurrentPageNumber method #1
     */
    @Test
    public void getCurrentPageNumber1() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        assertEquals(1, r.getCurrentPageNumber());
    }

    /**
     * Test getCurrentPageNumber method #2
     */
    @Test
    public void getCurrentPageNumber2() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("OK").getPlain(), cmd);
        assertEquals(-1, r.getCurrentPageNumber());
    }

    /**
     * Test getFirstRecordIndex method #1
     */
    @Test
    public void getFirstRecordIndex1() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("OK").getPlain(), cmd);
        assertEquals(-1, r.getFirstRecordIndex());
    }

    /**
     * Test getFirstRecordIndex method #2
     */
    @Test
    public void getFirstRecordIndex2() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));

        Map<String, Object> h = rtm.getTemplate("OK").getHash();
        Map<String, ArrayList<String>> hm = new HashMap<String, ArrayList<String>>();
        hm.put("DOMAIN", new ArrayList<String>(Arrays.asList("mydomain1.com", "mydomain2.com")));
        h.put("PROPERTY", hm);
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(ResponseParser.serialize(h), cmd);
        assertEquals(0, r.getFirstRecordIndex());
    }

    /**
     * Test getColumns method
     */
    @Test
    public void getColumns() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        ArrayList<Column> cols = r.getColumns();
        assertEquals(6, cols.size());
    }

    /**
     * Test getColumnIndex method #1
     */
    @Test
    public void getColumnIndex1() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        String data = r.getColumnIndex("DOMAIN", 0);
        assertEquals("0-60motorcycletimes.com", data);
    }

    /**
     * Test getColumnIndex method #2
     */
    @Test
    public void getColumnIndex2() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        String data = r.getColumnIndex("COLUMN_NOT_EXISTS", 0);
        assertNull(data);
    }

    /**
     * Test getColumnKeys method
     */
    @Test
    public void getColumnKeys() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
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
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        Record rec = r.getCurrentRecord();
        Map<String, String> m = new HashMap<String, String>();
        m.put("COUNT", "2");
        m.put("DOMAIN", "0-60motorcycletimes.com");
        m.put("FIRST", "0");
        m.put("LAST", "1");
        m.put("LIMIT", "2");
        m.put("TOTAL", "2701");
        assertEquals(m, rec.getData());
    }

    /**
     * Test getCurrentRecord method #2
     */
    @Test
    public void getCurrentRecord2() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "StatusAccount");
        Response r = new Response(rtm.getTemplate("OK").getPlain(), cmd);
        assertNull(r.getCurrentRecord());
    }

    /**
     * Test getListHash method
     */
    @Test
    @SuppressWarnings("unchecked")
    public void getListHash() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
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
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        Record rec = r.getNextRecord();
        Map<String, String> hm = new HashMap<String, String>();
        hm.put("DOMAIN", "0-be-s01-0.com");
        assertEquals(hm, rec.getData());
        rec = r.getNextRecord();
        assertNull(rec);
    }

    /**
     * Test getPagination method
     */
    @Test
    public void getPagination() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
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
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");

        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        r.getNextRecord();
        Map<String, String> hm = new HashMap<String, String>();
        hm.put("COUNT", "2");
        hm.put("DOMAIN", "0-60motorcycletimes.com");
        hm.put("FIRST", "0");
        hm.put("LAST", "1");
        hm.put("LIMIT", "2");
        hm.put("TOTAL", "2701");
        assertEquals(hm, r.getPreviousRecord().getData());
        assertNull(r.getPreviousRecord());
    }

    /**
     * Test hasNextPage method #1
     */
    @Test
    public void hasNextPage1() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("OK").getPlain(), cmd);
        assertFalse(r.hasNextPage());
    }

    /**
     * Test hasNextPage method #2
     */
    @Test
    public void hasNextPage2() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        assertTrue(r.hasNextPage());
    }

    /**
     * Test hasPreviousPage method #1
     */
    @Test
    public void hasPreviousPage1() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("OK").getPlain(), cmd);
        assertFalse(r.hasPreviousPage());
    }

    /**
     * Test hasPreviousPage method #2
     */
    @Test
    public void hasPreviousPage2() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        assertFalse(r.hasPreviousPage());
    }

    /**
     * Test getLastRecordIndex method #1
     */
    @Test
    public void getLastRecordIndex1() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("OK").getPlain(), cmd);
        assertEquals(-1, r.getLastRecordIndex());
    }

    /**
     * Test getLastRecordIndex method #2
     */
    @Test
    public void getLastRecordIndex2() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        ResponseTemplate tpl =
                new ResponseTemplate(rtm.generateTemplate("200", "Command completed successfully"));
        Map<String, Object> h = tpl.getHash();
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("DOMAIN", new ArrayList<String>(Arrays.asList("mydomain1.com", "mydomain2.com")));
        h.put("PROPERTY", hm);
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(ResponseParser.serialize(h), cmd);
        assertEquals(1, r.getLastRecordIndex());
    }

    /**
     * Test getNextPageNumber method #1
     */
    @Test
    public void getNextPageNumber1() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("OK").getPlain(), cmd);
        assertEquals(-1, r.getNextPageNumber());
    }

    /**
     * Test getNextPageNumber method #2
     */
    @Test
    public void getNextPageNumber2() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        assertEquals(2, r.getNextPageNumber());
    }

    /**
     * Test getNumberOfPages method
     */
    @Test
    public void getNumberOfPages() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("OK").getPlain(), cmd);
        assertEquals(0, r.getNumberOfPages());
    }

    /**
     * Test getPreviousPageNumber method #1
     */
    @Test
    public void getPreviousPageNumber1() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("OK", rtm.generateTemplate("200", "Command completed successfully"));
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("OK").getPlain(), cmd);
        assertEquals(-1, r.getPreviousPageNumber());
    }

    /**
     * Test getPreviousPageNumber method #2
     */
    @Test
    public void getPreviousPageNumber2() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        assertEquals(-1, r.getPreviousPageNumber());
    }

    /**
     * Test rewindRecordList method
     */
    @Test
    public void rewindRecordList() {
        ResponseTemplateManager rtm = ResponseTemplateManager.getInstance();
        rtm.addTemplate("listP0",
                "[RESPONSE]\r\nPROPERTY[TOTAL][0]=2701\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[DOMAIN][0]=0-60motorcycletimes.com\r\nPROPERTY[DOMAIN][1]=0-be-s01-0.com\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LAST][0]=1\r\nPROPERTY[LIMIT][0]=2\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.023\r\nEOF\r\n");
        Map<String, String> cmd = new HashMap<String, String>();
        cmd.put("COMMAND", "QueryDomainList");
        Response r = new Response(rtm.getTemplate("listP0").getPlain(), cmd);
        assertNull(r.getPreviousRecord());
        assertNotNull(r.getNextRecord());
        assertNull(r.getNextRecord());
        r.rewindRecordList();
        assertNull(r.getPreviousRecord());
    }
}
