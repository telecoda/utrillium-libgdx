
package com.rsb.utrillium.views;

import java.util.ArrayList;
import java.util.Iterator;

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
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.rsb.utrillium.UTrilliumConst;
import com.rsb.utrillium.models.Bullet;
import com.rsb.utrillium.models.PhysicsMaster;
import com.rsb.utrillium.models.Player;
import com.rsb.utrillium.physics.MapBodyManager;

public class GamePlayScreen extends BaseGameScreen {
	
	private Player player;
	
	private TiledMap map;
	
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	//private World world = new World(new Vector2(0,0),true);
	private MapBodyManager mapBodyManager;
	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;

	private boolean renderPhysicsBodies=false;
	private boolean renderMapTiles=true;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	private BitmapFont font = new BitmapFont (Gdx.files.internal("data/fonts/arial-15.fnt"),Gdx.files.internal("data/fonts/arial-15.png"), false, true);

	private SpriteBatch spriteBatch;
	private Texture planeTexture;
	private Sprite planeSprite;
	private Texture bulletTexture;
	private Sprite bulletSprite;
	
	private float cx;
	private float cy;
	
	private int currentScreenWidth;
	private int currentScreenHeight;
	
	private int mapWidth;
	private int mapHeight;
	
	boolean mapLoaded=false;
	
	public GamePlayScreen (Game game) {
		super(game);
		
	}

	@Override
	public void show () {

		// This method is called when screen becomes "current screen"
		// initialise everything for next call of render() method
		initPhysics();
		
		initCamera();
				
		initCurrentLevelMap();

		initPlayer();
		// load sprites
		initSprites();
	}

	private void initPhysics() {
		// if world already exists detroy if first
		if(PhysicsMaster.physicsWorld != null) {
			PhysicsMaster.physicsWorld.dispose();
		}
		PhysicsMaster.physicsWorld = new World(new Vector2(0,0),true);
	}
	
	private void initCamera() {
		updateScreenDimensions();

		camera = new OrthographicCamera();		
		camera.setToOrtho(false, currentScreenWidth, currentScreenHeight);
		camera.update();
	}

