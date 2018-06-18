package net.ispapi.apiconnector;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for apiconnector ListResponse class
 */
public class ListResponseTest {
    /**
     * Test list method
     */
    @Test
    public void testList() {
        String raw = "[RESPONSE]\r\nPROPERTY[TOTAL][0]=100\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[LAST][0]=99\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LIMIT][0]=2\r\nPROPERTY[CREATEDDATE][0]=2016-06-07 18:02:02\r\nPROPERTY[CREATEDDATE][1]=2008-03-18 09:37:25\r\nPROPERTY[FINALIZATIONDATE][0]=2017-06-08 18:02:02\r\nPROPERTY[FINALIZATIONDATE][1]=2017-03-19 09:37:25\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n";
        ListResponse r = new ListResponse(raw);
        ArrayList<ArrayList<String>> list = r.list();
        assertEquals(2, list.size());
        for (int i=0; i<list.size(); i++){
            ArrayList<String> entry = list.get(i);
            assertEquals(r.getColumnKeys().size(), entry.size());
        }
    }

    /**
     * Test iterator quick access methods
     */
    @Test
    public void testIterator() {
        String raw = "[RESPONSE]\r\nPROPERTY[TOTAL][0]=100\r\nPROPERTY[FIRST][0]=0\r\nPROPERTY[LAST][0]=99\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[LIMIT][0]=2\r\nPROPERTY[CREATEDDATE][0]=2016-06-07 18:02:02\r\nPROPERTY[CREATEDDATE][1]=2008-03-18 09:37:25\r\nPROPERTY[FINALIZATIONDATE][0]=2017-06-08 18:02:02\r\nPROPERTY[FINALIZATIONDATE][1]=2017-03-19 09:37:25\r\nDESCRIPTION=Command completed successfully\r\nCODE=200\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n";
        ListResponse r = new ListResponse(raw);
        r.current();
        assertEquals(true, r.hasNext());
        assertEquals(false, r.hasPrevious());
        int i=0;
        while(r.hasNext()){
            r.next();
            i++;
        }
        assertEquals(2, i);
        assertEquals(false, r.hasNext());
        assertEquals(true, r.hasPrevious());
        r.rewind();
        assertEquals(true, r.hasNext());
        assertEquals(false, r.hasPrevious());
        while(r.hasNext()){
            r.next();
        }
        while(r.hasPrevious()){
            r.previous();
            i--;
        }
        assertEquals(0, i);
        assertEquals(true, r.hasNext());
        assertEquals(false, r.hasPrevious());
    }
}