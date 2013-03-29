
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

public class GameScreen extends UTrilliumScreen {
	TiledMap map;
	
	SimpleTileAtlas atlas;
	
	TileMapRenderer renderer;
	
	SpriteBatch spriteBatch = new SpriteBatch();
	
	OrthographicCamera camera = new OrthographicCamera();
	BitmapFont font;
	SpriteBatch batch;
	int[] layers = {0};

	
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
		
		if(mapLoaded) {
			Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			camera.update();
			
			renderer.render(camera,layers);
			batch.begin();
			font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20); 
			batch.end();
		} else {
			Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);			
		}
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			game.setScreen(new MainMenu(game));
		}
	}
	


	@Override
	public void hide () {
		Gdx.app.debug("UTrillium", "dispose game screen");
		//renderer.dispose();
		//controlRenderer.dispose();
	}
	
	
}
