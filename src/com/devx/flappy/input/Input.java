package com.devx.flappy.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback {

	public static boolean[] keys = new boolean[65536];

	// Setup a key callback. It will be called every time a key is pressed, repeated or released
	public void invoke(long window, int key, int scancode, int action, int mods) {
		//Auto-generated method abstract
		keys[key] = action ==GLFW.GLFW_RELEASE ?false:true; 
	}
	
	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	
}
