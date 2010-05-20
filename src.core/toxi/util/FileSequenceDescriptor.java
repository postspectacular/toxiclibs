package toxi.util;

import java.io.File;
import java.util.Iterator;

/**
 * A descriptor and iterator for handling file sequences.
 * 
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
     *            file pattern in the format: e.g. "basename%d04.ext"
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
     * Creates an iterator providing paths for each file in the sequence. The
     * iterator does not support the remove() method and attempts to use it
     * results in an {@link UnsupportedOperationException} being thrown.
     */
    public Iterator<String> iterator() {
        return new SequenceIterator(start, getFinalIndex());
    }
}
