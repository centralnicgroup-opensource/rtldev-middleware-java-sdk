package net.hexonet.apiconnector;

import java.util.ArrayList;

/**
 * Column covers Column Data in a better accessible way
 * 
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public class Column {
    /** column size */
    public int length;
    /** column name */
    private String key;
    /** column data container */
    private ArrayList<String> data;

    /**
     * Class constructor.
     */
    public Column(String key, ArrayList<String> data) {
        this.data = new ArrayList<String>(data);
        this.key = key;
        this.length = this.data.size();
    }

    /**
     * Get column name
     * 
     * @return column name
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Get column data
     *
     * @return column data
     */
    public ArrayList<String> getData() {
        return this.data;
    }

    /**
     * Get data for given column index
     * 
     * @return data for given column index
     */
    public String getDataByIndex(int idx) {
        if (this.hasDataIndex(idx)) {
            return this.data.get(idx);
        }
        return null;
    }

    /**
     * Check if column data index exists
     * 
     * @return boolean check result
     */
    private boolean hasDataIndex(int idx) {
        return (idx >= 0 && idx < this.length);
    }
}
