package com.rsb.utrillium.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.rsb.utrillium.UTrilliumConst;

public class Bullet extends PhysicsGameModel{
	
	public Bullet() {

	}
	public Bullet(Body body, float x, float y) {
		position.x = x;
		position.y = y;
		physicsBody = body;
		
		
	}
	@Override
	public void update(float deltaTime) {		
		
		Vector2 worldPoint = this.physicsBody.getWorldCenter();
/*		this.position.x = worldPoint.x*UTrilliumConst.TILE_WIDTH;
		this.position.x -=(UTrilliumConst.BULLET_WIDTH/2.0f);
		this.position.y = worldPoint.y*UTrilliumConst.TILE_WIDTH;
		this.position.y -= (UTrilliumConst.BULLET_HEIGHT/2.0f);*/
		PhysicsMaster.convertPhysicsObjectCentreToSpriteCoord(worldPoint, this.position);
		this.rotation = (float) Math.toDegrees(this.physicsBody.getTransform().getRotation());

	}

}
