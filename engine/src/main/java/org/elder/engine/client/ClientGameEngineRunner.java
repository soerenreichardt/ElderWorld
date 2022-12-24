package org.elder.engine.client;

import org.elder.engine.api.GameEngineRunner;
import org.elder.engine.api.GameExecutable;
import org.elder.engine.ecs.Component;
import org.elder.engine.ecs.ComponentRegistry;
import org.elder.engine.ecs.api.GameComponent;
import org.elder.engine.input.KeyInputResource;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class ClientGameEngineRunner implements GameEngineRunner {

    private int width;
    private int height;

    private final KeyInputResource keyInputResource;

    public ClientGameEngineRunner(int width, int height) {
        this.width = width;
        this.height = height;

        this.keyInputResource = new KeyInputResource();

        registerComponents();
    }

    @Override
    public void start(GameExecutable<?> gameExecutable) throws InterruptedException {
        var window = new Window(width, height);
        window.initialize();

        glfwSetKeyCallback(window.getId(), keyInputResource);

        var game = new ClientGameEngine(window, gameExecutable, keyInputResource);
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
                .getSubTypesOf(Component.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(GameComponent.class))
                .forEach(componentRegistry::registerComponent);
    }
}
