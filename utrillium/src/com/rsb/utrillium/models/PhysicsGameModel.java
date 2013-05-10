package com.rsb.utrillium.models;

import com.badlogic.gdx.math.Vector2;
import com.rsb.utrillium.physics.PhysicsMaster;


public abstract  class PhysicsGameModel extends GameModel{

	public PhysicsGameModel(String name, String type){
		this.name=name;
		this.type=type;
	}

	public String name;
	public String type;
	
	/*
	 * Update position based upon physics object position
	 */
	public void updateFromBodyPos(Vector2 bodyPosition, float rotationInRadians) {
		
		PhysicsMaster.convertPhysicsObjectCentreToSpriteCoord(bodyPosition, this.position);
		this.rotationInDegrees = (float) Math.toDegrees(rotationInRadians);
	}
}
