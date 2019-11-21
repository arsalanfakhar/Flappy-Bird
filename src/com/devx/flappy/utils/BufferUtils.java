package com.devx.flappy.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {

	private BufferUtils() {
		
	}
	
	public static ByteBuffer createByteBuffer(byte[] array) {
		//as they are onlyy 1 bytes
		ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		//flip to reverse it as lwjgl wants it in coloumn major order  
		result.put(array).flip();
		return result;
	} 
	
	public static FloatBuffer createFloatBuffer(float[] array) {
		//as they are only 4 bytes
		FloatBuffer result = ByteBuffer.allocateDirect(array.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		
		//flip to reverse it
		result.put(array).flip();
		return result;
	}
	
	public static IntBuffer createIntBuffer(int[] array) {
		//as they are onlyy 4 bytes
		IntBuffer result = ByteBuffer.allocateDirect(array.length*4).order(ByteOrder.nativeOrder()).asIntBuffer();
		//flip to reverse it
		result.put(array).flip();
		return result;
	}
}
