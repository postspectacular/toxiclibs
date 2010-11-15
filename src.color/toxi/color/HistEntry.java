package toxi.color;

/**
 * A single histogram entry, a coupling of color & frequency. Implements a
 * comparator to sort histogram entries based on freq.
 */
public class HistEntry implements Comparable<HistEntry> {

    protected float freq;
    protected TColor col;

    HistEntry(TColor c) {
        col = c;
        freq = 1;
    }

    public int compareTo(HistEntry e) {
        return (int) (e.freq - freq);
    }

    public TColor getColor() {
        return col;
    }

    public float getFrequency() {
        return freq;
    }
}