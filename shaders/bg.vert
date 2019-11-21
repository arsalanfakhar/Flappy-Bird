#version 330 core

//0 and 1 attribute from shaders	
layout (location = 0) in vec4 position; 
layout (location = 1) in vec2 tc; //texture cordinate

//projection matrix
uniform mat4 pr_matrix;
uniform mat4 vw_matrix;

//struct to send out data into fragment shader
out DATA
{
	vec2 tc;
	vec3 position;
} vs_out;

void main()
{
	gl_Position = pr_matrix * vw_matrix * position; //it will set the position
	//setting struct values
	vs_out.tc = tc;
	
	//position of backgroung for lightining not inclusive of projection matrix
	vs_out.position = vec3(vw_matrix * position);
}