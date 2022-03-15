package org.elder.core;

import org.elder.core.ecs.Component;
import org.elder.core.ecs.ComponentManager;
import org.elder.core.ecs.GameComponent;
import org.elder.core.ecs.GameObject;
import org.lwjgl.opengl.GL;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

public class Game extends Thread {

    private final List<GameObject> gameObjects;

    public Game() {
        this.gameObjects = new ArrayList<>();
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
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        loop(window);
    }

    private void loop(Window window) {
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
        new Reflections("org.elder", Scanners.SubTypes.filterResultsBy(__ -> true))
                .getSubTypesOf(Object.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(GameComponent.class))
                .forEach(clazz -> componentManager.registerComponent((Class<? extends Component>) clazz));
    }
}
