#version 330 core

in vec3 a_position;

uniform mat4 u_projViewTrans;
uniform float u_time;
uniform float u_scale;
uniform float u_speed;

out vec3 worldPos;

void main()
{
    vec3 offset = vec3(a_position.x * u_scale, 0.0, a_position.z * u_scale);
    float timeFactor = u_time * u_speed;

    vec3 noiseOffset = vec3(0.0);
    vec3 newPosition = a_position + noiseOffset;


    // newPosition.y = 5.0;

    // newPosition.xz = floor(newPosition.xz + 0.5);

    // float layerDensity = 1.0;
    // newPosition.y *= layerDensity;

    // float heightFactor = 1.0;
    // worldPos = vec3(newPosition.xz, heightFactor);
    worldPos = newPosition;

    newPosition.x += u_time * 0.4;

    // newPosition.y = max(newPosition.y, 5.0);

    // newPosition.y += 0.0;

    gl_Position = u_projViewTrans * vec4(newPosition, 1.0);
}