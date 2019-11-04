package com.devx.flappy.graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;

import javax.imageio.ImageIO;

import com.devx.flappy.utils.BufferUtils;


public class Texture {
	private int width, height;
	//id that was assigned to texture
	private int texture;
	
	public Texture(String path) {
		texture=load(path);
	}

	//make array of pixels here
	private int load(String path) {
		int[] pixels = null;
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[getWidth() * height];
			//populate the pixel array with the data
			image.getRGB(0, 0, getWidth(), height, pixels, 0, getWidth());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//as we need to re arrange the data before we pass it into opengl
		//we need to change the format what open gl wants
		int[] data = new int[getWidth() * height];
		for (int i = 0; i < getWidth() * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		//create a new texture
		int result = glGenTextures();
		//bind texture to select it
		glBindTexture(GL_TEXTURE_2D, result); //it is a 2d texture
		//it will disable anti aliasing for our texture and we dont want texture to be blurred if game runs at high resolution
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		//but now lwjgl doesn need array it need buffers so we convert
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, getWidth(), height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		//unbind texture to de select it
		glBindTexture(GL_TEXTURE_2D, 0);
		return result;
	}

	public int getWidth() {
		return width;
	}

	public int getTexture() {
		return texture;
	}
	public int getHeight() {
		return height;
	}
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
