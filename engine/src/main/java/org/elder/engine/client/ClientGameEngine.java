package org.elder.engine.client;

import org.elder.engine.Scene;
import org.elder.engine.api.GameEngine;
import org.elder.engine.api.GameExecutable;
import org.elder.engine.ecs.api.Resource;
import org.elder.engine.ecs.system.SystemManager;
import org.elder.engine.ecs.system.SystemManagerBuilder;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class ClientGameEngine extends GameEngine<Scene> {

    private final Window window;
    private final long windowId;

    public ClientGameEngine(
            Window window,
            GameExecutable<Scene> gameExecutable,
            Resource... resources
    ) {
        super(gameExecutable, resources);
        this.window = window;
        this.windowId = window.getId();
    }

    @Override
    protected final SystemManager initializeSystemManager(SystemManagerBuilder systemManagerBuilder, Resource[] resources) {
        return systemManagerBuilder
                .fromReflection()
                .withResources(resources)
                .withPackagePrefix("org.elder")
                .build();
    }

    @Override
    protected void initialize() {
        initializeOpenGL();
    }

    @Override
    protected void preUpdate() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }

    @Override
    protected void update(float delta) {
        glfwSwapBuffers(windowId); // swap the color buffers
    }

    @Override
    protected boolean interruptGameLoop() {
        return window.shouldClose();
    }

    @Override
    public void setScene(Scene scene) {
        if (activeScene != scene) {
            activeScene = scene;
            scene.camera().initializeProjectionMatrix(window.width(), window.height());
            systemManager.onSceneChanged(scene);
        }
    }

    private void initializeOpenGL() {
        glfwMakeContextCurrent(windowId);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glEnable(GL_CULL_FACE);

        glViewport(0, 0, window.width(), window.height());

        glfwSwapInterval(1);
    }
}
