package org.elder.geometry;

import org.elder.engine.ecs.api.AbstractGameObject;
import org.elder.engine.physics.Velocity;
import org.elder.engine.rendering.DefaultShader;
import org.elder.engine.rendering.Mesh;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;

public class Square extends AbstractGameObject {

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

    private Mesh mesh;
    private Velocity velocity;

    public Square(String name) {
        super(name);
    }

    @Override
    protected void start() {
        this.mesh = addComponent(Mesh.class);
        this.velocity = addComponent(Velocity.class);
        this.velocity.transform = transform;
        initializeMesh();
    }

    public Velocity velocity() {
        return this.velocity;
    }

    private void initializeMesh() {
        mesh.vertices = SQUARE_VERTICES;
        mesh.indices = SQUARE_INDICES;
        mesh.shader = new DefaultShader(GL.getCapabilities());
        mesh.transform = transform;
    }
}
