package com.rsb.utrillium;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class UTrilliumDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "utrillium";
		cfg.useGL20 = false;
		cfg.width = 640;
		cfg.height = 512;
		
		new LwjglApplication(new UTrillium(), cfg);
	}
}
