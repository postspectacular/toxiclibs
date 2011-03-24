/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.data.csv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * This class allows the user to refer to CSV fields/columns via freely chosen
 * IDs. This is useful in cases where the actual CSV column name is very long or
 * the order of fields can be varying (e.g. when consuming CSV data from
 * different sources). The class constructor expects a {@link HashMap} to
 * establish the mapping between the custom keys and the actual CSV field names.
 * The following example demonstrates this usage:
 * 
 * <pre>
 * HashMap&lt;String,String&gt; keys=new HashMap&lt;String,String&gt;();
 * 
 * // creates a new alias "name" for the actual CSV field name: "What is your name?"
 * keys.put("name","What is your name?");
 * keys.put("phone","What is your phone number?");
 * keys.put("tue","Times available on Tuesday");
 * 
 * keys.put("id","Actual CSV field name");
 * 
 * CSVFieldMapper mapper=new CSVFieldMapper(keys);
 * ...
 * </pre>
 * 
 * This mapper instance is then passed to a {@link CSVParser} and used to help
 * parsing &amp; validating the data. When a row has been successfully parsed,
 * the {@link CSVParser} emits an event to allow clients to further process this
 * data, e.g. by bundling it into custom data types:
 * 
 * <pre>
 * public void csvNewItemParsed(String[] fields, CSVFieldMapper map) {
 *   Person p = new Person();
 *   // use the mapper to get the field for the custom field ID "name"
 *   p.setName(map.get("name",fields));
 *   p.setPhone(map.get("phone",fields));
 *   ...
 * }
 * </pre>
 */
public class CSVFieldMapper {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    protected HashMap<String, String> fieldNames;
    protected HashMap<String, Integer> fieldOrder;

    protected DateFormat dateFormat = DEFAULT_DATE_FORMAT;

    public CSVFieldMapper(HashMap<String, String> cols) {
        fieldNames = cols;
        fieldOrder = new HashMap<String, Integer>();
    }

    /**
     * Looks up the value for CSV column mapped to the given ID.
     * 
     * @param id
     * @param fields
     * @return value or null, if not matched.
     */
    public String get(String id, String[] fields) {
        String value = null;
        if (fieldNames.containsKey(id)) {
            int i = fieldOrder.get(fieldNames.get(id));
            if (i < fields.length) {
                value = fields[i];
            }
        }
        return value;
    }

    public Date getDate(String id, String[] fields) {
        try {
            return dateFormat.parse(get(id, fields));
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * @return the dateFormat
     */
    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public float getFloat(String id, String[] fields, float defaultValue) {
        try {
            return Float.parseFloat(get(id, fields));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getInt(String id, String[] fields, int defaultValue) {
        try {
            return Integer.parseInt(get(id, fields));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getMappedFieldCount() {
        return fieldNames.size();
    }

    public boolean setColumnOrder(String[] columnTitles) {
        fieldOrder.clear();
        for (String s : fieldNames.values()) {
            boolean flag = false;
            for (int j = 0; j < columnTitles.length; j++) {
                if (s.equalsIgnoreCase(columnTitles[j])) {
                    fieldOrder.put(s, j);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param dateFormat
     *            the dateFormat to set
     */
    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
}