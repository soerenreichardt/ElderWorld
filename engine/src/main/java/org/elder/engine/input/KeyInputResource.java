package org.elder.engine.input;

import org.elder.engine.Resource;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class KeyInputResource implements GLFWKeyCallbackI, Resource {

    private final boolean[] keydown = new boolean[GLFW_KEY_LAST + 1];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keydown[key] = action == GLFW_PRESS || action == GLFW_REPEAT;
    }

    public boolean keyDown(int key) {
        return this.keydown[key];
    }

    public boolean keyUp(int key) {
        return !keyDown(key);
    }
}
