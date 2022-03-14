package org.elder.core;

import org.elder.core.ecs.Component;
import org.elder.core.ecs.ComponentManager;
import org.elder.core.ecs.GameComponent;
import org.lwjgl.opengl.GL;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

public class Game extends Thread {

    public Game() {
        registerComponents();
    }

    @Override
    public void run() {
        var window = new Window(800, 600);
        window.initialize();

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glfwSwapBuffers(window.getId()); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    private void registerComponents() {
        var componentManager = ComponentManager.getInstance();
        new Reflections("org.elder", new SubTypesScanner(false))
                .getSubTypesOf(Object.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(GameComponent.class))
                .forEach(clazz -> componentManager.registerComponent((Class<? extends Component>) clazz));
    }
}