	private void initCurrentLevelMap() {
		Gdx.app.debug("UTrillium", "loading map");

		map = new TmxMapLoader().load("data/level01.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1f);
		
		if(map == null) {
			String errorMsg = "Failed to load map ";
			Gdx.app.error("UTrillium.GameScreen", errorMsg);
		}
		
		// create Box2D physics world
		mapBodyManager = new MapBodyManager(PhysicsMaster.physicsWorld, 1/UTrilliumConst.TILE_WIDTH, "data/materials.xml", 0);
		mapBodyManager.createPhysics(map, "physics");
		
		debugRenderer=new Box2DDebugRenderer();
		
		this.mapLoaded = true;

		Gdx.app.debug("UTrillium", "map loaded");

	}
	
	private void initPlayer() {
		
		// get reference to physics body for player
		Iterator<Body> bodies = PhysicsMaster.physicsWorld.getBodies();
		
		while(bodies.hasNext()){
			Body body = bodies.next();
			// find mainPlayer body
			MapProperties properties = (MapProperties)body.getUserData();
			
			if(properties!=null) {
				String propertyName = properties.get("name", "no name", String.class);
				if(propertyName.equals("mainPlayer")) {
					// found main player!
					int x = (Integer) properties.get("x");
					int y= (Integer) properties.get("y");

					player = new Player(body,x+32,y+32,bullets);
					return;

				}
			}
			
		}
	
		throw new RuntimeException("mainPlayer definition not found in map! Cannot continue.");

	}

	private void initSprites() {
		planeTexture = new Texture(Gdx.files.internal("data/sprites/plane.png")); 
		planeSprite = new Sprite(planeTexture);

		bulletTexture = new Texture(Gdx.files.internal("data/sprites/bullet.png")); 
		bulletSprite = new Sprite(bulletTexture);
	}

	
	@Override
	public void render (float delta) {
		
		updateScreenDimensions();
		
		updateGameObjects(delta);
		
		processInput();
		
		renderScreenObjects(delta);
		
	}

	private void renderScreenObjects(float delta) {
		// clear the screen
		Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
		
		// let the camera follow the plane		
		moveCameraRelativeToPlayer();
		
		renderMap();
		
		renderBullets();

		renderPlane();


		renderDebugBox2d();
		
		renderCameraCursor();
		
		renderDebugInfo(delta);
	}

	private void renderBullets() {

		spriteBatch = renderer.getSpriteBatch();
		spriteBatch.begin();
		
		for (Bullet bullet : bullets) {

			bulletSprite.setPosition(bullet.position.x-UTrilliumConst.BULLET_WIDTH/2.0f, bullet.position.y-UTrilliumConst.BULLET_HEIGHT/2.0f);
			bulletSprite.setRotation(bullet.rotation);
			bulletSprite.draw(spriteBatch);

		}
		
		
		spriteBatch.end();
		
	}

	private void updateGameObjects(float delta) {

		PhysicsMaster.physicsWorld.step(delta, 6, 2);
		
		// update the Utrillium game objects (process input, collision detection, position update)
		player.update(delta);
		
		// update bullets
		for (Bullet bullet : bullets) {

			bullet.update(delta);
		} 
	}

	private void updateScreenDimensions() {
		currentScreenWidth = Gdx.graphics.getWidth();
		currentScreenHeight = Gdx.graphics.getHeight();
		
		cx = currentScreenWidth/2;
		cy = currentScreenHeight/2;
	}

	private void renderDebugBox2d() {
		
		if(renderPhysicsBodies) {
			//Create a copy of camera projection matrix
		    debugMatrix=new Matrix4(camera.combined);
		 
			debugMatrix.scale(UTrilliumConst.TILE_WIDTH, UTrilliumConst.TILE_WIDTH, 1f);
			
			spriteBatch.begin();
	        //BoxObjectManager.GetWorld() gets the reference to Box2d World object
			debugRenderer.render(PhysicsMaster.physicsWorld, debugMatrix);
			spriteBatch.end();
		}
	}

	private void renderMap() {
		
		if(renderMapTiles) {
			
			renderer.render();
		}
	}

	
	private void moveCameraRelativeToPlayer() {
		camera.position.x = player.position.x;
		camera.position.y = player.position.y;
		camera.update();
		// set the tile map render view based on what the
		// camera sees and render the map
		renderer.setView(camera);
	}
	
	
	private void processInput() {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			game.setScreen(new MainMenu(game));
		}
		
		// toggle map rendering
		if (Gdx.input.isKeyPressed(Keys.M)) {
			renderMapTiles = !renderMapTiles;
		}

		// toggle physics bodies rendering
		if (Gdx.input.isKeyPressed(Keys.B)) {
			renderPhysicsBodies = !renderPhysicsBodies;
		}

	}


	private void renderPlane() {

		spriteBatch = renderer.getSpriteBatch();
		spriteBatch.begin();
		
		planeSprite.setPosition(camera.position.x-planeTexture.getWidth()/2, camera.position.y-planeTexture.getHeight()/2);
		planeSprite.setRotation(player.rotation);
		planeSprite.draw(spriteBatch);
		
		
		spriteBatch.end();
	}
	
	private void renderDebugInfo(float delta) {
		spriteBatch = renderer.getSpriteBatch();
		int fontX = (int) (camera.position.x - cx);
		int fontY = (int) (camera.position.y + cy);
		spriteBatch.begin();
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), fontX+10, fontY-20); 
		font.draw(spriteBatch, "Camera x: " + camera.position.x +" , Camera y: "+ camera.position.y+ " mapWidth: "+ mapWidth + " mapHeight: " +mapHeight+ " mapBodies:" + PhysicsMaster.physicsWorld.getBodyCount(), fontX+10, fontY-40); 
		font.draw(spriteBatch, "cx: " + cx +" , cy: "+ cy + " screenWidth: "+ currentScreenWidth + " screenHeight: " +currentScreenHeight + " delta:" + delta, fontX+10, fontY-60); 
		font.draw(spriteBatch, "playerX: " + player.position.x +" , playerY: "+ player.position.y + " playerBodyX: "+ player.physicsBody.getPosition().x + " playerBodyY: " +player.physicsBody.getPosition().y, fontX+10, fontY-80); 
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
		mapBodyManager.destroyPhysics();
	}
	
	
}
