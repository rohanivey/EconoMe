package com.rohan.econome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DustCloud extends Actor {
	private Texture img = new Texture(Gdx.files.internal("dust.png"));

	enum Direction {
		Left, Right
	}

	float velocity;
	Vector2 location;
	float timer = 1f;

	DustCloud(String inputString, float inputX, float inputY) {
		switch (inputString) {
		case "Left":
			velocity = -0.2f;
			break;
		case "Right":
			velocity = 0.5f;
			break;
		default:
			break;

		}

		location = new Vector2(inputX, inputY);
		System.out.println("Made dust cloud");
	}

	@Override
	public float getX() {
		return location.x;
	}

	@Override
	public float getY() {
		return location.y;
	}

	public Texture getTexture() {
		return img;
	}

	public void update() {
		timer -= 0.1f;
		updateLocation();
	}

	public float getTimer() {
		return timer;
	}

	public void updateLocation() {
		location.x += velocity;
	}

	public void draw() {

	}
}
