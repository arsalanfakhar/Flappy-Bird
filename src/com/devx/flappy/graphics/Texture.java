package com.devx.flappy.graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;

import javax.imageio.ImageIO;

import com.devx.flappy.utils.BufferUtils;

//this class will be used to load images in the games
public class Texture {
	private int width, height;
	
	//id that was assigned to texture
	private int texture;
	
	public Texture(String filePath) {
		texture = load(filePath);
	}
	
	
	private int load(String path) {
		//make array of pixels here
		int[] pixels = null;
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			//populate the pixel array with the data
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//as we need to re arrange the data before we pass it into opengl
		//we need to change the format what open gl wants
		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		//create a new texture
		int result = glGenTextures();
		
		//bind texture to select it and perform operations like selecting a layer in photoshop
		glBindTexture(GL_TEXTURE_2D, result);
		
		//it will disable anti aliasing for our texture and we dont want texture to be blurred if game runs at high resolution
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		//but now lwjgl doesn need array it need buffers so we convert
		//select the layer and then apply the texture
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		
		//unbind texture to de select it
		glBindTexture(GL_TEXTURE_2D, 0);
		return result;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
