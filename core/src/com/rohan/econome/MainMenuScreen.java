package com.rohan.econome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

public class MainMenuScreen implements Screen {

	SpriteBatch sb = new SpriteBatch();

	Stage stage;
	Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	TextButton playButton = new TextButton("Play", skin);
	TextButton loadButton = new TextButton("Load", skin);
	TextButton optionsButton = new TextButton("Options", skin);
	TextButton exitButton = new TextButton("Exit", skin);
	Array<Button> buttonList = new Array<Button>();
	GameStateManager gsm;

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		stage.addActor(playButton);
		stage.addActor(loadButton);
		stage.addActor(optionsButton);
		stage.addActor(exitButton);

		buttonList.add(playButton);
		buttonList.add(loadButton);
		buttonList.add(optionsButton);
		buttonList.add(exitButton);

		int i = 0;

		for (Button b : buttonList) {
			b.setX(Gdx.graphics.getWidth() * 0.4f);
			b.setY(Gdx.graphics.getHeight() * 0.6f - i * 32);
			b.setBounds(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			b.setDebug(true);
			i++;
			System.out.println("X: " + b.getX() + " Y: " + b.getY()
					+ " Width: " + b.getWidth() + " Height: " + b.getHeight());
		}

		optionsButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int optionsButton) {
				System.out.println("Button clicked");
				gsm.setOptions();
				return true;
			}
		});

		optionsButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int optionsButton) {
				System.out.println("Honk");
				// else play buzz or something
				return true;
			}
		});

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
		stage.dispose();

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void setGSM(GameStateManager inputGSM) {
		gsm = inputGSM;
	}

}
