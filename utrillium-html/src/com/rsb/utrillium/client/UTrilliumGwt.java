package com.rsb.utrillium.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.rsb.utrillium.UTrillium;

public class UTrilliumGwt extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(640, 512);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return new UTrillium();
	}
}

