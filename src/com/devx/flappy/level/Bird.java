package com.devx.flappy.level;

import static org.lwjgl.glfw.GLFW.*;

import com.devx.flappy.graphics.Shader;
import com.devx.flappy.graphics.Texture;
import com.devx.flappy.graphics.VertexArray;
import com.devx.flappy.input.Input;
import com.devx.flappy.math.Matrix4f;
import com.devx.flappy.math.Vector3f;

public class Bird {

	private float SIZE = 1.0f; //size of the bird
	private VertexArray mesh; 
	private Texture texture;
	
	private Vector3f position = new Vector3f();
	private float rot;//only for z axis of bird as we used ortho 3d
	private float delta = 0.0f; //for change in y axis
	
	public Bird() {
		float[] vertices = new float[] {
			-SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
			-SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
			 SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
			 SIZE / 2.0f, -SIZE / 2.0f, 0.2f
		};
			
		byte[] indices = new byte[] {
			0, 1, 2,
			2, 3, 0
		};
		
		float[] tcs = new float[] {
			0, 1,
			0, 0,
			1, 0,
			1, 1
		};
		
		mesh = new VertexArray(vertices, indices, tcs); //mesh
		texture = new Texture("res/bird.png");
	}
	
	public void update() {
		position.y -= delta; //It will have delta value from fall
		if (Input.isKeyDown(GLFW_KEY_SPACE)) 
			delta = -0.15f;//to go above
		else
			delta += 0.01f;//constant falling of bird
		
		rot = -delta * 90.0f;
	}
	
	public void fall() {
		delta = -0.15f; //amount by which the bird should fall
	}
	
	public void render() {
		Shader.BIRD.enable();
		//it will translate as well as rotate the bird
		Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot))); //this is done for the shader
		texture.bind();
		mesh.render();
		Shader.BIRD.disable();
	}

	public float getY() {
		return position.y;
	}

	public float getSize() {
		return SIZE;
	}

	
}
