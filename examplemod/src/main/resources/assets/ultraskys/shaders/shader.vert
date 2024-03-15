#version 330 core

in vec3 a_position;

uniform mat4 u_projViewTrans;
uniform float u_time;
uniform float u_scale;
uniform float u_speed;

out vec3 worldPos;

float rand(vec2 co)
{
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

float noise(vec2 p)
{
    vec2 i = floor(p);
    vec2 f = fract(p);

    // Smooth the coordinates
    vec2 u = f * f * (3.0 - 2.0 * f);

    // Get random values at the four corners
    float a = rand(i);
    float b = rand(i + vec2(1.0, 0.0));
    float c = rand(i + vec2(0.0, 1.0));
    float d = rand(i + vec2(1.0, 1.0));

    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

void main()
{
    vec3 offset = vec3(a_position.x * u_scale, a_position.y * u_scale, a_position.z * u_scale);
    float timeFactor = u_time * u_speed;

    vec3 noiseOffset = vec3(noise(offset.xy + timeFactor), noise(offset.yz + timeFactor), noise(offset.zx + timeFactor));
    vec3 newPosition = a_position + noiseOffset;

    worldPos = newPosition;
    gl_Position = u_projViewTrans * vec4(newPosition, 1.0);
}