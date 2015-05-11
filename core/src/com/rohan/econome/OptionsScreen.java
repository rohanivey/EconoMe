package com.rohan.econome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class OptionsScreen implements Screen {

	Stage stage;
	Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	SpriteBatch sb = new SpriteBatch();

	Label volumeLabel = new Label("Volume", skin);
	Slider volumeSlider = new Slider(0, 100, 4, false, skin);
	TextButton backButton = new TextButton("Back", skin);
	GameStateManager gsm;

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		stage.addActor(volumeLabel);
		stage.addActor(volumeSlider);
		stage.addActor(backButton);

		backButton.setX(Gdx.graphics.getWidth() - 64);
		backButton.setY(64);

		volumeLabel.setPosition(Gdx.graphics.getWidth() / 2 + 40,
				Gdx.graphics.getHeight() / 2 + 40);
		volumeSlider.setPosition(volumeLabel.getX() + volumeLabel.getWidth()
				+ 8, volumeLabel.getY());

		backButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int backButton) {
				System.out.println(volumeSlider.getValue());
				gsm.setMain();
				return true;
			}
		});

		stage.setDebugAll(true);

	}

	@Override
	public void render(float delta) {
		sb.begin();
		stage.draw();
		sb.end();

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

	public void setGSM(GameStateManager inputGSM) {
		gsm = inputGSM;
	}

}
