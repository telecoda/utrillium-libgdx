package com.rsb.utrillium;

import com.badlogic.gdx.Game;
import com.rsb.utrillium.views.MainMenu;

public class UTrillium extends Game {
	
	@Override
	public void create () {
		setScreen(new MainMenu(this));
	}

}
