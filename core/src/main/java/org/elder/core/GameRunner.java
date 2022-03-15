package org.elder.core;

import org.elder.core.ecs.Component;
import org.elder.core.ecs.ComponentManager;
import org.elder.core.ecs.GameComponent;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class GameRunner {

    public GameRunner() {
        registerComponents();
    }

    public void start() throws InterruptedException {
        var window = new Window(800, 600);
        window.initialize();

        var updateAndRenderThread = new Game(window::shouldClose, window.getId());
        updateAndRenderThread.start();
        windowLoop(window);

        updateAndRenderThread.join();

        glfwFreeCallbacks(window.getId());
        glfwDestroyWindow(window.getId());
        glfwTerminate();
    }

    private void windowLoop(Window window) {
        while (!window.shouldClose()) {

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
