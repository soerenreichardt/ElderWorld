package org.elder.core;

import org.elder.core.ecs.Component;
import org.elder.core.ecs.ComponentRegistry;
import org.elder.core.ecs.GameComponent;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class GameRunner {

    private int width;
    private int height;

    public GameRunner(int width, int height) {
        this.width = width;
        this.height = height;

        registerComponents();
    }

    public void start() throws InterruptedException {
        var window = new Window(width, height);
        window.initialize();

        var game = new Game(window::shouldClose, window.getId(), width, height);
        game.start();
        windowLoop(window);

        game.join();

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
        var componentRegistry = ComponentRegistry.getInstance();
        new Reflections("org.elder", Scanners.SubTypes.filterResultsBy(__ -> true))
                .getSubTypesOf(Object.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(GameComponent.class))
                .forEach(clazz -> componentRegistry.registerComponent((Class<? extends Component>) clazz));
    }
}
