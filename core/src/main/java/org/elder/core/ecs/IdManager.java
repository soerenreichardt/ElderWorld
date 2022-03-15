package org.elder.core.ecs;

import org.eclipse.collections.impl.stack.mutable.primitive.IntArrayStack;

public class IdManager {

    private static final IdManager INSTANCE = new IdManager();

    public static IdManager getInstance() {
        return INSTANCE;
    }

    private final IntArrayStack freeIds;
    private int maxEntityId;

    private IdManager() {
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
}
