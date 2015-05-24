package com.rohan.econome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rohan.econome.Player.AnimationState;

public class Movement {

	private static final int FRAME_COLS = 4;
	private static final int FRAME_ROWS = 4;

	protected Animation walkLeft;
	protected Animation walkRight;
	protected Animation walkUp;
	protected Animation walkDown;
	protected AnimationState animationState = AnimationState.Left;
	protected float stateTime;
	protected TextureRegion[] animationFrames;
	protected TextureRegion currentFrame;

	protected Texture img;

	public Movement(Texture inputImg) {
		img = inputImg;

		TextureRegion[][] tempFrames = TextureRegion.split(img, img.getWidth()
				/ FRAME_COLS, img.getHeight() / FRAME_ROWS);
		for (int i = 0; i < FRAME_ROWS; i++) {
			animationFrames = new TextureRegion[FRAME_COLS];
			int index = 0;
			for (int j = 0; j < FRAME_COLS; j++) {
				animationFrames[index++] = tempFrames[i][j];
			}
			switch (i) {
			case 0:
				walkUp = new Animation(1f / 9f, animationFrames);
				break;
			case 1:
				walkLeft = new Animation(1f / 9f, animationFrames);
				break;
			case 2:
				walkDown = new Animation(1f / 9f, animationFrames);
				break;
			case 3:
				walkRight = new Animation(1f / 9f, animationFrames);
				break;
			}
		}
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = walkLeft.getKeyFrame(stateTime, true);
	}

	public void handleAnimation() {
		stateTime += Gdx.graphics.getDeltaTime();
		switch (animationState) {
		case Left:
			// System.out.println("State is left");
			currentFrame = walkLeft.getKeyFrame(stateTime, true);
			break;
		case Right:
			// System.out.println("State is right");
			currentFrame = walkRight.getKeyFrame(stateTime, true);
			break;
		case Up:
			// System.out.println("State is up");
			currentFrame = walkUp.getKeyFrame(stateTime, true);
			break;
		case Down:
			// System.out.println("State is down");
			currentFrame = walkDown.getKeyFrame(stateTime, true);
			break;
		}
	}

	public void update() {
		handleAnimation();
	}

	public TextureRegion getFrame() {
		return currentFrame;
	}

}
