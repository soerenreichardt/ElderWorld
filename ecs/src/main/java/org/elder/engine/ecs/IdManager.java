package org.elder.engine.ecs;

import org.eclipse.collections.impl.stack.mutable.primitive.IntArrayStack;

public class IdManager {

    private final IntArrayStack freeIds;
    private int maxEntityId;

    public IdManager() {
        this.freeIds = new IntArrayStack();
        this.maxEntityId = 0;
    }

    public int getNewId() {
        if (freeIds.isEmpty()) {
            return maxEntityId++;
        }

        return freeIds.pop();
    }

    public void removeId(int id) {
        freeIds.push(id);
    }

    public int maxEntityId() {
        return maxEntityId;
    }
}
