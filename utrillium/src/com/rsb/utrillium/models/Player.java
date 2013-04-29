package com.rsb.utrillium.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Transform;
import com.rsb.utrillium.UTrilliumConst;

public class Player extends GameModel {
	static final int IDLE = 0;
	static final int RUN = 1;
	static final int JUMP = 2;
	static final int SPAWN = 3;
	static final int DYING = 4;
	static final int DEAD = 5;
	static final int LEFT = -1;
	static final int RIGHT = 1;
	static final float ACCELERATION = 20f;
	static final float JUMP_VELOCITY = 10;
	static final float GRAVITY = 0.0f;
	static final float MAX_VEL = 6f;
	static final float DAMP = 0.90f;


	int state = SPAWN;
	float stateTime = 0;
	int dir = LEFT;
	Map map;
	MapProperties mapProps;
	int mapHeight,mapWidth=0;
	boolean grounded = false;
	public Body physicsBody;
	
	public Player (Body body, float x, float y) {
		position.x = x;
		position.y = y;
		physicsBody = body;
		this.physicsBody.setAngularDamping(5f);
		this.physicsBody.setLinearDamping(1f);
		state = SPAWN;
		stateTime = 0;
	}

	private void processMapProps(Map map) {
		this.map = map;
		this.mapProps = map.getProperties();
		
		//tilewidth=64, tileheight=64, orientation=orthogonal, height=48, width=48
		int tileWidth = (Integer) mapProps.get("tilewidth");
		int tileHeight = (Integer) mapProps.get("tileheight");
		int width = (Integer) mapProps.get("width");
		int height = (Integer) mapProps.get("height");
		this.mapHeight = height * tileHeight;
		this.mapWidth = width * tileWidth;

	}

	public void update (float deltaTime) {
		
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
		
	}

	private void speedUp(float deltaTime) {
		//physicsBody.setLinearVelocity(new Vector2(100,0));
		Vector2 force=new Vector2(10.0f*deltaTime,0);
		force.rotate((float) Math.toDegrees(this.physicsBody.getTransform().getRotation()));
		this.physicsBody.applyLinearImpulse(force, this.physicsBody.getWorldCenter(),true);

	}

	private void slowDown(float deltaTime) {
		//physicsBody.setLinearVelocity(new Vector2(-100,0));
		Vector2 force=new Vector2(-10.0f*deltaTime,0);
		force.rotate((float) Math.toDegrees(this.physicsBody.getTransform().getRotation()));
		this.physicsBody.applyLinearImpulse(force, this.physicsBody.getWorldCenter(),true);
	}

	private void rotateLeft(float deltaTime) {
		//physicsBody.setLinearVelocity(new Vector2(0,-100));
		this.physicsBody.setAngularVelocity(5f);
//		this.rotation = (float) Math.toDegrees(this.physicsBody.getTransform().getRotation());

	}

	private void rotateRight(float deltaTime) {
		//physicsBody.setLinearVelocity(new Vector2(0,100));
		this.physicsBody.setAngularVelocity(-5f);
//		
	}
	
}
