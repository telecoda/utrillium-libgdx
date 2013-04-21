
package com.rsb.utrillium.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class BaseGameScreen implements Screen {
	Game game;

	public BaseGameScreen (Game game) {
		this.game = game;
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
