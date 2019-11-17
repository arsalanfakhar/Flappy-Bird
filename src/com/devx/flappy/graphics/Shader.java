package com.devx.flappy.graphics;

import com.devx.flappy.math.Matrix4f;
import com.devx.flappy.math.Vector3f;
import com.devx.flappy.utils.ShaderUtils;
import com.devx.flappy.graphics.Shader;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

public class Shader {
	
	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;
	
	public static Shader BG, BIRD, PIPE, FADE;
	
	private boolean enabled = false;
	
	private final int ID;
	
	//inorder to avoid cpu from calling again ang again we cache the location of the shader
	//as it remain same throughout the program
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	
	public Shader(String vertex, String fragment) {
		ID = ShaderUtils.load(vertex, fragment);
	}
	
	public static void loadAll() {
		BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
		BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
		PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
		FADE = new Shader("shaders/fade.vert", "shaders/fade.frag");
	}
	
	//get shader to setup its things
	public int getUniform(String name) {
		if (locationCache.containsKey(name))
			return locationCache.get(name);
		
		int result = glGetUniformLocation(ID, name);
		if (result == -1) 
			System.err.println("Could not find uniform variable '" + name + "'!");
		else
			locationCache.put(name, result);
		return result;
	}
	
	//uniform variables help us provide data from the CPU
	public void setUniform1i(String name, int value) {
		if (!enabled) enable();
		glUniform1i(getUniform(name), value);
	}
	
	//uniform variables help us provide data from the CPU 
	//this for float
	public void setUniform1f(String name, float value) {
		if (!enabled) enable();
		glUniform1f(getUniform(name), value);
	}
	
	//this for float
	public void setUniform2f(String name, float x, float y) {
		if (!enabled) enable();
		glUniform2f(getUniform(name), x, y);
	}
	
	public void setUniform3f(String name, Vector3f vector) {
		if (!enabled) enable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniformMat4f(String name, Matrix4f matrix) {
		if (!enabled) enable();
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}
	
	//bind the program
	public void enable() {
		glUseProgram(ID);
		enabled = true;
	}
	
	//unbind the program
	public void disable() {
		glUseProgram(0);
		enabled = false;
	}
	
}
