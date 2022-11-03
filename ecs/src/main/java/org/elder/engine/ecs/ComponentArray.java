package org.elder.engine.ecs;

import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;

import java.util.ArrayList;
import java.util.Iterator;

public class ComponentArray implements Iterable<Component> {

    public static final int EMPTY_ELEMENT = -1;

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
        return internalId == EMPTY_ELEMENT
                ? null
                : components.get(internalId);
    }

    public void remove(int id) {
        var internalId = idMapping.get(id);
        components.set(internalId, null);
        idMapping.set(id, EMPTY_ELEMENT);
    }

    public int size() {
        return idManager.maxEntityId();
    }

    public boolean contains(int id) {
        return idMapping.size() > id && idMapping.get(id) != EMPTY_ELEMENT;
    }

    @Override
    public Iterator<Component> iterator() {
        return new NullFilteringListIterator<>(components.iterator());
    }

    private static void ensureSize(MutableIntList list, int targetSize) {
        for (int i = list.size(); i < targetSize; i++) {
            list.add(EMPTY_ELEMENT);
        }
    }
}
