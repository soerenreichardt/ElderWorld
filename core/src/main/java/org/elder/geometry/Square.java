package org.elder.geometry;

import org.elder.core.ecs.GameObject;
import org.elder.core.rendering.DefaultShader;
import org.elder.core.rendering.Mesh;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;

public class Square extends GameObject {

    public static final Vector2f[] SQUARE_VERTICES = new Vector2f[] {
            new Vector2f(-1.0f, 1.0f),
            new Vector2f(-1.0f, -1.0f),
            new Vector2f(1.0f, -1.0f),
            new Vector2f(1.0f, 1.0f)
    };

    private static final int[] SQUARE_INDICES = new int[] {
            0, 1, 3,
            3, 1, 2
    };

    private final Mesh mesh;

    public Square(String name) {
        super(name);
        this.mesh = addComponent(Mesh.class);

        initializeMesh();
    }

    private void initializeMesh() {
        mesh.vertices = SQUARE_VERTICES;
        mesh.indices = SQUARE_INDICES;
        mesh.shader = new DefaultShader(GL.getCapabilities());
    }
}
