package org.elder.engine;

import org.elder.geometry.Square;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameEngine extends Thread {

    private final Window window;
    private final long windowId;

    private final SystemManager systemManager;

    private Scene activeScene;

    public GameEngine(Window window, Resource... resources) {
        this.window = window;
        this.windowId = window.getId();
        this.systemManager = new SystemManager(resources);
    }

    @Override
    public void run() {
        initializeOpenGL();

        systemManager.loadSystems();
        systemManager.start();
        debug();

        var lastTime = System.nanoTime();
        while (!window.shouldClose()) {
            var currentTime = System.nanoTime();
            float dt = (currentTime - lastTime) * 1E-9f;
            lastTime = currentTime;

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            systemManager.update(dt);

            glfwSwapBuffers(windowId); // swap the color buffers
        }

        systemManager.stop();
    }

    public void setActiveScene(Scene scene) {
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

    private void debug() {
        var scene = new Scene("Debug Scene");
        for (int i = 0; i < 10; i++) {
            var square = new Square("Square" + i);
            scene.addGameObject(square);
            square.transform().scale(0.1f);
            var xPos = ((float) Math.random() * 2) - 1.0f;
            var yPos = ((float) Math.random() * 2) - 1.0f;
            square.transform().position.set(xPos, yPos);
            square.velocity().rotation = new Vector2f((float) Math.random(), (float) Math.random());
        }
        setActiveScene(scene);
    }
}
