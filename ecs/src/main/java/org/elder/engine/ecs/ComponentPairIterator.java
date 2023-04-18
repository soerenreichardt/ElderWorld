package org.elder.engine.ecs;

import java.util.Iterator;

public class ComponentPairIterator<T1 extends Component, T2 extends Component> implements Iterator<ComponentManager.ComponentPair<T1, T2>> {

    private final ComponentArray componentArray1;
    private final ComponentArray componentArray2;
    private final int length;

    private int counter;
    private final ComponentManager.ComponentPair<T1, T2> resultCache;

    public ComponentPairIterator(ComponentArray componentArray1, ComponentArray componentArray2) {
        this.componentArray1 = componentArray1;
        this.componentArray2 = componentArray2;
        this.length = Math.min(componentArray1.size(), componentArray1.size());
        this.counter = -1;
        this.resultCache = new ComponentManager.ComponentPair<>();
    }

    @Override
    public boolean hasNext() {
        while (counter < length) {
            counter++;
            if (componentArray1.contains(counter) && componentArray2.contains(counter)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ComponentManager.ComponentPair<T1, T2> next() {
        resultCache.component1 = (T1) componentArray1.get(counter);
        resultCache.component2 = (T2) componentArray2.get(counter);

        return resultCache;
    }
}
