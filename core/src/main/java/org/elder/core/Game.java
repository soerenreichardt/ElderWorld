package org.elder.core;

import org.elder.core.ecs.GameSystem;
import org.elder.core.physics.PositioningSystem;
import org.elder.core.rendering.RenderSystem;
import org.elder.geometry.Square;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game extends Thread {

    private final BooleanSupplier shouldCloseFn;
    private final long window;
    private final int width;
    private final int height;

    private final List<GameSystem> systems;

    private Scene activeScene;

    public Game(
            BooleanSupplier shouldCloseFn,
            long window,
            int width,
            int height
    ) {
        this.shouldCloseFn = shouldCloseFn;
        this.window = window;
        this.width = width;
        this.height = height;
        this.systems = new ArrayList<>();
    }

    @Override
    public void run() {
        initializeOpenGL();

        addSystems();
        initializeSystems();

        debug();

        var lastTime = System.nanoTime();
        while (!shouldCloseFn.getAsBoolean()) {
            var currentTime = System.nanoTime();
            float dt = (currentTime - lastTime) * 1E-9f;
            lastTime = currentTime;

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glViewport(0, 0, width, height);

            systems.forEach(system -> system.update(dt));

            glfwSwapBuffers(window); // swap the color buffers
        }

        removeSystems();
    }

    public void setActiveScene(Scene scene) {
        if (activeScene != scene) {
            activeScene = scene;
            scene.camera().initializeProjectionMatrix(width, height);
            systems.forEach(system -> system.onSceneChanged(scene));
        }
    }

    private void initializeOpenGL() {
        glfwMakeContextCurrent(window);
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

        glfwSwapInterval(1);
    }

    private void addSystems() {
        this.systems.add(new PositioningSystem());
        this.systems.add(new RenderSystem(width, height));
    }

    private void initializeSystems() {
        this.systems.forEach(GameSystem::start);
    }

    private void removeSystems() {
        this.systems.forEach(GameSystem::stop);
    }

    private void debug() {
        var scene = new Scene("Debug Scene");
        for (int i = 0; i < 10; i++) {
            var square = new Square("Square" + i);
            scene.addGameObject(square);
            square.transform().scale(0.1f);
            var xPos = (float) Math.random();
            var yPos = (float) Math.random();
            square.transform().position.set(xPos, yPos);
            square.velocity().rotation = new Vector2f((float) Math.random(), (float) Math.random());
        }
        setActiveScene(scene);
    }
}
