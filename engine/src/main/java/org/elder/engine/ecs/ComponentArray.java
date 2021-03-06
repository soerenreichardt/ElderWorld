package org.elder.engine.ecs;

import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;

import java.util.ArrayList;
import java.util.Iterator;

public class ComponentArray implements Iterable<Component> {

    private final IdManager idManager;
    private final IntArrayList idMapping;
    private final ArrayList<Component> components;

    public ComponentArray() {
        this.idManager = new IdManager();
        this.idMapping = new IntArrayList();
        this.components = new ArrayList<>();
    }

    public void addComponent(int id, Component component) {
        var internalId = idManager.getNewId();

        ensureSize(idMapping, id + 1);
        idMapping.set(id, internalId);

        if (components.size() == internalId) {
            components.add(component);
        } else {
            components.set(internalId, component);
        }
    }

    public Component get(int id) {
        var internalId = idMapping.get(id);
        return components.get(internalId);
    }

    public void remove(int id) {
        var internalId = idMapping.get(id);
        components.set(internalId, null);
    }

    @Override
    public Iterator<Component> iterator() {
        return new NullFilteringListIterator<>(components.iterator());
    }

    private static void ensureSize(MutableIntList list, int targetSize) {
        for (int i = list.size(); i < targetSize; i++) {
            list.add(-1);
        }
    }
}
