package com.rohan.econome;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CharacterCreationState implements Screen {

	private enum Process {
		makeName, makeStats, makeFeats, makeSprite, makeOverview
	}

	private SpriteBatch sb;
	private Player tempPlayer;
	private Stage stage;
	private Table mainTable;
	private Skin skin = new Skin(
			Gdx.files.internal("Character Creation/uiskin.json"));
	private Jukebox jukebox;
	private GameStateManager gsm;

	private Process currentPage;

	private TextField firstNameTextField;
	private TextField secondNameTextField;
	private TextButton nextButton;
	private TextButton backButton;

	private String tempFirst, tempSecond;

	private int tempStr, tempWis, tempInt, tempAgi, tempVit, pointsToSpend;

	private Button plusStr, plusWis, plusInt, plusAgi, plusVit, minusStr,
			minusWis, minusInt, minusAgi, minusVit;

	private Label pointsToSpendText, str, wis, intel, agi, vit, pointsText,
			strText, wisText, intelText, agiText, vitText;

	private FeatManager tempFeatManager;
	private ScrollPane scroll;
	private Image featImage;
	private Label featDescription, featRequirement;
	private Feat currentFeat;

	private Sprite hairSprite;
	private TextureRegion[][] tempHairs;
	private int hairChoice = 0;
	Button minusHair, plusHair, minusEyes, plusEyes, minusMouth, plusMouth;
	private Image hairImage, eyeImage, mouthImage;

	ArrayList<Actor> actorList;

	@Override
	public void show() {
		sb = gsm.getAM().getSB();
		jukebox = gsm.getAM().getJukebox();

		currentPage = Process.makeName;
		tempPlayer = new Player();
		pointsToSpend = 6;

		makeNameSetup();

	}

	@Override
	public void render(float delta) {
		stage.act(delta);
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

	public void setGSM(GameStateManager inputGSM) {
		gsm = inputGSM;
	}

	public void assignStatArrowButtons() {
		plusStr.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusStr) {
				if (pointsToSpend > 0) {
					tempStr += 1;
					System.out.println(tempStr);
					pointsToSpend -= 1;
					str.setText(String.valueOf(tempStr));
					pointsToSpendText.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		minusStr.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusStr) {
				if (tempStr > 0) {
					tempStr -= 1;
					pointsToSpend += 1;
					str.setText(String.valueOf(tempStr));
					pointsToSpendText.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});
		plusWis.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusWis) {
				if (pointsToSpend > 0) {
					tempWis += 1;
					System.out.println(tempWis);
					pointsToSpend -= 1;
					wis.setText(String.valueOf(tempWis));
					pointsToSpendText.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		minusWis.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusWis) {
				if (tempWis > 0) {
					tempWis -= 1;
					pointsToSpend += 1;
					wis.setText(String.valueOf(tempWis));
					pointsToSpendText.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});
		plusInt.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusInt) {
				if (pointsToSpend > 0) {
					tempInt += 1;
					System.out.println(tempInt);
					pointsToSpend -= 1;
					intel.setText(String.valueOf(tempInt));
					pointsToSpendText.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		minusInt.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusInt) {
				if (tempInt > 0) {
					tempInt -= 1;
					pointsToSpend += 1;
					intel.setText(String.valueOf(tempInt));
					pointsToSpendText.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		plusAgi.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusAgi) {
				if (pointsToSpend > 0) {
					tempAgi += 1;
					System.out.println(tempAgi);
					pointsToSpend -= 1;
					agi.setText(String.valueOf(tempAgi));
					pointsToSpendText.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		minusAgi.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusAgi) {
				if (tempAgi > 0) {
					tempAgi -= 1;
					pointsToSpend += 1;
					agi.setText(String.valueOf(tempAgi));
					pointsToSpendText.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		plusVit.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusVit) {
				if (pointsToSpend > 0) {
					tempVit += 1;
					System.out.println(tempVit);
					pointsToSpend -= 1;
					vit.setText(String.valueOf(tempVit));
					pointsToSpendText.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		minusVit.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusVit) {
				if (tempVit > 0) {
					tempVit -= 1;
					pointsToSpend += 1;
					vit.setText(String.valueOf(tempVit));
					pointsToSpendText.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});
	}

	public void hideActors() {
		for (Actor a : actorList) {
			a.setVisible(false);
		}
	}

	public void showActors() {
		for (Actor a : actorList) {
			a.setVisible(true);
		}
	}

	public Boolean doubleCheck() {
		final Table tempTable = new Table(skin);
		tempTable.setFillParent(true);

		Label tempLabel;
		TextButton yesButton;
		TextButton noButton;
		switch (currentPage) {
		case makeName:
			hideActors();

			tempLabel = new Label("Are you sure you are " + tempFirst + " "
					+ tempSecond + "?", skin);
			yesButton = new TextButton("Yes", skin);
			noButton = new TextButton("No", skin);

			tempTable.add(tempLabel);
			tempTable.row();
			tempTable.add(yesButton);
			tempTable.add(noButton);
			stage.addActor(tempTable);
			tempTable.toFront();

			yesButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int yesButton) {
					tempPlayer.setFirstName(tempFirst);
					tempPlayer.setLastName(tempSecond);

					System.out.println(tempFirst + " " + tempSecond);
					makeStatsSetup();
					return true;
				}
			});
			noButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int noButton) {
					showActors();
					tempTable.remove();
					return true;
				}
			});
			break;
		case makeStats:
			mainTable.setVisible(false);
			nextButton.setVisible(false);
			tempLabel = new Label("Are you happy with what you can do?", skin,
					"default");
			yesButton = new TextButton("Yes", skin);
			noButton = new TextButton("No", skin);

			tempTable.add(tempLabel);
			tempTable.row();
			tempTable.add(yesButton).uniform();
			tempTable.add(noButton).uniform();
			stage.addActor(tempTable);
			tempTable.toFront();

			yesButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int yesButton) {
					tempPlayer.setStats("Strength", tempStr);
					tempPlayer.setStats("Wisdom", tempWis);
					tempPlayer.setStats("Intelligence", tempInt);
					tempPlayer.setStats("Agility", tempAgi);
					tempPlayer.setStats("Vitality", tempVit);
					makeFeatsSetup();
					return true;
				}
			});
			noButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int noButton) {
					mainTable.setVisible(true);
					nextButton.setVisible(true);
					tempTable.remove();
					return true;
				}
			});

			break;
		case makeFeats:
			mainTable.setVisible(false);
			nextButton.setVisible(false);
			tempLabel = new Label("Are you sure you want to choose "
					+ currentFeat.getName(), skin, "default");
			yesButton = new TextButton("Yes", skin);
			noButton = new TextButton("No", skin);

			tempTable.add(tempLabel);
			tempTable.row();
			tempTable.add(yesButton).uniform();
			tempTable.add(noButton).uniform();
			stage.addActor(tempTable);
			tempTable.toFront();

			yesButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int yesButton) {
					tempPlayer.addFeat(currentFeat);
					makeSpriteSetup();
					return true;
				}
			});
			noButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int noButton) {
					mainTable.setVisible(true);
					nextButton.setVisible(true);
					tempTable.remove();
					return true;
				}
			});
			break;
		case makeSprite:

			for (Actor a : actorList) {
				a.setVisible(false);
			}

			tempLabel = new Label("Looking good?", skin, "default");
			yesButton = new TextButton("Yes", skin);
			noButton = new TextButton("No", skin);

			tempTable.add(tempLabel);
			tempTable.row();
			tempTable.add(yesButton).uniform();
			tempTable.add(noButton).uniform();
			stage.addActor(tempTable);
			tempTable.toFront();

			yesButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int yesButton) {
					System.out
							.println("MakeSprite yes button pressed in doublecheck");
					gsm.setScreen(new Level(gsm, "firstPlatformer.tmx"));
					stage.dispose();
					return true;
				}
			});
			noButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int noButton) {
					mainTable.setVisible(true);
					for (Actor a : actorList) {
						a.setVisible(true);
					}
					tempTable.remove();
					return true;
				}
			});
			break;
		case makeOverview:
			break;
		default:
			break;
		}
		return false;
	}

	public void makeSpriteSetup() {
		stage.dispose();

		stage = new Stage();
		currentPage = Process.makeSprite;
		Gdx.input.setInputProcessor(stage);
		mainTable = new Table(skin);

		nextButton = new TextButton("Next", skin);
		nextButton.setX(Gdx.graphics.getWidth() - nextButton.getWidth());
		nextButton.setY(8);
		stage.addActor(nextButton);

		actorList.add(nextButton);

		defineNextButton(nextButton);

		Group bodyGroup = new Group();
		Image paperDoll = new Image(new Texture(
				Gdx.files.internal("character/front.png")));
		bodyGroup.addActor(paperDoll);
		bodyGroup.setSize(paperDoll.getWidth(), paperDoll.getHeight());
		bodyGroup.setX(Gdx.graphics.getWidth() / 2 - bodyGroup.getWidth() / 2);
		bodyGroup
				.setY(Gdx.graphics.getHeight() / 2 - bodyGroup.getHeight() / 2);
		System.out.println("Height " + bodyGroup.getHeight() / 2);
		stage.addActor(bodyGroup);
		actorList.add(bodyGroup);

		Texture img = new Texture(Gdx.files.internal("character/hairAtlas.png"));
		tempHairs = TextureRegion.split(img, 32, 64);
		hairSprite = new Sprite(tempHairs[1][0]);

		Texture tempTexture = new Texture(
				Gdx.files.internal("GUI Assets/minusArrow.png"));
		TextureRegion tempRegion = new TextureRegion(tempTexture);
		TextureRegionDrawable tempDraw = new TextureRegionDrawable(tempRegion);
		minusHair = new Button(tempDraw);
		minusMouth = new Button(tempDraw);
		minusEyes = new Button(tempDraw);

		tempTexture = new Texture(
				Gdx.files.internal("GUI Assets/plusArrow.png"));
		tempRegion = new TextureRegion(tempTexture);
		tempDraw = new TextureRegionDrawable(tempRegion);
		plusEyes = new Button(tempDraw);
		plusHair = new Button(tempDraw);
		plusMouth = new Button(tempDraw);

		eyeImage = new Image(hairSprite);
		hairImage = new Image(hairSprite);
		mouthImage = new Image(hairSprite);

		bodyGroup.addActor(eyeImage);
		bodyGroup.addActor(mouthImage);
		bodyGroup.addActor(hairImage);

		int minusX = Gdx.graphics.getWidth() / 2 - 32;
		int plusX = Gdx.graphics.getWidth() / 2 + 32;

		int startY = Gdx.graphics.getHeight() / 2;

		ArrayList<Actor> minusList = new ArrayList<Actor>();
		ArrayList<Actor> plusList = new ArrayList<Actor>();

		minusList.add(minusHair);
		minusList.add(minusEyes);
		minusList.add(minusMouth);

		plusList.add(plusHair);
		plusList.add(plusEyes);
		plusList.add(plusMouth);

		int tempInt = 0;

		for (Actor a : minusList) {
			a.setX(minusX);
			a.setY(startY - tempInt * 32);
			tempInt++;
			stage.addActor(a);
			System.out.println("Added actor " + a.toString() + " at "
					+ a.getX() + ", " + a.getY());
			actorList.add(a);
		}

		tempInt = 0;

		for (Actor a : plusList) {
			a.setX(plusX);
			a.setY(startY - tempInt * 32);
			tempInt++;
			stage.addActor(a);
			System.out.println("Added actor " + a.toString() + " at "
					+ a.getX() + ", " + a.getY());
			actorList.add(a);
		}

		// stage.setDebugAll(true);

		minusHair.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusHair) {
				if (hairChoice > 0) {
					hairChoice--;
				}
				System.out.println("Minus");
				hairSprite = new Sprite(tempHairs[hairChoice][0]);
				hairImage.setDrawable(new SpriteDrawable(hairSprite));
				return true;
			}
		});

		plusHair.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusHair) {
				if (hairChoice < tempHairs.length - 1) {
					hairChoice++;
				}
				System.out.println("Plus");
				hairSprite = new Sprite(tempHairs[hairChoice][0]);
				hairImage.setDrawable(new SpriteDrawable(hairSprite));
				return true;
			}
		});

	}

	public void makeFeatsSetup() {
		stage.dispose();

		stage = new Stage();
		currentPage = Process.makeFeats;
		tempFeatManager = new FeatManager(tempPlayer);
		currentFeat = tempFeatManager.getFeatList().get(0);
		Gdx.input.setInputProcessor(stage);
		mainTable = new Table(skin);
		stage.addActor(mainTable);

		Table featTable = new Table(skin);
		Texture tempImg = new Texture(tempFeatManager.getFeatList().get(0)
				.getImg());
		featImage = new Image(tempImg);

		featRequirement = new Label("", skin, "default");
		featDescription = new Label("", skin, "default");
		for (final Feat f : tempFeatManager.getFeatList()) {
			System.out.println(f.getName());
			Button tempButton = new TextButton(f.getName(), skin);
			tempButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int tempButton) {
					Image newImage = new Image(new Texture(f.getImg()));
					featImage = newImage;
					featDescription.setText(f.getDescription());
					currentFeat = f;
					return true;
				}
			});
			featTable.add(tempButton).row();

		}

		System.out.println(featTable.getChildren());
		scroll = new ScrollPane(featTable);
		scroll.setScrollBarPositions(false, true);

		Label titleLabel = new Label("Choose a Feat", skin, "default");
		featDescription.setWrap(true);
		featDescription.setWidth(Gdx.graphics.getWidth() / 2);

		mainTable.setFillParent(true);
		mainTable.add(titleLabel).center().colspan(2).row();
		mainTable.add(scroll).center().expandX().fillX()
				.width(Gdx.graphics.getWidth() / 2);
		mainTable.add(featImage).expandX().fillX()
				.width(Gdx.graphics.getWidth() / 2)
				.height(Gdx.graphics.getHeight() * 0.40f).row();
		mainTable.add(featRequirement).width(Gdx.graphics.getWidth())
				.height(Gdx.graphics.getHeight() / 10).expandX().colspan(2)
				.row();
		mainTable
				.add(featDescription)
				.left()
				.size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 4);
		mainTable.row();

		nextButton = new TextButton("Next", skin);
		nextButton.setX(stage.getWidth() - nextButton.getWidth() - 16);
		nextButton.setY(16);
		stage.addActor(nextButton);

		defineNextButton(nextButton);

	}

	public void makeNameSetup() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		actorList = new ArrayList<Actor>();

		Label firstName = new Label("First name: ", skin);
		firstName.setX(Gdx.graphics.getWidth() / 2 - firstName.getWidth());
		firstName.setY(Gdx.graphics.getHeight() / 2 + firstName.getHeight()
				* 1.9f);

		firstNameTextField = new TextField("", skin);
		firstNameTextField.setX(Gdx.graphics.getWidth() / 2);
		firstNameTextField.setY(Gdx.graphics.getHeight() / 2
				+ firstName.getHeight());
		firstNameTextField.setAlignment(1);
		firstNameTextField.setMaxLength(12);

		Label secondName = new Label("Last name: ", skin);
		secondName.setX(Gdx.graphics.getWidth() / 2 - firstName.getWidth());
		secondName.setY(Gdx.graphics.getHeight() / 2 - firstName.getHeight()
				* 0.2f);

		secondNameTextField = new TextField("", skin);
		secondNameTextField.setAlignment(1);
		secondNameTextField.setMaxLength(12);
		secondNameTextField.setX(Gdx.graphics.getWidth() / 2);
		secondNameTextField.setY(Gdx.graphics.getHeight() / 2
				- firstName.getHeight());

		nextButton = new TextButton("Next", skin);

		nextButton.setX(stage.getWidth() - nextButton.getWidth() - 16);
		nextButton.setY(16);
		defineNextButton(nextButton);

		actorList.add(firstName);
		actorList.add(firstNameTextField);
		actorList.add(secondName);
		actorList.add(secondNameTextField);
		actorList.add(nextButton);

		stage.addActor(firstName);
		stage.addActor(firstNameTextField);
		stage.addActor(secondName);
		stage.addActor(secondNameTextField);
		stage.addActor(nextButton);

	}

	public void makeStatsSetup() {

		stage.dispose();

		stage = new Stage();
		currentPage = Process.makeStats;
		Gdx.input.setInputProcessor(stage);
		mainTable = new Table(skin);
		stage.addActor(mainTable);
		mainTable.setFillParent(true);

		Texture tempTexture = new Texture(
				Gdx.files.internal("GUI Assets/minusArrow.png"));
		TextureRegion tempRegion = new TextureRegion(tempTexture);
		TextureRegionDrawable tempDraw = new TextureRegionDrawable(tempRegion);
		minusStr = new Button(tempDraw);
		minusWis = new Button(tempDraw);
		minusInt = new Button(tempDraw);
		minusAgi = new Button(tempDraw);
		minusVit = new Button(tempDraw);

		tempTexture = new Texture(
				Gdx.files.internal("GUI Assets/plusArrow.png"));
		tempRegion = new TextureRegion(tempTexture);
		tempDraw = new TextureRegionDrawable(tempRegion);

		plusStr = new Button(tempDraw);
		plusWis = new Button(tempDraw);
		plusInt = new Button(tempDraw);
		plusAgi = new Button(tempDraw);
		plusVit = new Button(tempDraw);

		pointsToSpendText = new Label(String.valueOf(pointsToSpend), skin,
				"default");
		str = new Label(String.valueOf(tempStr), skin, "default");
		wis = new Label(String.valueOf(tempWis), skin, "default");
		intel = new Label(String.valueOf(tempInt), skin, "default");
		agi = new Label(String.valueOf(tempAgi), skin, "default");
		vit = new Label(String.valueOf(tempVit), skin, "default");

		pointsText = new Label("Points to spend: ", skin, "default");
		strText = new Label("Strength: ", skin, "default");
		wisText = new Label("Wisdom: ", skin, "default");
		intelText = new Label("Intelligence: ", skin, "default");
		agiText = new Label("Agility: ", skin, "default");
		vitText = new Label("Vitality: ", skin, "default");

		mainTable.add(pointsText).colspan(2).center();
		mainTable.add(pointsToSpendText);
		mainTable.row();

		mainTable.add(strText);
		mainTable.add(minusStr);
		mainTable.add(str);
		mainTable.add(plusStr);
		mainTable.row();

		mainTable.add(wisText);
		mainTable.add(minusWis);
		mainTable.add(wis);
		mainTable.add(plusWis);
		mainTable.row();

		mainTable.add(intelText);
		mainTable.add(minusInt);
		mainTable.add(intel);
		mainTable.add(plusInt);
		mainTable.row();

		mainTable.add(agiText);
		mainTable.add(minusAgi);
		mainTable.add(agi);
		mainTable.add(plusAgi);
		mainTable.row();

		mainTable.add(vitText);
		mainTable.add(minusVit);
		mainTable.add(vit);
		mainTable.add(plusVit);
		mainTable.row();

		assignStatArrowButtons();

		nextButton = new TextButton("Next", skin);
		stage.addActor(nextButton);
		nextButton.setX(Gdx.graphics.getWidth() - nextButton.getWidth());
		nextButton.setY(nextButton.getHeight() / 2);

		defineNextButton(nextButton);

		// mainTable.setDebug(true);

	}

	public void defineNextButton(Button inputButton) {
		Button button = inputButton;

		switch (currentPage) {
		case makeFeats:
			button.addListener(new InputListener() {
				// Triggered on pressing down
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("Button Hit");
					doubleCheck();
					return true;
				}
			});
			break;
		case makeName:
			button.addListener(new InputListener() {
				// Triggered on pressing down
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("Button Hit");
					tempFirst = firstNameTextField.getText();
					tempSecond = secondNameTextField.getText();
					doubleCheck();
					return true;
				}

				// Triggered on release
				// public void touchUp() {

			}
			// }
			);
			break;
		case makeOverview:
			break;
		case makeSprite:
			button.addListener(new InputListener() {
				// Triggered on pressing down
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("Button Hit");
					doubleCheck();
					return true;
				}
			});
			break;
		case makeStats:
			button.addListener(new InputListener() {
				// Triggered on pressing down
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("Button Hit");
					doubleCheck();
					return true;
				}
			});
			break;
		default:
			break;

		}
	}

}
