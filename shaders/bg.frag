#version 330 core

//colr fragment
layout (location = 0) out vec4 color;

//in goes from vertexx shader
in DATA
{
	vec2 tc;
	vec3 position;
} fs_in;

uniform vec2 bird;
uniform sampler2D tex;
uniform float time;

void main()
{
	color = texture(tex, fs_in.tc);//we pass texture with color info and vertecies

	//din se shru karo
	//lightining based on bird
	if(time<4.0)
		color *= 2.0 / (length(bird - fs_in.position.xy) + 2.5) + 0.7; //last parameter is for glow of the bird
	
	//sham hogayi
	if(time>4.0 && time <10.0)
		color *= 2.0 / (length(bird - fs_in.position.xy) + 2.5) + 0.6; //last parameter is for glow of the bird
	
	//rat hogayi
	if(time>10.0)
		color *= 2.0 / (length(bird - fs_in.position.xy) + 2.5) + 0.4; //last parameter is for glow of the bird

}