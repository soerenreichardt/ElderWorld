package org.elder.core.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memSlice;

public class Shader {

    private static final int UNINITIALIZED = -1;

    private final String vertexShaderPath;
    private final String fragmentShaderPath;
    private final int version;
    private int program;

    public Shader(String vertexShaderPath, String fragmentShaderPath, GLCapabilities capabilities) {
        this.vertexShaderPath = vertexShaderPath;
        this.fragmentShaderPath = fragmentShaderPath;

        this.version = openGLVersion(capabilities);
        this.program = UNINITIALIZED;
    }

    public void compile() {
        var vertexShader = glCreateShader(GL_VERTEX_SHADER);
        var fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        compileShader(vertexShaderPath, vertexShader);
        compileShader(fragmentShaderPath, fragmentShader);

        var program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);

        printProgramInfoLog(program);

        if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
            throw new IllegalStateException("Failed to link program.");
        }

        this.program = program;
    }

    public void use() {
        if (program == UNINITIALIZED) {
            throw new IllegalStateException("Expected shader to be compiled and linked to a program");
        }
        glUseProgram(program);
    }

    private ByteBuffer readShaderFile(String shaderPath) {
        try {
            return ioResourceToByteBuffer(shaderPath, 4096);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void compileShader(String shaderPath, int shader) {
        var byteBuffer = readShaderFile(shaderPath);
        compileShader(version, shader, byteBuffer);
    }

    private void compileShader(int version, int shader, ByteBuffer code) {
        try (MemoryStack stack = stackPush()) {
            ByteBuffer header = stack.ASCII("#version " + version + "\n#line 0\n", false);

            glShaderSource(
                    shader,
                    stack.pointers(header, code),
                    stack.ints(header.remaining(), code.remaining())
            );

            glCompileShader(shader);
            printShaderInfoLog(shader);

            if (glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE) {
                throw new IllegalStateException("Failed to compile shader.");
            }
        }
    }


    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        var path = Path.of(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) {
                }
            }
        } else {
            try (
                    InputStream source = Shader.class.getClassLoader().getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)
            ) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                    }
                }
            }
        }

        buffer.flip();
        return memSlice(buffer);
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    private static void printShaderInfoLog(int obj) {
        int infoLogLength = glGetShaderi(obj, GL_INFO_LOG_LENGTH);
        if (infoLogLength > 0) {
            glGetShaderInfoLog(obj);
            System.out.format("%s\n", glGetShaderInfoLog(obj));
        }
    }

    private static void printProgramInfoLog(int obj) {
        int infoLogLength = glGetProgrami(obj, GL_INFO_LOG_LENGTH);
        if (infoLogLength > 0) {
            glGetProgramInfoLog(obj);
            System.out.format("%s\n", glGetProgramInfoLog(obj));
        }
    }

    private static int openGLVersion(GLCapabilities capabilities) {
        int version;
        if (capabilities.OpenGL33) {
            version = 330;
        } else if (capabilities.OpenGL21) {
            version = 120;
        } else {
            version = 110;
        }
        return version;
    }
}
