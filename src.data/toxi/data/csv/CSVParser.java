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

import java.util.ArrayList;

import toxi.util.events.EventDispatcher;

/**
 * An event-based CSV data parser with support for field aliases. Parse events
 * are generated for single rows and total completion (or failures). This allows
 * the parsing to function asynchronously in its own thread.
 */
public class CSVParser {

    protected String lines[];
    protected int currLineIndex;

    protected char quoteChar = '"';
    protected char separator = ',';

    protected CSVFieldMapper mapper;

    protected final EventDispatcher<CSVListener> dispatcher = new EventDispatcher<CSVListener>();

    public CSVParser(CSVFieldMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * @return the dispatcher
     */
    public EventDispatcher<CSVListener> getDispatcher() {
        return dispatcher;
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

    /**
     * Parses the given String array of CSV text lines and emits parse events
     * for clients to process individual rows. The first row (1st array index)
     * is assumed to contain the fieldnames, just as it would be the case with a
     * standard CSV file.
     * 
     * @param lines
     *            array of CSV formatted lines of text
     */
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
                fieldArray[j] = fieldArray[j].substring(0,
                        fieldArray[j].length() - 1);
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