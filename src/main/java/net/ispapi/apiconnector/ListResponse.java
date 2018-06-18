package net.ispapi.apiconnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ListIterator;

/**
 * ListResponse class provides extra functionality to work with API responses.
 * It provides methods that are useful for data representation in table format.
 * In general the apiconnector Client always returns this type of response to be
 * as flexible as possible.
 *  
 * @author      Kai Schwarz
 * @version     %I%, %G%
 * @since 1.0
 */
public class ListResponse extends HashResponse {
  /** represents the list iterator */
  private ListIterator<ArrayList<String>> it = null;
  /** represents the list of rows of the api response */
  private ArrayList<ArrayList<String>> rows;

  /**
   * Class constructor.
   * @param p_r raw api response
   */
  public ListResponse(String p_r) {
    super(p_r);
    rows = new ArrayList<ArrayList<String>>();
    Map<String, Object> h = hash();
    ArrayList<?> cols = getColumnKeys();
    if (isSuccess()) {
      Map<?, ?> properties = (HashMap<?, ?>) h.get("PROPERTY");
      for (int i = 0; i < count(); i++) {
        ArrayList<String> row = new ArrayList<String>();
        for (int c = 0; c < cols.size(); c++) {
          String colkey = (String) cols.get(c);
          ArrayList<?> values = (ArrayList<?>) properties.get(colkey);
          if (values != null) {
            row.add((String) values.get(i));
          }
        }
        rows.add(row);
      }
    }
    rewind();
  }

  /**
   * method to return the list of available rows
   * @return list of rows
   */
  public ArrayList<ArrayList<String>> list() {
    return rows;
  }

  /**
   * quick access method to iterator's hasNext method
   * @return hasNext result
   */
  public boolean hasNext() {
    return this.it.hasNext();
  }

  /**
   * quick access method to iterator's next method
   * @return next result
   */
  public ArrayList<String> next() {
    return this.it.next();
  }

  /**
   * quick access method to iterator's hasPrevious method
   * @return hasPrevious result
   */
  public boolean hasPrevious() {
    return this.it.hasPrevious();
  }

  /**
   * quick access method to iterator's previous method
   * @return previous result
   */
  public ArrayList<String> previous() {
    return this.it.previous();
  }


  /**
   * method to get current row
   * @return current row
   */
  public ArrayList<String> current() {
    if (this.it.hasNext()) {
      this.it.next();
      return this.it.previous();
    } else {
      if (this.it.hasPrevious()) {
        this.it.previous();
        return this.it.next();
      }
    }
    return null;
  }

  /**
   * method to reset the iterator
   */
  public void rewind() {
    it = rows.listIterator();
  }
}