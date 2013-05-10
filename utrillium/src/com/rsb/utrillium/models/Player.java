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
	public Player (ArrayList<Bullet> bullets, Body playerBody) {
		super("mainPlayer", "Player");
		
		setState(GameModelState.ACTIVE);
		
		this.bullets = bullets;
		
		// attach a weapon to the place
		this.weapon = new Weapon(32f, 0f, playerBody, 0.25f);
		
		
	}


	public void update (float deltaTime) {
		
		weapon.update(deltaTime);
		
		processInput(deltaTime);

		stateTime += deltaTime;
	}

		
	private void processInput(float deltaTime) {
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

	
	
}
