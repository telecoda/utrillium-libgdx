package com.rsb.utrillium.physics;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.rsb.utrillium.UTrilliumConst;
import com.rsb.utrillium.models.PhysicsGameModel;

public class PhysicsMaster {

	public static World physicsWorld;
	
	public static float unitsPerPixel = 1.0f/UTrilliumConst.TILE_WIDTH;
	
	public static ArrayList<Body> bodiesToDestroy;
	
	public static void convertPhysicsObjectCentreToSpriteCoord(Vector2 worldPos, Vector2 screenPos) {

		screenPos.x = worldPos.x * UTrilliumConst.TILE_WIDTH;
		screenPos.y = worldPos.y * UTrilliumConst.TILE_WIDTH;
	}
	
	public static Shape getRectangle(float x, float y, float width, float height) {
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((x + (width * 0.5f)) * PhysicsMaster.unitsPerPixel,
								   (y + (height * 0.5f) ) * PhysicsMaster.unitsPerPixel);
		polygon.setAsBox((width * 0.5f) * PhysicsMaster.unitsPerPixel,
						 (height * 0.5f) * PhysicsMaster.unitsPerPixel,
						 size,
						 0.0f);
		return polygon;
	}

	public static void updateGameObjectPositions() {
		Iterator<Body> bodies = physicsWorld.getBodies();
		
		while(bodies.hasNext()) {
			Body body = bodies.next();
			PhysicsGameModel gameModelObject = (PhysicsGameModel)body.getUserData();
			
			if(gameModelObject != null) {
				// update position
				gameModelObject.updateFromBodyPos(body.getWorldCenter(),body.getTransform().getRotation());
			}
			
		}
		
	}
	
	  
 	public static void update(float delta) {

 		// init bodies to destroy
 		bodiesToDestroy = new ArrayList<Body>();
 		
		physicsWorld.step(delta, 6, 2);
		
		// destroy unwanted bodies
		for(Body body : bodiesToDestroy) {
			PhysicsMaster.physicsWorld.destroyBody(body);
		}

		
		updateGameObjectPositions();
		
	}
}
