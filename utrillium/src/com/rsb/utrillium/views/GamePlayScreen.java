
package com.rsb.utrillium.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.rsb.physics.MapBodyManager;
import com.rsb.utrillium.models.Player;

public class GamePlayScreen extends BaseGameScreen {
	
	private Player player;
	
	private TiledMap map;
	
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private World world = new World(new Vector2(0,0),true);
	private MapBodyManager mapBodyManager;
	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;

	private boolean renderPhysicsBodies=false;
	private boolean renderMapTiles=true;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	private BitmapFont font;
	private SpriteBatch spriteBatch;
	private Texture planeTexture;
	private Sprite planeSprite;
	
	private float cx;
	private float cy;
	
	private int currentWidth;
	private int currentHeight;
	
	private int mapWidth;
	private int mapHeight;
	
	boolean mapLoaded=false;
	
	public GamePlayScreen (Game game) {
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
		
		font = new BitmapFont (Gdx.files.internal("data/fonts/arial-15.fnt"),Gdx.files.internal("data/fonts/arial-15.png"), false, true);
	
		
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
		mapBodyManager = new MapBodyManager(world, 1.0f, "data/materials.xml", 0);
		mapBodyManager.createPhysics(map, "physics");

		
		 
		debugRenderer=new Box2DDebugRenderer();


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
		
		// let the camera follow the plane		
		moveCameraRelativeToPlayer();
		
		renderMap();
		
		renderPlane();
		
		renderDebugBox2d();
		
		renderCameraCursor();
		
		renderCoordinates();
		
	}

	private void renderDebugBox2d() {
		
		if(renderPhysicsBodies) {
			//Create a copy of camera projection matrix
		    debugMatrix=new Matrix4(camera.combined);
		 
			debugMatrix.scale(1f/1f, 1f/1f, 1f);
			
			spriteBatch.begin();
	        //BoxObjectManager.GetWorld() gets the reference to Box2d World object
			debugRenderer.render(world, debugMatrix);
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

	private void updateUtrillium(float deltaTime) {
		if(deltaTime == 0) return;
		//this.stateTime += deltaTime;
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
