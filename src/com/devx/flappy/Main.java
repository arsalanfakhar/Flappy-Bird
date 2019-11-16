package com.devx.flappy;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.devx.flappy.graphics.Shader;
import com.devx.flappy.input.Input;
import com.devx.flappy.level.Level;
import com.devx.flappy.math.Matrix4f;

public class Main implements Runnable {

	private int width = 1000;
	private int height = 600;
	private long window;
	private Thread thread;
	private boolean running=false;
	private Level level;
	
	public void start() {
		running=true;
		thread=new Thread(this,"Game");
		thread.start();
	}
	
	//initialize all the things
	public void init() {
		//now we will use glfw which is a c library like glut
		if(!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
			
		}
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		
		//window will have an id
		window=glfwCreateWindow(width, height, "Flappy", NULL, NULL);
		if(window==NULL) {
			System.err.println("Unable to create GLFW window!");
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		
		//now to set window to center of screen
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		
		glfwSetWindowPos(window, (vidmode.width()-width)/2, (vidmode.height()-height)/2);
		
		//setting callbacks
		glfwSetKeyCallback(window, new Input());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		
 		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		
		System.out.println("Open GL:"+glGetString(GL_VERSION));
		
		Shader.loadAll();
		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.setUniform1i("tex", 1);
		
		Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BIRD.setUniform1i("tex", 1);
		
		Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.PIPE.setUniform1i("tex", 1);
		level = new Level();
	}
	
	
	
	//it will start on a separate thread
	@Override
	public void run() {
		// TODO Auto-generated method stub
		init();
		while(running) {
			update();
			render();
			
			if(glfwWindowShouldClose(window)) {
				running=false;
			}
		}
	}
	private void update() {
		//to refresh the window
		glfwPollEvents();
		
		if(Input.keys[GLFW_KEY_SPACE]) {
			System.out.println("Flap bird");
		}
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(error);
		glfwSwapBuffers(window);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main().start();
	}
	
}
