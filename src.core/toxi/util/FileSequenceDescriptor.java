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

package toxi.util;

import java.io.File;
import java.util.Iterator;

/**
 * A descriptor and iterator for handling file sequences.
 */
public class FileSequenceDescriptor implements Iterable<String> {

    private class SequenceIterator implements Iterator<String> {

        private int curr;
        private int end;

        public SequenceIterator(int start, int end) {
            this.curr = start;
            this.end = end;
        }

        public boolean hasNext() {
            return curr < end;
        }

        public String next() {
            String path = getPathForIndex(curr);
            curr++;
            return path;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove() not supported");
        }

    }

    public String filePattern;
    public String extension;
    public int numDigits;
    public int start;
    public int end = -1;

    /**
     * Creates a new descriptor from the given sequence details.
     * 
     * @param filePattern
     *            file pattern in the format: e.g. "path/basename%d04.ext"
     * @param extension
     *            file extension (e.g. ".tga")
     * @param numDigits
     *            number of digits used for the index
     * @param start
     *            start index
     */
    public FileSequenceDescriptor(String filePattern, String extension,
            int numDigits, int start) {
        this.filePattern = filePattern;
        this.extension = extension;
        this.numDigits = numDigits;
        this.start = start;
    }

    /**
     * Returns the base path of the sequence, i.e. the substring of the
     * sequence's file pattern from the beginning until the first occurence of
     * the % sign indicating the frame numbers.
     * 
     * @return path string
     */
    public String getBasePath() {
        return filePattern.substring(0, filePattern.indexOf('%'));
    }

    /**
     * Calculates sequence duration
     * 
     * @return number of files in sequence
     */
    public int getDuration() {
        return getFinalIndex() - start;
    }

    /**
     * Identifies the index of the last file of the sequence.
     * 
     * @return final index
     */
    public int getFinalIndex() {
        if (end == -1) {
            end = start;
            while (true) {
                if (!new File(getPathForIndex(end)).canRead()) {
                    break;
                } else {
                    end++;
                }
            }
        }
        return end;
    }

    /**
     * Constructs the file path for the given absolute index
     * 
     * @param i
     *            index
     * @return path
     */
    public String getPathForIndex(int i) {
        return String.format(filePattern, i);
    }

    /**
     * Returns the index of the first file of the sequence.
     * 
     * @return start index
     */
    public int getStartIndex() {
        return start;
    }

    /**
     * Creates an iterator providing paths for each file in the sequence. The
     * iterator does not support the remove() method and attempts to use it
     * results in an {@link UnsupportedOperationException} being thrown.
     */
    public Iterator<String> iterator() {
        return new SequenceIterator(start, getFinalIndex());
    }
}
