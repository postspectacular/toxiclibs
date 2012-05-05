package toxi.geom;

import java.util.List;

public interface SpatialIndex<T> {

    public void clear();

    public int index(T p);

    public boolean isIndexed(T item);

    public List<T> itemsWithinRadius(T p, float radius);

    public int reindex(T p, T q);

    public int size();

    public int unindex(T p);

}