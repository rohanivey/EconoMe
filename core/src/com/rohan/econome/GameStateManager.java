package com.rohan.econome;

import com.badlogic.gdx.Game;

public class GameStateManager extends Game {

	private MainMenuScreen mainMenu;
	private OptionsScreen optionsMenu;

	public GameStateManager() {
		create();
	}

	@Override
	public void create() {
		mainMenu = new MainMenuScreen();
		optionsMenu = new OptionsScreen();

		mainMenu.setGSM(this);
		optionsMenu.setGSM(this);
		setScreen(mainMenu);
	}

	public void setMain() {
		setScreen(mainMenu);
	}

	public void setOptions() {
		setScreen(optionsMenu);
	}

}
