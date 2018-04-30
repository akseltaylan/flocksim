package algorithm;
import javax.media.j3d.*;
import javax.vecmath.*;

import shapes.Polygon;

/*
 * Boids are an abstract structure that represent the birds
 * or objects represented in the scene during the simulation.
 */

public class Boid {
	
	private Polygon shape;
	private Vector2f location;
	private Vector2f velocity;
	public TransformGroup displayGroup;
	public Transform3D display;
	protected double speedLimit = 0.05;
	
	public Boid (Vector2f loc, Polygon sh) {
		velocity = new Vector2f();
		velocity.x = 0.02f;
		velocity.y = 0.02f;
		location = loc;
		shape = sh;
	}
	
	public Boid (Vector2f loc, Polygon sh, double speed) {
		velocity = new Vector2f();
		velocity.x = (float) speed - ((float) speed / 2f);
		velocity.y = (float) speed - ((float) speed / 2f);
		speedLimit = speed;
		location = loc;
		shape = sh;
	}
	
	public void setVelocity(Vector2f vel) {
		if ( vel.x > speedLimit) {
			vel.x = (float) speedLimit;
		}
		if ( vel.y > speedLimit) {
			vel.y = (float) speedLimit;
		} 
		
		this.velocity = vel;
	}
	
	public void setLocation(Vector2f pos) {
		location = pos;
	}
	
	public Vector2f getLocation() {
		return location;
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}
	
	public Polygon getPoly() {
		return shape;
	}
	
	public void setPoly(Polygon p) {
		shape = p;
	}
	
	public Transform3D getDisplay() {
		return display;
	}
}
