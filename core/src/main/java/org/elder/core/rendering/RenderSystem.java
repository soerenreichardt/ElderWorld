package org.elder.core.rendering;

import org.elder.core.ecs.ComponentManager;
import org.elder.core.ecs.System;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;

public class RenderSystem implements System {

    private final List<Mesh> meshComponents;
    private final List<Buffers> buffersList;

    public RenderSystem() {
        this.meshComponents = ComponentManager
                .getInstance()
                .getComponentListReference(Mesh.class)
                .orElseGet(List::of);
        this.buffersList = new ArrayList<>(meshComponents.size());
    }

    public void start() {
        for (Mesh mesh : meshComponents) {
            int ibo = glGenBuffers();
            int vbo = glGenBuffers();

            var vertexBuffer = BufferUtils.createFloatBuffer(mesh.vertices.length);
            for (Vector2f vertex : mesh.vertices) {
                vertex.get(vertexBuffer);
            }

            var indexBuffer = BufferUtils.createIntBuffer(mesh.indices.length);
            indexBuffer.put(mesh.indices);

            buffersList.add(new Buffers(vbo, ibo, vertexBuffer, indexBuffer));
        }
    }

    @Override
    public void update(float delta) {
        buffersList.forEach(this::renderMesh);
    }

    private void renderMesh(Buffers buffers) {
        var vertexBuffer = buffers.vertexBuffer();

        glBindBuffer(GL_ARRAY_BUFFER, buffers.vbo());
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer.flip(), GL_STATIC_DRAW);
        glEnableClientState(GL_VERTEX_ARRAY);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers.ibo());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffers.indexBuffer(), GL_STATIC_DRAW);
        glVertexPointer(2, GL_FLOAT, 0, 0L);

        glDrawArrays(GL_TRIANGLES, 0, vertexBuffer.capacity());
    }

    record Buffers(
            int vbo,
            int ibo,
            FloatBuffer vertexBuffer,
            IntBuffer indexBuffer
    ) {}
}
