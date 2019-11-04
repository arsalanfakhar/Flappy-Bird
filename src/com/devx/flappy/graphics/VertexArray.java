package com.devx.flappy.graphics;
//open gl versions
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.devx.flappy.utils.BufferUtils;

public class VertexArray {
	//the vertices which will be rendered
	
	//VAO=Vertex Array Object
	private int vao,vbo,ibo,tbo;
	//texture cordinate object
	private int count;
	
	 public VertexArray(float[] vertices,byte[] indices,float[] textureCordinates) {
		 
		// TODO Auto-generated constructor stub
		 count=indices.length;
		 vao= glGenVertexArrays();
		 glBindVertexArray(vao);
		 
		 vbo=glGenBuffers();
		 glBindBuffer(GL_ARRAY_BUFFER, vbo);
		 glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
		 glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
		 glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);
		 
		 //now for texture
		 tbo=glGenBuffers();
		 glBindBuffer(GL_ARRAY_BUFFER, tbo);
		 glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCordinates), GL_STATIC_DRAW);
		 glVertexAttribPointer(Shader.TCOORD_ATTRIB, 2, GL_FLOAT, false, 0, 0);
		 glEnableVertexAttribArray(Shader.TCOORD_ATTRIB);
		 
		 ibo=glGenBuffers();
		 glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		 glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);
		 
		 glBindBuffer(GL_ARRAY_BUFFER, 0);
		 glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
}
