package com.rohan.econome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class PlayScreen implements Screen {

	private SpriteBatch sb;
	private Jukebox jukebox;
	private Stage stage;
	private GameStateManager gsm;
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

	private Level level;

	public PlayScreen(String inputLevel) {

		// Super spooky
		gsm = AssetManager.getGSM();

		setSb(gsm.getAM().getSB());
		jukebox = gsm.getAM().getJukebox();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		level = new Level(this, inputLevel);

	}

	@Override
	public void show() {

		setSb(gsm.getAM().getSB());
		jukebox = gsm.getAM().getJukebox();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		stage.addActor(level);

	}

	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Camera getCam() {
		return stage.getCamera();
	}

	public SpriteBatch getSb() {
		return sb;
	}

	public void setSb(SpriteBatch sb) {
		this.sb = sb;
	}
}
