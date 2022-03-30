package org.elder.core.rendering;

import org.elder.core.ecs.ComponentManager;
import org.elder.core.ecs.GameSystem;
import org.elder.core.ecs.Transform;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderSystem implements GameSystem {

    private final List<Mesh> meshComponents;
    private final List<RenderObject> buffersList;
    private int vao;

    public RenderSystem() {
        this.meshComponents = ComponentManager
                .getInstance()
                .getComponentListReference(Mesh.class)
                .orElseGet(List::of);
        this.buffersList = new ArrayList<>(meshComponents.size());
    }

    @Override
    public void start() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        for (Mesh mesh : meshComponents) {
            int vbo = glGenBuffers();
            int ibo = glGenBuffers();
            var indices = fillOpenGLBuffers(mesh, vbo, ibo);

            var shader = mesh.shader;
            shader.compile();

            var positionMatrixLocation = shader.positionMatrixLocation();
            glEnableVertexAttribArray(positionMatrixLocation);
            glVertexAttribPointer(positionMatrixLocation, 2, GL_FLOAT, false, 0, 0L);

            buffersList.add(new RenderObject(indices, mesh.transform, shader, vbo, ibo));
        }
    }

    @Override
    public void stop() {
        cleanUp();
    }

    @Override
    public void reset() {
        cleanUp();
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

    @Override
    public void update(float delta) {
        buffersList.forEach(this::renderMesh);
    }

    private void renderMesh(RenderObject obj) {
        obj.shader().use();
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);

        glDrawElements(GL_TRIANGLES, obj.numTriangles(), GL_UNSIGNED_INT, 0L);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        glUseProgram(0);
    }

    private void cleanUp() {
        glDeleteVertexArrays(vao);
        buffersList.forEach(renderObject -> {
            glDeleteBuffers(renderObject.vbo());
            glDeleteBuffers(renderObject.ibo());
            renderObject.shader().delete();
        });
    }

    record RenderObject(
            int numTriangles,
            Transform transform,
            Shader shader,
            int vbo,
            int ibo
    ) {}
}
