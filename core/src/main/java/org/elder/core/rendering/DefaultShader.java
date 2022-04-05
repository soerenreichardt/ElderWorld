package org.elder.core.rendering;

import org.lwjgl.opengl.GLCapabilities;

public class DefaultShader extends Shader {
    public DefaultShader(GLCapabilities capabilities) {
        super("shader/default.vert", "shader/default.frag", capabilities);
    }
}
