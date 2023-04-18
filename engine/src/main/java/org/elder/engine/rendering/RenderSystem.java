package org.elder.engine.rendering;

import org.elder.engine.Scene;
import org.elder.engine.ecs.Transform;
import org.elder.engine.ecs.api.AbstractGameObject;
import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.ecs.api.GameSystem;
import org.elder.engine.ecs.api.UpdatableSystem;
import org.elder.engine.physics.PositioningSystem;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

@GameSystem({ PositioningSystem.class })
public class RenderSystem implements UpdatableSystem {

    private List<RenderObject> buffersList;
    private Iterable<Mesh> meshComponents;
    private Camera camera;

    public RenderSystem() {
        this.buffersList = new ArrayList<>();
        this.meshComponents = Collections.emptyList();
    }

    @Override
    public void start() {
        initialize();
    }

    @Override
    public void onSceneChanged(BasicScene scene) {
        cleanUp();
        this.meshComponents = scene.componentManager().componentListIterable(Mesh.class);
        this.buffersList = new ArrayList<>();
        this.camera = ((Scene)scene).camera();
        initialize();
    }

    @Override
    public void onGameObjectAdded(AbstractGameObject gameObject) {
        if (gameObject.hasComponent(Mesh.class)) {
            var newMesh = gameObject.getComponent(Mesh.class);
            if (!newMesh.isInitialized()) {
                initializeMesh(newMesh);
            }
        }
    }

    @Override
    public void stop() {
        cleanUp();
    }

    @Override
    public void update(float delta) {
        buffersList.forEach(this::renderMesh);
    }

    private void initialize() {
        meshComponents.forEach(this::initializeMesh);
    }

    private void initializeMesh(Mesh mesh) {
        var vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        int ibo = glGenBuffers();
        var indices = fillOpenGLBuffers(mesh, vbo, ibo);

        var shader = mesh.shader;
        shader.compile();

        shader.use();
        glUniformMatrix4fv(shader.projectionMatrixUniformLocation(), false, camera.projectionMatrix().get(new float[16]));
        shader.unUse();

        var positionMatrixLocation = shader.positionMatrixLocation();
        glEnableVertexAttribArray(positionMatrixLocation);
        glVertexAttribPointer(positionMatrixLocation, 2, GL_FLOAT, false, 0, 0L);

        buffersList.add(new RenderObject(indices, mesh.transform, shader, vao, vbo, ibo));
    }

    private void renderMesh(RenderObject obj) {
        var shader = obj.shader();
        shader.use();
        glBindVertexArray(obj.vao());
        glEnableVertexAttribArray(shader.positionMatrixLocation());

        GL20.glUniformMatrix4fv(shader.modelMatrixUniformLocation(), false, obj.transform().getModelMatrix().get(new float[16]));
        glUniformMatrix4fv(shader.viewMatrixUniformLocation(), false, camera.viewMatrix().get(new float[16]));

        glDrawElements(GL_TRIANGLES, obj.numTriangles(), GL_UNSIGNED_INT, 0L);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        glUseProgram(0);
    }

    private int fillOpenGLBuffers(Mesh mesh, int vbo, int ibo) {
        var vertexBuffer = BufferUtils.createFloatBuffer(mesh.vertices.length * 2);
        Vector2f[] vertices = mesh.vertices;
        for (Vector2f vertex : vertices) {
            vertexBuffer.put(vertex.x);
            vertexBuffer.put(vertex.y);
        }

        var indexBuffer = BufferUtils.createIntBuffer(mesh.indices.length);
        indexBuffer.put(mesh.indices);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer.flip(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.flip(), GL_STATIC_DRAW);

        return indexBuffer.capacity();
    }

    private void cleanUp() {
        buffersList.forEach(renderObject -> {
            glDeleteBuffers(renderObject.vbo());
            glDeleteBuffers(renderObject.ibo());
            glDeleteVertexArrays(renderObject.vao());
            renderObject.shader().delete();
        });
    }

    record RenderObject(
            int numTriangles,
            Transform transform,
            Shader shader,
            int vao,
            int vbo,
            int ibo
    ) {}
}
