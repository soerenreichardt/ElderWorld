package org.elder.engine.ecs;

import java.util.Iterator;

public class NullFilteringListIterator<T> implements Iterator<T> {

    private final Iterator<T> listIterator;
    T nextItem;

    public NullFilteringListIterator(Iterator<T> listIterator) {
        this.listIterator = listIterator;
    }

    @Override
    public boolean hasNext() {
        while (listIterator.hasNext()) {
            nextItem = listIterator.next();
            if (nextItem != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T next() {
        return nextItem;
    }
}
