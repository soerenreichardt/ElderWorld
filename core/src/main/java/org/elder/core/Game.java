package org.elder.core;

import org.lwjgl.opengl.GL;

import java.util.function.BooleanSupplier;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

public class Game extends Thread {

    private final BooleanSupplier shouldCloseFn;
    private final long window;

    public Game(BooleanSupplier shouldCloseFn, long window) {
        this.shouldCloseFn = shouldCloseFn;
        this.window = window;
    }

    @Override
    public void run() {
        glfwMakeContextCurrent(window);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();


        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        var lastTime = System.nanoTime();
        while (!shouldCloseFn.getAsBoolean()) {
            var currentTime = System.nanoTime();
            float dt = (currentTime - lastTime) * 1E-9f;

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glfwSwapBuffers(window); // swap the color buffers
        }
    }
}
