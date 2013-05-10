package com.rsb.utrillium.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.rsb.utrillium.UTrilliumConst;

public class Bullet extends PhysicsGameModel{
	
	
	
	public Bullet(String name, String type) {
		super(name, type);

		this.setState(GameModelState.ACTIVE);
	}

	@Override
	public void update(float deltaTime) {		
		

	}

}
