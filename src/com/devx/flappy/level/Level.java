package com.devx.flappy.level;
import java.util.Random;

import com.devx.flappy.graphics.Shader;
import com.devx.flappy.graphics.Texture;
import com.devx.flappy.graphics.VertexArray;
import com.devx.flappy.input.Input;
import com.devx.flappy.math.Matrix4f;
import com.devx.flappy.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Level {

	private VertexArray background, fade;
	private Texture bgTexture;
	
	private int xScroll = 0;
	private int map = 0; //to advance the screen
	
	private Bird bird;
	
	private Pipe[] pipes = new Pipe[5 * 2];//amount of pipes
	private int index = 0;
	private float OFFSET = 5.0f; //quater of the screen
	private boolean control = true, reset = false;
	
	private Random random = new Random();
	
	//it is updated every second
	private float time = 0.0f;
	
	public Level() {
		//it takes up half of our screen
		float[] vertices = new float[] {
			-10.0f, -10.0f * 9.0f / 16.0f, 0.0f, //bottom left
			-10.0f,  10.0f * 9.0f / 16.0f, 0.0f,//top left
			  0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
			  0.0f, -10.0f * 9.0f / 16.0f, 0.0f
		};
		
		//they will help us not to make 6 vertices instead we can use 3 only by reusing them
		byte[] indices = new byte[] {
			//for first triangle
			0, 1, 2,
			//for second traingle
			2, 3, 0
		};
		
		//texture cordinates applied to vertices
		float[] tcs = new float[] {
			0, 1,
			0, 0,
			1, 0,
			1, 1
		};
		
		fade = new VertexArray(6);
		background = new VertexArray(vertices, indices, tcs);
		bgTexture = new Texture("res/bg.jpeg");
		bird = new Bird();
		
		createPipes();
	}
	
	//it will create only 4 set of pipes
	private void createPipes() {
		Pipe.create();
		for (int i = 0; i < 5 * 2; i += 2) {
			//inorder to plac pipes at random positions
			pipes[i] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
			pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 11.5f); //y==distance b/w pipes increase or decrease to set difficulty
			index += 2;
		}
	}
	
	//but we need more pipes so we loop through them
	private void updatePipes() {
		pipes[index % 10] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
		pipes[(index + 1) % 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 11.5f);
		index += 2;
	}
	
	public void update() {
		if (control) {
			xScroll--;
			if (-xScroll % 335 == 0) map++;
			//update pipes after when scrool percentage is 250 percent
			if (-xScroll > 250 && -xScroll % 120 == 0)
				updatePipes();
		}
		
		bird.update();
		//what to do when collision detected
		//when collision is there control is lost
		if (control && collision()) {
			bird.fall();
			control = false;
		}
			
		//Reset when space is pressed and no control
		if (!control && Input.isKeyDown(GLFW_KEY_SPACE))	
			reset = true;
		
		//update time
		time += 0.01f;
		System.out.println("time"+time);
	}
	
	private void renderPipes() {
		Shader.PIPE.enable();
		Shader.PIPE.setUniform2f("bird", 0, bird.getY());
		Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0.0f, 0.0f))); //as translation is only done on x axis
		Pipe.getTexture().bind();
		Pipe.getMesh().bind();
		
		//10 pipes are present on the screen 5 on top and 5 on bottom
		for (int i = 0; i < 5 * 2; i++) {
			Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
			Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
			Pipe.getMesh().draw();
		}
		
		Pipe.getMesh().unbind();
		Pipe.getTexture().unbind();
	}
	
	//This will check for bird and pipe collision
	private boolean collision() {
		//Loop will check for all 10 pipes
		for (int i = 0; i < 5 * 2; i++) {
			//Position of bird and pipe
			float bx = -xScroll * 0.05f;
			float by = bird.getY();
			float px = pipes[i].getX();
			float py = pipes[i].getY();
			
			//Left and right bounds of bird 
			float bx0 = bx - bird.getSize() / 2.0f;
			float bx1 = bx + bird.getSize() / 2.0f;
			float by0 = by - bird.getSize() / 2.0f;
			float by1 = by + bird.getSize() / 2.0f;
			
			//Left and right bounds of pipe
			float px0 = px;
			float px1 = px + Pipe.getWidth();
			float py0 = py;
			float py1 = py + Pipe.getHeight();
			
			//this means we are inside pipe horizontally
			if (bx1 > px0 && bx0 < px1) {
				//this means we are inside pipe vertically
				if (by1 > py0 && by0 < py1) {
					return true;
				}
			}
		}
		return false;
	}	
	
	public boolean isGameOver() {
		return reset;
	}
	
	//background will be drawn 4 times on screen and is moving left
	public void render() {
		bgTexture.bind();
		Shader.BG.enable();
		Shader.BG.setUniform2f("bird", 0, bird.getY());
		//addition
		Shader.BG.setUniform1f("time", time);
		
		background.bind(); //bind once and then draw 4 times
		for (int i = map; i < map + 4; i++) {
			Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f))); //sets the bacground speed
			background.draw();
		}
		Shader.BG.disable();
		bgTexture.unbind();
		
		renderPipes();
		bird.render();
		
		Shader.FADE.enable();
		Shader.FADE.setUniform1f("time", time);
		fade.render();
		Shader.FADE.disable();
	}	
}
