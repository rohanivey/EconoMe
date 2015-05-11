package com.rohan.econome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class CharacterCreationState implements Screen {

	private enum Process {
		makeName, makeStats, makeFeats, makeSprite, makeOverview
	}

	private SpriteBatch sb;
	private Player tempPlayer;
	private Stage stage;
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	private Jukebox jukebox;
	private GameStateManager gsm;

	private Process currentPage;

	private TextField firstNameTextField;
	private TextField secondNameTextField;
	private TextButton nextButton;
	private TextButton backButton;

	private String tempFirst, tempSecond;

	private int tempStr, tempWis, tempInt, tempAgi, tempVit, pointsToSpend;

	private TextButton minusStr, plusStr, minusWis, plusWis, minusInt, plusInt,
			minusAgi, plusAgi, minusVit, plusVit;

	private Label pointsToSpendText, str, wis, intel, agi, vit, pointsText,
			strText, wisText, intelText, agiText, vitText;

	private FeatManager tempFeatManager;

	private Image featImage;
	private Label featDescription, featRequirement;
	private Feat currentFeat;

	private Sprite hairSprite;
	private TextureRegion[][] tempHairs;
	private int hairChoice = 0;
	private TextButton minusHair, plusHair, minusEyes, plusEyes, minusMouth,
			plusMouth;
	private Image hairImage, eyeImage, mouthImage;

	@Override
	public void show() {
		sb = gsm.getAM().getSB();
		jukebox = gsm.getAM().getJukebox();

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

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

}
