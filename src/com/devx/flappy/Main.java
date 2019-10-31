package com.devx.flappy;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWVidMode;

public class Main implements Runnable {

	private int width = 1000;
	private int height = 600;
	private long window;
	private Thread thread;
	private boolean running=false;
	public void start() {
		running=true;
		thread=new Thread(this,"Game");
		thread.start();
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main().start();
		
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
			
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		//now to set window to center of screen
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		
		glfwSetWindowPos(window, (vidmode.width()-width)/2, (vidmode.height()-height)/2);
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		
		
	}
	
	
	//it will start with thread
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
	}

	private void render() {
		glfwSwapBuffers(window);
	}
}
