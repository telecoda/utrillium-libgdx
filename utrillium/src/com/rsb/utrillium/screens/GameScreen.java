
package com.rsb.utrillium.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.SimpleTileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameScreen extends UTrilliumScreen {
	
	private TiledMap map;
	
	private SimpleTileAtlas atlas;
	
	private TileMapRenderer renderer;
	
	private SpriteBatch spriteBatch = new SpriteBatch();
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	private OrthographicCamera camera = new OrthographicCamera();
	private BitmapFont font;
	private SpriteBatch batch;
	private int[] layers = {0};

	private float cx;
	private float cy;
	
	private int currentWidth;
	private int currentHeight;
	
	private int mapWidth;
	private int mapHeight;
	
	boolean mapLoaded=false;
	
	public GameScreen (Game game) {
		super(game);
		
	}

	@Override
	public void show () {

		Gdx.app.debug("UTrillium", "loading map");

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * 512, 512);
		camera.update();

		
		font = new BitmapFont();
		batch = new SpriteBatch();

		// load map
		String levelFile = "data/level01.tmx";
		map = TiledLoader.createMap(Gdx.files.internal(levelFile));
		if(map == null) {
			String errorMsg = String.format("Failed to load map %s", levelFile);
			Gdx.app.error("UTrillium.GameScreen", errorMsg);
		}
		
		// load atlas
		atlas = new SimpleTileAtlas(map, Gdx.files.internal("data"));
		renderer = new TileMapRenderer(map, atlas, 32, 32);

		Gdx.app.debug("UTrillium", "map loaded");

		this.mapLoaded = true;
	}

	
	@Override
	public void render (float delta) {
		
		currentWidth = Gdx.graphics.getWidth();
		currentHeight = Gdx.graphics.getHeight();
		
		mapWidth = renderer.getMapWidthUnits();
		mapHeight = renderer.getMapHeightUnits();
		
		cx = currentWidth/2;
		cy = currentHeight/2;
		
		processInput();
		
		if(mapLoaded) {
			Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			camera.update();
			
			renderer.render(camera,layers);
			renderCoordinates();
			
			renderCameraCursor();
			
		} else {
			Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);			
		}
		
		
	}

	private void processInput() {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			game.setScreen(new MainMenu(game));
		}
		
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP) ) {
			// move up
			if(camera.position.y <= (mapHeight-cy) ) {
				camera.position.y+=10;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
			// move down
			if(camera.position.y >= cy ) {
				camera.position.y-=10;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			// move left
			if(camera.position.x >= cx ) {
				camera.position.x-=10;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			// move right
			if(camera.position.x <= (mapWidth-cx) ) {
				camera.position.x+=10;
			} 
		}
	}

	private void renderCoordinates() {
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20); 
		font.draw(batch, "Camera x: " + camera.position.x +" , Camera y: "+ camera.position.y+ " mapWidth: "+ mapWidth + " mapHeight: " +mapHeight, 10, 50); 
		font.draw(batch, "cx: " + cx +" , cy: "+ cy + " screenWidth: "+ currentWidth + " screenHeight: " +currentHeight, 10, 70); 
		batch.end();
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
		//renderer.dispose();
		//controlRenderer.dispose();
	}
	
	
}
