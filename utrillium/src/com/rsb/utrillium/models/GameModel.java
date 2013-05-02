package com.rsb.utrillium.models;

import com.badlogic.gdx.math.Vector2;

public abstract class GameModel {
	
	/*
	 * Update method called on object every cycle
	 * - if the object also reacts to input this is where it should be handled
	 */
	public abstract void update (float deltaTime);

	public Vector2 position = new Vector2();
	//public Vector2 acceleration = new Vector2();
	//public Vector2 velocity = new Vector2();
	public float rotation=0;
	
	public float stateTime;

}
