package com.rohan.econome;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class EconoMe extends ApplicationAdapter {

	GameStateManager gsm;

	@Override
	public void create() {
		gsm = new GameStateManager();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.render();
	}
}
