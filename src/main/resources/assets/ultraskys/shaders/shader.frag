#version 330 core
#ifdef GL_ES
precision mediump float;
#endif

in vec3 worldPos;

out vec4 outColor;

float cloudShape(float x, float y) {

    float blockX = floor(x);
    float blockY = floor(y);

    float n = fract(sin(dot(vec2(blockX, blockY), vec2(12.9898, 4.1414))) * 43758.5453);

    float threshold = 0.5;
    return n > threshold ? 1.0 : 0.0;
}

void main()
{
    float cloudDensity = cloudShape(worldPos.x, worldPos.y);
    if (cloudDensity < 1.0) discard;
    outColor = vec4(1.0, 1.0, 1.0, 0.2); // Set the cloud color to white
}
