package com.centralnicreseller.apiconnector;

import java.util.HashMap;
import java.util.Map;

/**
 * Record covers Row Data in a better accessible way
 *
 * @author Kai Schwarz
 * @version %I%, %G%
 * @since 2.0
 */
public class Record {
    /** row data container */
    private final Map<String, String> data;

    /**
     * Class constructor
     *
     * @param data row data as associative array
     */
    public Record(Map<String, String> data) {
        this.data = new HashMap<>(data);
    }

    /**
     * Get full row data
     *
     * @return full row data
     */
    public Map<String, String> getData() {
        return this.data;
    }

    /**
     * Get data for given golumn name
     *
     * @param key column name
     * @return data for given column name
     */
    public String getDataByKey(String key) {
        if (this.hasData(key)) {
            return this.data.get(key);
        }
        return null;
    }

    /**
     * Check if column data exists
     *
     * @param key column name
     * @return boolean check result
     */
    private boolean hasData(String key) {
        return this.data.containsKey(key);
    }
}
