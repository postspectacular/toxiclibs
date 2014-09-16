package toxi.geom;

import java.util.List;

public interface SpatialIndex<T> {

    public void clear();

    public boolean index(T p);

    public boolean isIndexed(T item);

    public List<T> itemsWithinRadius(T p, float radius, List<T> results);

    public boolean reindex(T p, T q);

    public int size();

    public boolean unindex(T p);

}