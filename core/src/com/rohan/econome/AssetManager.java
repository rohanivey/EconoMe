package com.rohan.econome;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AssetManager {

	private static SpriteBatch sb;
	private static Jukebox jukebox;
	private static GameStateManager gsm;

	public AssetManager(GameStateManager inputGSM) {
		sb = new SpriteBatch();
		jukebox = new Jukebox();
		gsm = inputGSM;

	}

	public SpriteBatch getSB() {
		sb = new SpriteBatch();
		return sb;
	}

	public Jukebox getJukebox() {
		return jukebox;
	}

	public static GameStateManager getGSM() {
		return gsm;
	}

}
