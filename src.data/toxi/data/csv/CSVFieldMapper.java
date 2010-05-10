package toxi.data.csv;

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

    protected HashMap<String, String> fieldNames;
    protected HashMap<String, Integer> fieldOrder;

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
}