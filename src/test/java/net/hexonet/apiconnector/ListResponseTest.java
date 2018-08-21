package net.hexonet.apiconnector;

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
     * Test API Response with different column size
     * e.g. QueryAccountingList with LIMIT > 1
     */
    @Test
    public void testListDifferentColLength() {
        String raw = "[RESPONSE]\r\nCODE=200\r\nDESCRIPTION=Command completed successfully\r\nPROPERTY[FIRST][0]=30850\r\nPROPERTY[LAST][0]=30851\r\nPROPERTY[COUNT][0]=2\r\nPROPERTY[TOTAL][0]=30852\r\nPROPERTY[LIMIT][0]=2\r\nPROPERTY[ACCOUNTINGAMOUNT][0]=1\r\nPROPERTY[ACCOUNTINGAMOUNT][1]=1\r\nPROPERTY[ACCOUNTINGCURRENCY][0]=eur\r\nPROPERTY[ACCOUNTINGCURRENCY][1]=eur\r\nPROPERTY[ACCOUNTINGDATE][0]=2018-08-20 13:48:04\r\nPROPERTY[ACCOUNTINGDATE][1]=2018-08-21 12:48:03\r\nPROPERTY[ACCOUNTINGDESCRIPTION][0]=anthon234yyyy.com\r\nPROPERTY[ACCOUNTINGDESCRIPTION][1]=0000000000001.com\r\nPROPERTY[ACCOUNTINGINVOICEID][0]=-\r\nPROPERTY[ACCOUNTINGINVOICEID][1]=-\r\nPROPERTY[ACCOUNTINGPRICE][0]=0.00\r\nPROPERTY[ACCOUNTINGPRICE][1]=0.00\r\nPROPERTY[ACCOUNTINGREFERENCE][0]=\r\nPROPERTY[ACCOUNTINGREFERENCE][1]=\r\nPROPERTY[ACCOUNTINGTYPE][0]=TRADE_DOMAIN_FAILED\r\nPROPERTY[ACCOUNTINGTYPE][1]=TRADE_DOMAIN_FAILED\r\nPROPERTY[ACCOUNTINGVAT][0]=19.00\r\nPROPERTY[ACCOUNTINGVAT][1]=19.00\r\nPROPERTY[ACCOUNTINGVATPRICE][0]=0.00\r\nPROPERTY[ACCOUNTINGVATPRICE][1]=0.00\r\nPROPERTY[OPENINGBALANCE][0]=136476.95\r\nPROPERTY[OPENINGBALANCEPRICE][0]=112568.55\r\nPROPERTY[OPENINGBALANCEVATPRICE][0]=23908.40\r\nPROPERTY[SUM][0]=136476.95\r\nPROPERTY[SUMPRICE][0]=112568.55\r\nPROPERTY[SUMVATPRICE][0]=23908.40\r\nQUEUETIME=0\r\nRUNTIME=0.12\r\nEOF\r\n";
        ListResponse r = new ListResponse(raw);
        ArrayList<ArrayList<String>> list = r.list();
        assertEquals(2, list.size());
        String tmp = r.getColumnIndex("SUM", 1);
        assertEquals(null, tmp);
        tmp = r.getColumnIndex("ACCOUNTINGVATPRICE", 0);
        assertEquals("0.00", tmp);
        tmp = r.getColumnIndex("ACCOUNTINGVATPRICE", 1);
        assertEquals("0.00", tmp);
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