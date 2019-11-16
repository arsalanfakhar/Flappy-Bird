package com.devx.flappy.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback {

	public static boolean[] keys=new boolean[62236];
	
	// Setup a key callback. It will be called every time a key is pressed, repeated or released.
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		// TODO Auto-generated method stub
		//if action not equal release
		keys[key] = action != GLFW.GLFW_RELEASE; 
	}
	
	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	
}
