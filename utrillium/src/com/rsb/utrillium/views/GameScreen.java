
package com.rsb.utrillium.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.rsb.utrillium.models.Player;

public class GameScreen extends UTrilliumScreen {
	
	private Player player;
	
	private TiledMap map;
	
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private World world = new World(new Vector2(0,0),true);
	//private MapBodyManager mapBodyManager;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	private BitmapFont font;
	private SpriteBatch spriteBatch;
	private Texture planeTexture;
	private Sprite planeSprite;
	
	private int[] layers = {0};

	private float cx;
	private float cy;
	
	private int currentWidth;
	private int currentHeight;
	
	private int mapWidth;
	private int mapHeight;
	
	private float stateTime = 0;
	
	boolean mapLoaded=false;
	
	public GameScreen (Game game) {
		super(game);
		
	}

	@Override
	public void show () {

		Gdx.app.debug("UTrillium", "loading map");

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// create an orthographic camera, shows us 30x20 units of the world
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();
		
		font = new BitmapFont();
	
		
		// load the map, set the unit scale to 1/16 (1 unit == 64 pixels)
		map = new TmxMapLoader().load("data/level01.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / 1f);
		
		// init player
		player = new Player(map,128+32,128+32);
				
		
		if(map == null) {
			String errorMsg = "Failed to load map ";
			Gdx.app.error("UTrillium.GameScreen", errorMsg);
		}
		
		
		// create Box2D physics world
		//mapBodyManager = new MapBodyManager(world, 1.0f, 0);
		//mapBodyManager.createPhysics(map, 1);



		Gdx.app.debug("UTrillium", "map loaded");
		
		// load sprites
		planeTexture = new Texture(Gdx.files.internal("data/sprites/plane.png")); 

		planeSprite = new Sprite(planeTexture);
		this.mapLoaded = true;
	}

	
	@Override
	public void render (float delta) {
		
		currentWidth = Gdx.graphics.getWidth();
		currentHeight = Gdx.graphics.getHeight();
		
		//mapWidth = renderer.getMapWidthUnits();
		//mapHeight = renderer.getMapHeightUnits();
		
		cx = currentWidth/2;
		cy = currentHeight/2;
		
		//world.step(delta, 1, 1);
		
		
		processInput();
				
		// clear the screen
		Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// get the delta time
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		// update the Utrillium game objects (process input, collision detection, position update)
		player.update(deltaTime);
		updateUtrillium(deltaTime);
		
		renderMap();
		
		
		renderPlane();
		
		renderCameraCursor();
		
		renderCoordinates();
		
	}

	private void renderMap() {
		// let the camera follow the plane
		
		camera.position.x = player.position.x;
		camera.position.y = player.position.y;
		camera.update();
		
		// set the tile map rendere view based on what the
		// camera sees and render the map
		renderer.setView(camera);
		renderer.render();
	}

	private void updateUtrillium(float deltaTime) {
		if(deltaTime == 0) return;
		this.stateTime += deltaTime;
	}
	
	private void processInput() {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			game.setScreen(new MainMenu(game));
		}
		
	}

		
		/*
		// check input and apply to velocity & state
		if((Gdx.input.isKeyPressed(Keys.SPACE) || isTouched(0.75f, 1)) && koala.grounded) {
			koala.velocity.y += Koala.JUMP_VELOCITY;
			koala.state = Koala.State.Jumping;
			koala.grounded = false;
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A) || isTouched(0, 0.25f)) {
			koala.velocity.x = -Koala.MAX_VELOCITY;
			if(koala.grounded) koala.state = Koala.State.Walking;
			koala.facesRight = false;
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D) || isTouched(0.25f, 0.5f)) {
			koala.velocity.x = Koala.MAX_VELOCITY;
			if(koala.grounded) koala.state = Koala.State.Walking;
			koala.facesRight = true;
		}
		
		// apply gravity if we are falling
		koala.velocity.add(0, GRAVITY);
		
		// clamp the velocity to the maximum, x-axis only
		if(Math.abs(koala.velocity.x) > Koala.MAX_VELOCITY) {
			koala.velocity.x = Math.signum(koala.velocity.x) * Koala.MAX_VELOCITY;
		}
		
		// clamp the velocity to 0 if it's < 1, and set the state to standign
		if(Math.abs(koala.velocity.x) < 1) {
			koala.velocity.x = 0;
			if(koala.grounded) koala.state = Koala.State.Standing;
		}
		
		// multiply by delta time so we know how far we go
		// in this frame
		koala.velocity.scl(deltaTime);
		
		// perform collision detection & response, on each axis, separately
		// if the koala is moving right, check the tiles to the right of it's
		// right bounding box edge, otherwise check the ones to the left
		Rectangle koalaRect = rectPool.obtain();
		koalaRect.set(koala.position.x, koala.position.y, Koala.WIDTH, Koala.HEIGHT);
		int startX, startY, endX, endY;
		if(koala.velocity.x > 0) {
			startX = endX = (int)(koala.position.x + Koala.WIDTH + koala.velocity.x);
		} else {
			startX = endX = (int)(koala.position.x + koala.velocity.x);
		}
		startY = (int)(koala.position.y);
		endY = (int)(koala.position.y + Koala.HEIGHT);
		getTiles(startX, startY, endX, endY, tiles);
		koalaRect.x += koala.velocity.x;
		for(Rectangle tile: tiles) {
			if(koalaRect.overlaps(tile)) {
				koala.velocity.x = 0;
				break;
			}
		}
		koalaRect.x = koala.position.x;
		
		// if the koala is moving upwards, check the tiles to the top of it's
		// top bounding box edge, otherwise check the ones to the bottom
		if(koala.velocity.y > 0) {
			startY = endY = (int)(koala.position.y + Koala.HEIGHT + koala.velocity.y);
		} else {
			startY = endY = (int)(koala.position.y + koala.velocity.y);
		}
		startX = (int)(koala.position.x);
		endX = (int)(koala.position.x + Koala.WIDTH);
		getTiles(startX, startY, endX, endY, tiles);
		koalaRect.y += koala.velocity.y;
		for(Rectangle tile: tiles) {
			if(koalaRect.overlaps(tile)) {
				// we actually reset the koala y-position here
				// so it is just below/above the tile we collided with
				// this removes bouncing :)
				if(koala.velocity.y > 0) {
					koala.position.y = tile.y - Koala.HEIGHT;
					// we hit a block jumping upwards, let's destroy it!
					TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(1);
					layer.setCell((int)tile.x, (int)tile.y, null);
				} else {
					koala.position.y = tile.y + tile.height;
					// if we hit the ground, mark us as grounded so we can jump
					koala.grounded = true;
				}
				koala.velocity.y = 0;
				break;
			}
		}
		rectPool.free(koalaRect);
		
		// unscale the velocity by the inverse delta time and set 
		// the latest position
		koala.position.add(koala.velocity);
		koala.velocity.scl(1/deltaTime);
		
		// Apply damping to the velocity on the x-axis so we don't
		// walk infinitely once a key was pressed
		koala.velocity.x *= Koala.DAMPING;
		
	}

	*/

