package com.rsb.utrillium.models;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.rsb.utrillium.UTrilliumConst;

public class Player extends PhysicsGameModel {
	

	private Weapon weapon;
	private ArrayList<Bullet> bullets;
	public Player (Body body, float x, float y, ArrayList<Bullet> bullets) {
		position.x = x;
		position.y = y;
		physicsBody = body;
		this.physicsBody.setAngularDamping(5f);
		this.physicsBody.setLinearDamping(1f);
		stateTime = 0;
		this.bullets = bullets;
		
		// attach a weapon to the place
		this.weapon = new Weapon(32f, 0f, this, 0.25f);
		
		
	}


	public void update (float deltaTime) {
		
		weapon.update(deltaTime);
		
		processInput(deltaTime);
		Vector2 worldPoint = this.physicsBody.getWorldCenter();
		this.position.x = worldPoint.x*UTrilliumConst.TILE_WIDTH;
		this.position.y = worldPoint.y*UTrilliumConst.TILE_WIDTH;

		this.rotation = (float) Math.toDegrees(this.physicsBody.getTransform().getRotation());
		stateTime += deltaTime;
	}

		
	private void processInput(float deltaTime) {
		
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP) ) {
			speedUp(deltaTime);
		}
		if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
			slowDown(deltaTime);
		}
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			rotateLeft(deltaTime);
		}
		if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			rotateRight(deltaTime);
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			fireWeapon(deltaTime);
		}
	}

	private void fireWeapon(float deltaTime) {
		Bullet bullet = this.weapon.fire();
		if(bullet!=null) {
			bullets.add(bullet);
		}
	}

	private void speedUp(float deltaTime) {
		Vector2 force=new Vector2(10.0f*deltaTime,0);
		force.rotate((float) Math.toDegrees(this.physicsBody.getTransform().getRotation()));
		this.physicsBody.applyLinearImpulse(force, this.physicsBody.getWorldCenter(),true);

	}

	private void slowDown(float deltaTime) {
		Vector2 force=new Vector2(-10.0f*deltaTime,0);
		force.rotate((float) Math.toDegrees(this.physicsBody.getTransform().getRotation()));
		this.physicsBody.applyLinearImpulse(force, this.physicsBody.getWorldCenter(),true);
	}

	private void rotateLeft(float deltaTime) {
		this.physicsBody.setAngularVelocity(5f);

	}

	private void rotateRight(float deltaTime) {
		this.physicsBody.setAngularVelocity(-5f);
	}
	
}
