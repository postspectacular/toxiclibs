package toxi.util.datatypes;

import java.util.List;

public interface ItemIndex<T> {

    public void clear();

    public T forID(int id);

    public int getID(T item);

    public List<T> getItems();

    public int index(T item);

    public boolean isIndexed(T item);

    public int reindex(T item, T newItem);

    public int size();

    public int unindex(T item);

}