	private void renderPlane() {

		spriteBatch = renderer.getSpriteBatch();
		spriteBatch.begin();
		
		planeSprite.setPosition(camera.position.x-planeTexture.getWidth()/2, camera.position.y-planeTexture.getHeight()/2);
		planeSprite.setRotation(player.rotation);
		planeSprite.draw(spriteBatch);
		
		
		spriteBatch.end();
	}
	
	private void renderCoordinates() {
		spriteBatch = renderer.getSpriteBatch();
		int fontX = (int) (camera.position.x - cx);
		int fontY = (int) (camera.position.y + cy);
//		spriteBatch.setTransformMatrix(camera.combined);
		spriteBatch.begin();
		//spriteBatch.setProjectionMatrix(camera.combined);
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), fontX+10, fontY-20); 
		font.draw(spriteBatch, "Camera x: " + camera.position.x +" , Camera y: "+ camera.position.y+ " mapWidth: "+ mapWidth + " mapHeight: " +mapHeight, fontX+10, fontY-40); 
		font.draw(spriteBatch, "cx: " + cx +" , cy: "+ cy + " screenWidth: "+ currentWidth + " screenHeight: " +currentHeight, fontX+10, fontY-60); 
		spriteBatch.end();
	}

	private void renderCameraCursor() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.line(camera.position.x, camera.position.y, camera.position.x+10, camera.position.y);
		shapeRenderer.line(camera.position.x, camera.position.y, camera.position.x-10, camera.position.y);
		shapeRenderer.line(camera.position.x, camera.position.y, camera.position.x, camera.position.y+10);
		shapeRenderer.line(camera.position.x, camera.position.y, camera.position.x, camera.position.y-10);
		shapeRenderer.end();
	}
	


	@Override
	public void hide () {
		Gdx.app.debug("UTrillium", "dispose game screen");
		renderer.dispose();
		shapeRenderer.dispose();
		//mapBodyManager.destroyPhysics();
	}
	
	
}
