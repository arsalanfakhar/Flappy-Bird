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
	
	private Thread thread;
	
	//state of game
	private boolean running = false;
	
	private long window;
	
	private Level level;
	
	//starts the game
	public void start() {
		running = true;
		//run game on a different thread
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	//initialize all the open gl things
	private void init() {
		//now we will use glfw which is a c library like glut
		if (glfwInit() != true) {
			System.err.println("Could not initialize GLFW!");
			return;
		}
		
		//hints for windows
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		
		//window will have an id because in C there is no concept of objects
		window = glfwCreateWindow(width, height, "Flappy", NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create GLFW window!");
			return;
		}
		
		//now to set window to center of screen
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		
		//setting callbacks for key input
		glfwSetKeyCallback(window, new Input());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		
 		glEnable(GL_DEPTH_TEST);
 		
		glActiveTexture(GL_TEXTURE1);
		
		//Enable blending to blend the opaque and background with each other
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		//to load all the images
		Shader.loadAll();	
		
		//Projection matrix	for background
		//set the matriz with the coordinates	
		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.setUniform1i("tex", 1); //1 means first texture is active
		
		//Projection matrix	for bird
		Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BIRD.setUniform1i("tex", 1);
		
		//Projection matrix	for pipe
		Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.PIPE.setUniform1i("tex", 1);
		
		level = new Level();
	}
	
	//it will start on a separate thread as we want to render the graphics on one thread and game logic on another
	@Override
	public void run() {
		init();
		//we create a timer so it runs only 60 times per second
		long lastTime = System.nanoTime();
		double delta = 0.0; //changein time
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0; //it tells how many times we update normally 60
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			//if delta reaches 1 we have done it 60 times per second
			if (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			//it means per second
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;//for nest second
 				//as we reset them every second
				updates = 0;
				frames = 0;
			}
			if (glfwWindowShouldClose(window) == true)
				running = false;
		}
		
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	private void update() {
		//to refresh the window
		
		//This function processes only those events that are already in the event queue and then returns immediately. 
		//Processing events will cause the window and input callbacks associated with those events to be called.
		glfwPollEvents();
		
		level.update();
		if (level.isGameOver()) {
			level = new Level();
		}
	}
	
	private void render() {
		//every time graphic is rendered it needs to be cleared
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(error);
		
		//GLFW has be default double buffers front which is display and back is which we render to.
		//When the entire frame has been rendered, 
		//it is time to swap the back and the front buffers in order to display what has been rendered and begin rendering a new frame.
		/* Swap front and back buffers */
		glfwSwapBuffers(window);
	}

	public static void main(String[] args) {
		new Main().start();
	}
	
}
