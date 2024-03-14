#version 330 core

in vec3 a_position;
//in vec2 a_texCoord0;

uniform mat4 u_projViewTrans;
//uniform vec3 u_positionOffset;

//out vec2 v_texCoord0;
out vec3 worldPos;

void main()
{
    worldPos = a_position;// + u_positionOffset;
	//v_texCoord0 = a_texCoord0.xy;

	gl_Position = u_projViewTrans * vec4(worldPos, 1.0);

}
