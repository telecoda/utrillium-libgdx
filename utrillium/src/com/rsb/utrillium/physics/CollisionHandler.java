package com.rsb.utrillium.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rsb.utrillium.models.Bullet;
import com.rsb.utrillium.models.GameModelState;
import com.rsb.utrillium.models.PhysicsGameModel;
import com.rsb.utrillium.models.Wall;
import com.rsb.utrillium.views.GamePlayScreen;

public class CollisionHandler implements ContactListener {

	private GamePlayScreen gameScreen;
	
	public CollisionHandler(GamePlayScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Override
	public void beginContact(Contact contact) {

		Fixture f1 = contact.getFixtureA();
		Body b1 = f1.getBody();
		Fixture f2 = contact.getFixtureB();
		Body b2 = f2.getBody();
		PhysicsGameModel userData1 = (PhysicsGameModel) b1.getUserData();
		PhysicsGameModel userData2 = (PhysicsGameModel) b2.getUserData();
		// Depending on type of body we can handle

		if ((userData1 instanceof Wall) && (userData2 instanceof Bullet)) {
			// bullet hits wall
			Gdx.app.log("UTrillium", "bullet hit wall1");
			// destroy bullet
			bulletHasHitWall(b2,(Bullet)userData2);
		}

		if ((userData1 instanceof Bullet) && (userData2 instanceof Wall)) {
			// bullet hits wall
			Gdx.app.log("UTrillium", "bullet hit wall2");
			bulletHasHitWall(b1,(Bullet)userData1);

		}

		/*
		 * if(userData1.GetCollisionGroup()==OBJECT1 &&
		 * userData2.GetCollisionGroup()==OBJECT2)
		 * HandleObject12Collision(userData1,userData2);
		 * if(userData1.GetCollisionGroup()==OBJECT2 &&
		 * userData2.GetCollisionGroup()==OBJECT1)
		 * HandleObject12Collision(userData2,userData1);
		 */
	}

	public void bulletHasHitWall(Body bulletPhysicsBody,Bullet bullet) {
		
		// destroy bullet
		PhysicsMaster.bodiesToDestroy.add(bulletPhysicsBody);
		bullet.setState(GameModelState.DYING);
		gameScreen.removeBullet(bullet);
		// play sound, begin explosion animation etc
		// create explosion for bullet collision
		gameScreen.addBulletExplosion(bullet.position);
		
		
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
