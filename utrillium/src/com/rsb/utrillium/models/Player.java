package com.rsb.utrillium.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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

	public Rectangle bounds = new Rectangle();

	int state = SPAWN;
	float stateTime = 0;
	int dir = LEFT;
	Map map;
	MapProperties mapProps;
	int mapHeight,mapWidth=0;
	boolean grounded = false;
	Body physicsBody;
	
	public Player (Body body, float x, float y) {
		position.x = x;
		position.y = y;
		physicsBody = body;
		bounds.width = 0.6f;
		bounds.height = 0.8f;
		bounds.x = position.x + 0.2f;
		bounds.y = position.y;
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
		
		processInput();

		stateTime += deltaTime;
	}

		
	private void processInput() {
		
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP) ) {
			// move up
			if(this.position.y <= (mapHeight-UTrilliumConst.CY) ) {
				this.position.y+=10;
				this.rotation=90;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
			// move down
			if(this.position.y >= UTrilliumConst.CY ) {
				this.position.y-=10;
				this.rotation=-90;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			// move left
			if(this.position.x >= UTrilliumConst.CX ) {
				this.position.x-=10;
				this.rotation=-180;

			}
		}
		if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			// move right
			if(this.position.x <= (mapWidth-UTrilliumConst.CX) ) {
				this.position.x+=10;
				this.rotation=0;

			} 
		}
		
	}
	

	Rectangle[] r = {new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle()};

	private void tryMove () {
		bounds.x += velocity.x;
		fetchCollidableRects();
		for (int i = 0; i < r.length; i++) {
			Rectangle rect = r[i];
			if (bounds.overlaps(rect)) {
				if (velocity.x < 0)
					bounds.x = rect.x + rect.width + 0.01f;
				else
					bounds.x = rect.x - bounds.width - 0.01f;
				velocity.x = 0;
			}
		}

		bounds.y += velocity.y;
		fetchCollidableRects();
		for (int i = 0; i < r.length; i++) {
			Rectangle rect = r[i];
			if (bounds.overlaps(rect)) {
				if (velocity.y < 0) {
					bounds.y = rect.y + rect.height + 0.01f;
					grounded = true;
					if (state != DYING && state != SPAWN) state = Math.abs(acceleration.x) > 0.1f ? RUN : IDLE;
				} else
					bounds.y = rect.y - bounds.height - 0.01f;
				velocity.y = 0;
			}
		}

		position.x = bounds.x - 0.2f;
		position.y = bounds.y;
	}

	private void fetchCollidableRects () {
		/*int p1x = (int)bounds.x;
		int p1y = (int)Math.floor(bounds.y);
		int p2x = (int)(bounds.x + bounds.width);
		int p2y = (int)Math.floor(bounds.y);
		int p3x = (int)(bounds.x + bounds.width);
		int p3y = (int)(bounds.y + bounds.height);
		int p4x = (int)bounds.x;
		int p4y = (int)(bounds.y + bounds.height);

		int[][] tiles = map.tiles;
		int tile1 = tiles[p1x][map.tiles[0].length - 1 - p1y];
		int tile2 = tiles[p2x][map.tiles[0].length - 1 - p2y];
		int tile3 = tiles[p3x][map.tiles[0].length - 1 - p3y];
		int tile4 = tiles[p4x][map.tiles[0].length - 1 - p4y];

		if (state != DYING && (map.isDeadly(tile1) || map.isDeadly(tile2) || map.isDeadly(tile3) || map.isDeadly(tile4))) {
			state = DYING;
			stateTime = 0;
		}

		if (tile1 == Map.TILE)
			r[0].set(p1x, p1y, 1, 1);
		else
			r[0].set(-1, -1, 0, 0);
		if (tile2 == Map.TILE)
			r[1].set(p2x, p2y, 1, 1);
		else
			r[1].set(-1, -1, 0, 0);
		if (tile3 == Map.TILE)
			r[2].set(p3x, p3y, 1, 1);
		else
			r[2].set(-1, -1, 0, 0);
		if (tile4 == Map.TILE)
			r[3].set(p4x, p4y, 1, 1);
		else
			r[3].set(-1, -1, 0, 0);

		if (map.cube.state == Cube.FIXED) {
			r[4].x = map.cube.bounds.x;
			r[4].y = map.cube.bounds.y;
			r[4].width = map.cube.bounds.width;
			r[4].height = map.cube.bounds.height;
		} else
			r[4].set(-1, -1, 0, 0);
			
			*/
	}

}
