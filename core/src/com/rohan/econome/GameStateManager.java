package com.rohan.econome;

import com.badlogic.gdx.Game;

public class GameStateManager extends Game {

	private MainMenuScreen mainMenu;
	private LoadScreen loadMenu;
	private OptionsScreen optionsMenu;
	private CharacterCreationState characterCreationMenu;

	private AssetManager am;

	public GameStateManager() {
		create();
	}

	@Override
	public void create() {
		mainMenu = new MainMenuScreen();
		loadMenu = new LoadScreen();
		optionsMenu = new OptionsScreen();
		characterCreationMenu = new CharacterCreationState();
		am = new AssetManager(this);

		mainMenu.setGSM(this);
		loadMenu.setGSM(this);
		optionsMenu.setGSM(this);
		characterCreationMenu.setGSM(this);
		setScreen(mainMenu);
	}

	public void setMain() {
		setScreen(mainMenu);
	}

	public void setCharacterCreation() {
		setScreen(characterCreationMenu);
	}

	public void setLoad() {
		setScreen(loadMenu);
	}

	public void setOptions() {
		setScreen(optionsMenu);
	}

	public AssetManager getAM() {
		return am;
	}

}
