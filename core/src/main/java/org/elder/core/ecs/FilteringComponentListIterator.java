package org.elder.core.ecs;

import java.util.Iterator;

public class FilteringComponentListIterator<C extends Component> implements Iterator<C> {

    private final Iterator<C> componentListIterator;
    C nextItem;

    public FilteringComponentListIterator(Iterator<C> componentListIterator) {
        this.componentListIterator = componentListIterator;
    }

    @Override
    public boolean hasNext() {
        while (componentListIterator.hasNext()) {
            nextItem = componentListIterator.next();
            if (nextItem != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public C next() {
        return nextItem;
    }
}
