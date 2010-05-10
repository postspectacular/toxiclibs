package toxi.data.csv;

import java.util.ArrayList;

import toxi.util.events.EventDispatcher;

public class CSVParser {

    protected String lines[];
    protected int currLineIndex;

    protected char quoteChar = '"';
    protected char separator = ',';

    protected CSVFieldMapper mapper;

    protected EventDispatcher<CSVListener> dispatcher =
            new EventDispatcher<CSVListener>();

    public CSVParser(CSVFieldMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * @return the mapper
     */
    public CSVFieldMapper getMapper() {
        return mapper;
    }

    protected final String getNextLine() {
        currLineIndex++;
        if (currLineIndex < lines.length) {
            return lines[currLineIndex];
        } else {
            return null;
        }
    }

    public void parse(String[] lines) {
        this.lines = lines;
        this.currLineIndex = -1;
        if (mapper.setColumnOrder(parseLine(getNextLine()))) {
            while (currLineIndex < lines.length - 1) {
                String[] fields = parseLine(getNextLine());
                if (fields.length >= mapper.getMappedFieldCount()) {
                    for (CSVListener l : dispatcher) {
                        l.csvNewItemParsed(fields, mapper);
                    }
                } else if (fields.length > 1) {
                    for (CSVListener l : dispatcher) {
                        l.csvNewItemFailure(fields, mapper);
                    }
                }
            }
            for (CSVListener l : dispatcher) {
                l.csvParseSuccess(this);
            }
        } else {
            for (CSVListener l : dispatcher) {
                l.csvParseFailure(this);
            }
        }
    }

    protected final String[] parseLine(String s) {
        if (s == null) {
            return null;
        }
        ArrayList<String> fields = new ArrayList<String>();
        StringBuffer currField = new StringBuffer();
        boolean isOpen = false;
        do {
            if (isOpen) {
                currField.append("\n");
                s = getNextLine();
                if (s == null) {
                    break;
                }
            }
            for (int i = 0, slen = s.length(); i < slen; i++) {
                char c = s.charAt(i);
                if (c == quoteChar) {
                    if (isOpen && slen > i + 1 && s.charAt(i + 1) == quoteChar) {
                        currField.append(s.charAt(i + 1));
                        i++;
                    } else {
                        isOpen ^= true;
                        if (i > 2 && s.charAt(i - 1) != separator
                                && slen > i + 1 && s.charAt(i + 1) != separator) {
                            currField.append(c);
                        }
                    }
                } else if (c == separator && !isOpen) {
                    fields.add(currField.toString());
                    currField = new StringBuffer();
                } else {
                    currField.append(c);
                }
            }

        } while (isOpen);
        fields.add(currField.toString());
        String fieldArray[] = fields.toArray(new String[0]);
        for (int j = 0; j < fieldArray.length; j++) {
            fieldArray[j] = fieldArray[j].trim();
            if (fieldArray[j].endsWith("\"")) {
                fieldArray[j] = fieldArray[j].substring(0, fieldArray[j].length() - 1);
            }
        }
        return fieldArray;
    }

    /**
     * @param mapper
     *            the mapper to set
     */
    public void setMapper(CSVFieldMapper mapper) {
        this.mapper = mapper;
    }
}