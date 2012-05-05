package toxi.geom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import toxi.math.MathUtils;

public class SpatialBins<T> implements SpatialIndex<T> {

    private final float invBinWidth;
    private final float minOffset;
    private final int numBins;
    private int numItems;

    private final List<HashSet<T>> bins;
    private final CoordinateExtractor<T> extractor;

    public SpatialBins(float min, float max, int numBins,
            CoordinateExtractor<T> extractor) {
        this.extractor = extractor;
        this.bins = new ArrayList<HashSet<T>>();
        for (int i = 0; i < numBins; i++) {
            bins.add(new HashSet<T>());
        }
        this.minOffset = min;
        this.numBins = numBins;
        this.invBinWidth = numBins / (max - min);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#clear()
     */
    public void clear() {
        for (HashSet<T> bin : bins) {
            bin.clear();
        }
        numItems = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#index(T)
     */
    public int index(T p) {
        int id = (int) MathUtils.clip((extractor.coordinate(p) - minOffset)
                * invBinWidth, 0, numBins - 1);
        if (bins.get(id).add(p)) {
            numItems++;
        }
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#isIndexed(T)
     */
    public boolean isIndexed(T item) {
        // TODO Auto-generated method stub
        return false;
    }

    public List<T> itemsWithinRadius(T p, float radius) {
        int id = (int) MathUtils.clip((extractor.coordinate(p) - minOffset)
                * invBinWidth, 0, numBins);
        int tol = (int) Math.ceil(radius * invBinWidth);
        List<T> items = null;
        for (int i = Math.max(id - tol, 0), n = Math.min(
                Math.min(id + tol, numBins), numBins - 1); i <= n; i++) {
            if (items == null) {
                items = new ArrayList<T>();
            }
            items.addAll(bins.get(i));
        }
        return items;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#reindex(float, T)
     */
    public int reindex(T p, T q) {
        int id1 = (int) MathUtils.clip((extractor.coordinate(p) - minOffset)
                * invBinWidth, 0, numBins);
        int id2 = (int) MathUtils.clip((extractor.coordinate(q) - minOffset)
                * invBinWidth, 0, numBins);
        if (id1 != id2) {
            if (bins.get(id1).remove(p)) {
                numItems--;
            }
            if (bins.get(id2).add(q)) {
                numItems++;
            }
            return id2;
        }
        return id1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#size()
     */
    public int size() {
        return numItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#unindex(T)
     */
    public int unindex(T p) {
        int id = (int) MathUtils.clip((extractor.coordinate(p) - minOffset)
                * invBinWidth, 0, numBins);
        bins.get(id).remove(p);
        return id;
    }
}
