package com.rohan.econome;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AssetManager {

	private static SpriteBatch sb;

	private static Jukebox jukebox;

	public AssetManager() {
		sb = new SpriteBatch();
		jukebox = new Jukebox();
	}

	public SpriteBatch getSB() {
		return sb;
	}

	public Jukebox getJukebox() {
		return jukebox;
	}

}
