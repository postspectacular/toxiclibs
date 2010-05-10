package toxi.util.events;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EventDispatcher<T> implements Iterable<T> {

    protected List<T> listeners = new LinkedList<T>();

    public EventDispatcher() {
    }

    public void addListener(T listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public List<T> getListeners() {
        return listeners;
    }

    public Iterator<T> iterator() {
        return listeners.iterator();
    }

    public void removeListener(T listener) {
        listeners.remove(listener);
    }
}
