#version 330 core
#ifdef GL_ES
precision mediump float;
#endif

//in vec2 v_texCoord0;
in vec3 worldPos;

//uniform sampler2D texDiffuse;

out vec4 outColor;

void main()
{
    // vec4 texColor = texture(texDiffuse, v_texCoord0);
    // outColor = vec4(texColor.rgba);
    outColor = vec4(1.0);
}