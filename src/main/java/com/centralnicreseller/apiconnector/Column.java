package com.centralnicreseller.apiconnector;

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
    private final int length;
    /** column name */
    private final String key;
    /** column data container */
    private final ArrayList<String> data;

    /**
     * Class constructor.
     *
     * @param key  column name
     * @param data column data as list
     */
    public Column(String key, ArrayList<String> data) {
        this.data = new ArrayList<>(data);
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
     * @param idx column data index
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
     * @param idx column data index
     * @return boolean check result
     */
    private boolean hasDataIndex(int idx) {
        return (idx >= 0 && idx < this.length);
    }
}
