package com.rsb.utrillium.models;

import java.util.Iterator;

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

public class PhysicsMaster implements ContactListener{

	public static World physicsWorld;
	
	public static float unitsPerPixel = 1.0f/UTrilliumConst.TILE_WIDTH;

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

	public static void updateGameObjects() {
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
	
	@Override
	public void beginContact(Contact contact) {
        // TODO Auto-generated method stub
       /*
       Fixture f1=contact.getFixtureA();
       Body b1=f1.getBody();
       Fixture f2=contact.getFixtureB();
       Body b2=f2.getBody();
       BoxUserData userData1 = (BoxUserData) b1.getUserData();
       BoxUserData userData2 = (BoxUserData) b2.getUserData();
       //Depending on type of body we can handle 
      if(userData1.GetCollisionGroup()==OBJECT1  && userData2.GetCollisionGroup()==OBJECT2)
                HandleObject12Collision(userData1,userData2);
      if(userData1.GetCollisionGroup()==OBJECT2  && userData2.GetCollisionGroup()==OBJECT1)
                HandleObject12Collision(userData2,userData1);
      
      */
 }

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
}
