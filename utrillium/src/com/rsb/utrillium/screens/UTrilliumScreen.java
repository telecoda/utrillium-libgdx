
package com.rsb.utrillium.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class UTrilliumScreen implements Screen {
	Game game;

	public UTrilliumScreen (Game game) {
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
