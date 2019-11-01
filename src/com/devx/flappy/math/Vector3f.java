package com.devx.flappy.math;

public class Vector3f {

	//z component is render order when something is in front of
	public float x,y,z;
	public Vector3f() {
		// TODO Auto-generated constructor stub
		x=0.0f;
		y=0.0f;
		z=0.0f;
	}
	public Vector3f(float x, float y, float z) {
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
}
