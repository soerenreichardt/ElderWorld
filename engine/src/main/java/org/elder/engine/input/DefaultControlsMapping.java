package org.elder.engine.input;

import static org.lwjgl.glfw.GLFW.*;

public class DefaultControlsMapping implements ControlsInputMapping {

    @Override
    public int moveLeft() {
        return GLFW_KEY_A;
    }

    @Override
    public int moveRight() {
        return GLFW_KEY_D;
    }

    @Override
    public int moveUp() {
        return GLFW_KEY_W;
    }

    @Override
    public int moveDown() {
        return GLFW_KEY_S;
    }
}
