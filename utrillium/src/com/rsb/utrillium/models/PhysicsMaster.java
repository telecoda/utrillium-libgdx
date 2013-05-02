package com.rsb.utrillium.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.rsb.utrillium.UTrilliumConst;

public class PhysicsMaster {

	public static World physicsWorld;
	
	public static float unitsPerPixel = 1.0f/UTrilliumConst.TILE_WIDTH;

	public static void convertPhysicsObjectCentreToSpriteCoord(Vector2 worldPos, Vector2 screenPos) {

		screenPos.x = worldPos.x * UTrilliumConst.TILE_WIDTH;
		screenPos.y = worldPos.y * UTrilliumConst.TILE_WIDTH;
	}
}
