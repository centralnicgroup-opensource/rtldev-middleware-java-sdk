package net.hexonet.apiconnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Response covers all functionality to wrap a Backend API Response like accessing data
 * 
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public class Response extends ResponseTemplate {
    /**
     * The API Command used within this request
     */
    private Map<String, String> command;
    /**
     * Column names available in this responsse NOTE: this includes also FIRST, LAST, LIMIT, COUNT,
     * TOTAL and maybe further specific columns in case of a list query
     */
    private ArrayList<String> columnkeys;
    /**
     * Container of Column Instances
     */
    private ArrayList<Column> columns;
    /**
     * Record Index we currently point to in record list
     */
    private int recordIndex;
    /**
     * Record List (List of rows)
     */
    private ArrayList<Record> records;

    /**
     * Constructor
     * 
     * @param raw API plain response
     * @param cmd API command used within this request
     */
    @SuppressWarnings("unchecked") // not really a good way ...
    public Response(String raw, Map<String, String> cmd) {
        super(raw);
        this.command = new HashMap<String, String>(cmd);
        this.columnkeys = new ArrayList<String>();
        this.columns = new ArrayList<Column>();
        this.recordIndex = 0;
        this.records = new ArrayList<Record>();

        Object property = this.hash.get("PROPERTY");
        if (property != null) {
            Map<String, ArrayList<String>> p = (HashMap<String, ArrayList<String>>) property;
            Iterator<Map.Entry<String, ArrayList<String>>> it = p.entrySet().iterator();
            int count = 0;
            while (it.hasNext()) {
                Map.Entry<String, ArrayList<String>> pair = it.next();
                this.addColumn(pair.getKey(), pair.getValue());
                int len = pair.getValue().size();
                if (len > count) {
                    count = len;
                }
            }
            for (int i = 0; i < count; i++) {
                Map<String, String> d = new HashMap<String, String>();
                for (String key : this.columnkeys) {
                    Column col = this.getColumn(key);
                    if (col != null) {
                        String v = col.getDataByIndex(i);
                        if (v != null) {
                            d.put(key, v);
                        }
                    }
                }
                this.addRecord(d);
            }
        }
    }

    /**
     * Add a column to the column list
     * 
     * @param key  column name
     * @param data array of column data
     * @return Current Response Instance for method chaining
     */
    public Response addColumn(String key, ArrayList<String> data) {
        Column col = new Column(key, data);
        this.columns.add(col);
        this.columnkeys.add(key);
        return this;
    }

    /**
     * Add a record to the record list
     * 
     * @param h row data
     * @return Current Response Instance for method chaining
     */
    public Response addRecord(Map<String, String> h) {
        this.records.add(new Record(h));
        return this;
    }

    /**
     * Get column by column name
     * 
     * @param key column name
     * @return column instance or null if column does not exist
     */
    public Column getColumn(String key) {
        if (this.hasColumn(key)) {
            return this.columns.get(this.columnkeys.indexOf(key));
        }
        return null;
    }

    /**
     * Get Data by Column Name and Index
     * 
     * @param colkey column name
     * @param index  column data index
     * @return column data at index or null if not found
     */
    public String getColumnIndex(String colkey, int index) {
        Column col = this.getColumn(colkey);
        if (col != null) {
            return col.getDataByIndex(index);
        }
        return null;
    }

    /**
     * Get Column Names
     * 
     * @return Array of Column Names
     */
    public ArrayList<String> getColumnKeys() {
        return this.columnkeys;
    }

    /**
     * Get List of Columns
     * 
     * @return Array of Columns
     */
    public ArrayList<Column> getColumns() {
        return this.columns;
    }

    /**
     * Get Command used in this request
     * 
     * @return command
     */
    public Map<String, String> getCommand() {
        return this.command;
    }

    /**
     * Get Page Number of current List Query
     * 
     * @return page number or -1 in case of a non-list response
     */
    public int getCurrentPageNumber() {
        int first = this.getFirstRecordIndex();
        int limit = this.getRecordsLimitation();
        if ((first != -1) && (limit > 0)) {
            return (int) Math.floor((double) first / (double) limit) + 1;
        }
        return -1;
    }

    /**
     * Get Record of current record index
     * 
     * @return Record or null in case of a non-list response
     */
    public Record getCurrentRecord() {
        if (this.hasCurrentRecord()) {
            return this.records.get(this.recordIndex);
        }
        return null;
    }

    /**
     * Get Index of first row in this response
     * 
     * @return first row index; -1 if record list is empty
     */
    public int getFirstRecordIndex() {
        Column col = this.getColumn("FIRST");
        if (col != null) {
            String f = col.getDataByIndex(0);
            if (f != null) {
                return Integer.parseInt(f);
            }
        }
        if (this.records.size() > 0) {
            return 0;
        }
        return -1;
    }

    /**
     * Get last record index of the current list query
     * 
     * @return record index or -1 for a non-list response
     */
    public int getLastRecordIndex() {
        Column col = this.getColumn("LAST");
        if (col != null) {
            String l = col.getDataByIndex(0);
            if (l != null) {
                return Integer.parseInt(l);
            }
        }
        int len = this.getRecordsCount();
        if (len > 0) {
            return (len - 1);
        }
        return -1;
    }

    /**
     * Get Response as List Hash including useful meta data for tables
     * 
     * @return hash including list meta data and array of rows in hash notation
     */
    public Map<String, Object> getListHash() {
        ArrayList<Map<String, String>> lh = new ArrayList<Map<String, String>>();
        ArrayList<Record> recs = this.getRecords();
        for (Record rec : recs) {
            lh.add(rec.getData());
        }
        HashMap<String, Object> hm = new HashMap<String, Object>();
        HashMap<String, Object> me = new HashMap<String, Object>();
        me.put("columns", this.getColumnKeys());
        me.put("pg", this.getPagination());
        hm.put("LIST", lh);
        hm.put("meta", me);
        return hm;
    }

    /**
     * Get next record in record list
     * 
     * @return Record or null in case there's no further record
     */
    public Record getNextRecord() {
        if (this.hasNextRecord()) {
            return this.records.get(++this.recordIndex);
        }
        return null;
    }

    /**
     * Get Page Number of next list query
     * 
     * @return page number or -1 if there's no next page
     */
    public int getNextPageNumber() {
        int cp = this.getCurrentPageNumber();
        if (cp == -1) {
            return -1;
        }
        int page = cp + 1;
        int pages = this.getNumberOfPages();
        return (page <= pages ? page : pages);
    }

    /**
     * Get the number of pages available for this list query
     * 
     * @return number of pages
     */
    public int getNumberOfPages() {
        int t = this.getRecordsTotalCount();
        int limit = this.getRecordsLimitation();
        if (t > 0 && limit > 0) {
            return (int) Math.ceil((double) t / (double) this.getRecordsLimitation());
        }
        return 0;
    }

    /**
     * Get object containing all paging data
     * 
     * @return paginator data
     */
    public Map<String, Object> getPagination() {
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("COUNT", this.getRecordsCount());
        mp.put("CURRENTPAGE", this.getCurrentPageNumber());
        mp.put("FIRST", this.getFirstRecordIndex());
        mp.put("LAST", this.getLastRecordIndex());
        mp.put("LIMIT", this.getRecordsLimitation());
        mp.put("NEXTPAGE", this.getNextPageNumber());
        mp.put("PAGES", this.getNumberOfPages());
        mp.put("PREVIOUSPAGE", this.getPreviousPageNumber());
        mp.put("TOTAL", this.getRecordsTotalCount());
        return mp;
    }

    /**
     * Get Page Number of previous list query
     * 
     * @return page number or -1 if there's no previous page
     */
    public int getPreviousPageNumber() {
        int cp = this.getCurrentPageNumber();
        if (cp == -1) {
            return -1;
        }
        int pp = cp - 1;
        if (pp > 0) {
            return pp;
        }
        return -1;
    }

    /**
     * Get previous record in record list
     * 
     * @return Record or null if there's no previous record
     */
    public Record getPreviousRecord() {
        if (this.hasPreviousRecord()) {
            return this.records.get(--this.recordIndex);
        }
        return null;
    }

    /**
     * Get Record at given index
     * 
     * @param idx record index
     * @return Record or null if index does not exist
     */
    public Record getRecord(int idx) {
        if (idx >= 0 && this.records.size() > idx) {
            return this.records.get(idx);
        }
        return null;
    }

    /**
     * Get all Records
     * 
     * @return list of records
     */
    public ArrayList<Record> getRecords() {
        return this.records;
    }

    /**
     * Get count of rows in this response
     * 
     * @return count of rows
     */
    public int getRecordsCount() {
        return this.records.size();
    }

    /**
     * Get total count of records available for the list query
     * 
     * @return total count of records or count of records for a non-list response
     */
    public int getRecordsTotalCount() {
        Column col = this.getColumn("TOTAL");
        if (col != null) {
            String t = col.getDataByIndex(0);
            if (t != null) {
                return Integer.parseInt(t);
            }
        }
        return this.getRecordsCount();
    }

    /**
     * Get limit(ation) setting of the current list query This is the count of requested rows
     * 
     * @return limit setting or count requested rows
     */
    public int getRecordsLimitation() {
        Column col = this.getColumn("LIMIT");
        if (col != null) {
            String l = col.getDataByIndex(0);
            if (l != null) {
                return Integer.parseInt(l);
            }
        }
        return this.getRecordsCount();
    }

    /**
     * Check if this list query has a next page
     * 
     * @return boolean result
     */
    public boolean hasNextPage() {
        int cp = this.getCurrentPageNumber();
        if (cp == -1) {
            return false;
        }
        return (cp + 1 <= this.getNumberOfPages());
    }

    /**
     * Check if this list query has a previous page
     * 
     * @return boolean result
     */
    public boolean hasPreviousPage() {
        int cp = this.getCurrentPageNumber();
        if (cp == -1) {
            return false;
        }
        return ((cp - 1) > 0);
    }

    /**
     * Reset index in record list back to zero
     * 
     * @return Current Response Instance for method chaining
     */
    public Response rewindRecordList() {
        this.recordIndex = 0;
        return this;
    }

    /**
     * Check if column exists in response
     * 
     * @param key column name
     * @return boolean result
     */
    private boolean hasColumn(String key) {
        return (this.columnkeys.indexOf(key) != -1);
    }

    /**
     * Check if the record list contains a record for the current record index in use
     * 
     * @return boolean result
     */
    private boolean hasCurrentRecord() {
        int len = this.records.size();
        return (len > 0 && this.recordIndex >= 0 && this.recordIndex < len);
    }

    /**
     * Check if the record list contains a next record for the current record index in use
     * 
     * @return boolean result
     */
    private boolean hasNextRecord() {
        int next = this.recordIndex + 1;
        return (this.hasCurrentRecord() && (next < this.records.size()));
    }

    /**
     * Check if the record list contains a previous record for the current record index in use
     * 
     * @return boolean result
     */
    private boolean hasPreviousRecord() {
        return (this.recordIndex > 0 && this.hasCurrentRecord());
    }
}
