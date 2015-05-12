package com.rohan.econome;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class Player extends Actor {

	enum AnimationState {
		Left, Right, Up, Down
	}

	enum Direction {
		Left, Right
	}

	enum floorState {
		onGround, inAir, onLadder
	}

	enum State {
		Chatting, Moving, Trading, Fishing, Inventory
	}

	protected Vector2 location;
	protected Vector2 previousLocation;
	protected Vector2 tempLocation;

	protected Texture img;
	protected MapProperties mapProperties;
	protected int mapWidth;
	protected int mapHeight;
	protected Animation walkLeft;
	protected Animation walkRight;
	protected Animation walkUp;
	protected Animation walkDown;
	protected AnimationState animationState = AnimationState.Down;
	State currentState = State.Moving;
	protected float stateTime;
	TextureRegion[] animationFrames;

	TextureRegion[] animationFrames2;
	TextureRegion currentFrame;
	// Horizontal
	protected static final int FRAME_COLS = 9;
	// Vertical
	protected static final int FRAME_ROWS = 4;

	protected Rectangle boundingRectangle;

	protected Circle interactCircle;

	protected Vector2 interactCircleLocation;

	protected float interactTimer;
	protected int speed;

	protected Entity activeEntity;
	private int chatSelection;

	private String[][] characterKnowledge;
	private Boolean foundName;

	private TradeHandler th;

	private Boolean tradeSetup = false;

	protected InventoryManager im;

	protected int str, wis, intel, agility, vit;
	protected int coins = 150;
	protected Level level;
	protected XmlReader reader;
	protected StringWriter sw;

	protected XmlWriter writer;
	protected FileHandle file;

	protected ArrayList<Feat> featList = new ArrayList<Feat>();
	protected String firstName, lastName;
	private int jumpCount;
	private float jumpCooldown = 0.1f;
	private float vertVelocity = -0.8f;
	private float horVelocity = 0f;
	private float friction = 1f;

	private floorState fs = floorState.inAir;

	private ArrayList<DustCloud> dustList = new ArrayList<DustCloud>();

	public Player(int inputX, int inputY, Level inputLevel) {

		location = new Vector2(inputX, inputY);
		previousLocation = location;
		level = inputLevel;
		img = new Texture("animations/player.png");
		TextureRegion[][] tempFrames = TextureRegion.split(img, img.getWidth()
				/ FRAME_COLS, img.getHeight() / FRAME_ROWS);
		TextureRegion[][] tempFrames2 = TextureRegion.split(new Texture(
				"animations/ro2.png"), 32, 64);
		for (int i = 0; i < FRAME_ROWS; i++) {
			animationFrames = new TextureRegion[FRAME_COLS];
			int index = 0;
			for (int j = 0; j < FRAME_COLS; j++) {
				animationFrames[index++] = tempFrames[i][j];
			}
			animationFrames2 = new TextureRegion[30];
			for (int n = 0; n < 30; n++) {

				animationFrames2[n] = tempFrames2[n][0];
			}
			switch (i) {
			case 0:
				walkUp = new Animation(1f / 9f, animationFrames);
				break;
			case 1:
				walkLeft = new Animation(1f / 9f, animationFrames);
				break;
			case 2:
				// walkDown = new Animation(1f / 9f, animationFrames);
				walkDown = new Animation(1f / 30f, animationFrames2);
				break;
			case 3:
				walkRight = new Animation(1f / 9f, animationFrames);
				break;

			}
		}
		/*
		 * walkLeft = new Animation(1f/9f, animationFrames[1]); walkRight = new
		 * Animation(1f/9f, animationFrames[3]); walkUp = new Animation(1f/9f,
		 * animationFrames[0]); walkDown = new Animation(1f/9f,
		 * animationFrames[2]);
		 */
		stateTime = 0f;
		loadMap();
		currentFrame = walkDown.getKeyFrame(stateTime, true);

		boundingRectangle = new Rectangle();
		interactCircleLocation = new Vector2(location);
		interactCircle = new Circle(interactCircleLocation.x,
				interactCircleLocation.y, 4);
		interactTimer = 0f;
		reader = new XmlReader();
		readData();

		System.out.println("Player Stats: ");
		System.out.println("Strength: " + str);
		System.out.println("Intelligence: " + intel);
		System.out.println("Wisdom: " + wis);
		System.out.println("Agility: " + agility);
		System.out.println("Coins: " + coins);
		speed = agility;
		System.out.println("Speed: " + speed);

		characterKnowledge = new String[1000][100];
		characterKnowledge[0][0] = "thyself";

	}

	public Player() {
		// DON'T TOUCH, FOR TEMP PLAYER PURPOSES
	}

	public void addFeat(Feat inputFeat) {
		featList.add(inputFeat);
	}

	public void adjustGravity() {
		if (vertVelocity >= -5.0f)
			vertVelocity += -3f;
		else {
			vertVelocity = -12f;
		}
	}

	public void applyFriction() {
		if (horVelocity > friction && horVelocity > 0) {
			horVelocity -= friction;
		} else if (horVelocity < 0) {
			horVelocity += friction;
		} else if (horVelocity <= friction) {
			horVelocity = 0;
		}
	}

	public void applyGravity() {
		this.location.y += vertVelocity;
	}

	// public ArrayList<DustCloud> getDustList(){return null;}

	public void applySpeed(Direction inputDirection) {
		// I KNOW IT AIN'T PHYSICS KOSHER
		switch (inputDirection) {
		case Left:
			if (horVelocity > speed * -4 && fs == floorState.onGround) {
				horVelocity -= speed / 2;
				dustList.add(new DustCloud("Right", location.x, location.y));
			}
			break;
		case Right:
			if (horVelocity < speed * 4 && fs == floorState.onGround) {
				horVelocity += speed / 2;
				dustList.add(new DustCloud("Left", location.x, location.y));
			}
			break;
		}
	}

	public void checkChatting() {
		if (!activeEntity.getDialogueHandler().getChatting()) {
			System.out
					.println("Setting state to moving in player checkChatting()");
			currentState = State.Moving;
		}
		if (activeEntity.getDialogueHandler().getTrading()) {
			System.out
					.println("Setting state to trading in player checkChatting();");
			currentState = State.Trading;
		}
		// System.out.println("Entering chat mode!");
		interactTimer -= Gdx.graphics.getDeltaTime();
		if (interactTimer <= 0) {
			if (activeEntity.getDialogueHandler().getResponding()) {
				if (Gdx.input.isKeyPressed(Keys.E)) {
					interactTimer = 0.25f;
					activeEntity.getDialogueHandler().continueDialogue();
				}
			} else {
				if (Gdx.input.isKeyPressed(Keys.W)) {
					interactTimer = 0.10f;
					if (chatSelection == 0) {
						chatSelection = activeEntity.getDialogueHandler()
								.getTalkingPointsSize() - 1;
					} else {
						chatSelection--;
					}
				}

				if (Gdx.input.isKeyPressed(Keys.S)) {
					interactTimer = 0.10f;
					if (chatSelection < activeEntity.getDialogueHandler()
							.getTalkingPointsSize() - 1) {
						chatSelection += 1;
					} else {
						chatSelection = 0;
					}
				}
				if (Gdx.input.isKeyPressed(Keys.E)) {
					interactTimer = 0.25f;
					System.out.println("Player pressed E!");
					System.out.println("Player e: assigning new knowledge");
					characterKnowledge[checkNPCID()][findEmptyKnowledge(checkNPCID())] = activeEntity
							.getDialogueHandler().selectDialogue(chatSelection);
					System.out.println("Player e: Player should have learned "
							+ activeEntity.getDialogueHandler().selectDialogue(
									chatSelection));
					System.out
							.println("Player e: Reassigning player knowledge copy");
					activeEntity.getDialogueHandler().setPlayerKnowledgeCopy(
							characterKnowledge);
					System.out
							.println("Player e: Attempting to create dialogue");
					activeEntity.getDialogueHandler().createDialogue();
				}
			}
		}

	}

	public Boolean checkColliders() {
		Rectangle tempCollision = new Rectangle();
		Boolean canMove = true;

		for (Entity e : level.getCritters()) {
			tempCollision = new Rectangle(this.getCollision().x + horVelocity,
					this.getCollision().y, this.getCollision().width,
					this.getCollision().height);
			if (Intersector.overlaps(tempCollision, e.getCollision())) {
				canMove = false;
				e.handleCollision(this);
				return canMove;
			}
		}
		for (Rectangle r : level.getColliders()) {
			tempCollision = new Rectangle(this.getCollision().x + horVelocity,
					this.getCollision().y, this.getCollision().width,
					this.getCollision().height);
			if (Intersector.overlaps(tempCollision, r)) {
				canMove = false;
				return canMove;
			}
		}
		return canMove;
	}

	public void checkCollision() {
		// Dirty hack
		fs = floorState.inAir;
		if (checkGravityCollision(vertVelocity)) {
			fs = floorState.onGround;
			vertVelocity = 0f;
		}
		if (checkLadderCollision()) {
			fs = floorState.onLadder;
			if (vertVelocity < 0) {
				vertVelocity = 0f;
			}
		}
	}

	public Boolean checkCollision(Float inputVelocity) {
		Rectangle tempCollision = new Rectangle(this.getCollision().x,
				this.getCollision().y + inputVelocity,
				this.getCollision().width, this.getCollision().height
						+ inputVelocity);
		for (Rectangle r : level.getColliders()) {
			if (Intersector.overlaps(tempCollision, r)) {
				// Player hit something
				return true;

			}
		}
		return false;
	}

	public Boolean checkGravityCollision(Float inputVelocity) {
		Rectangle tempCollision = new Rectangle(this.getCollision().x,
				this.getCollision().y + inputVelocity,
				this.getCollision().width, this.getCollision().height
						+ inputVelocity);
		for (Rectangle r : level.getColliders()) {
			if (Intersector.overlaps(tempCollision, r)) {
				// If the player's collision box is greater than the y location
				// of the collider box, the player is above it
				// and therefore must be on the ground
				for (float i = vertVelocity / 2; i > 0; i--) {
					if (i % 2 == 0) {
						dustList.add(new DustCloud("Right", location.x,
								location.y));
					}
					dustList.add(new DustCloud("Left", location.x, location.y));
				}
				vertVelocity = 0;
				if (tempCollision.y > r.y) {
					// Player has hit the ground
					return true;
				}
			}
		}

		/*
		 * for(Zone z : level.getZones()) { Rectangle r = z.getRectangle();
		 * if(Intersector.overlaps(tempCollision, r)) { if(tempCollision.y >
		 * r.y) { return true; } }
		 * 
		 * 
		 * }
		 */

		return false;
	}

	public void checkInventory() {
		if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
			// ms.getCamera().setToOrtho(false);
			currentState = State.Moving;
		}
		im.update();
	}

	public Boolean checkLadderCollision() {

		Rectangle tempCollision = new Rectangle(this.getCollision().x,
				this.getCollision().y + vertVelocity,
				this.getCollision().width, this.getCollision().height
						+ vertVelocity);

		for (Zone z : level.getZones()) {
			if (z instanceof LadderZone) {
				Rectangle r = z.getRectangle();
				// if (Intersector.overlaps(this.getCollision(), r)) {
				if (Intersector.overlaps(tempCollision, r)) {
					return true;
				}
			}
		}

		return false;
	}

	public void checkLevelChange() {
		Rectangle tempCollision;
		for (Zone z : level.getZones()) {
			tempCollision = new Rectangle(this.getCollision().x,
					this.getCollision().y, this.getCollision().width,
					this.getCollision().height);
			if (Intersector.overlaps(tempCollision, z.getRectangle())
					&& z instanceof TeleZone) {

				writeData();
				System.out
						.println("Player.checkLevelChange is getting a zone change of the type "
								+ ((TeleZone) z).getType());
				switch (((TeleZone) z).getType()) {
				case "ORTHOGRAPHIC":
					System.out
							.println("Player.checkLevel change is setting level to a new orthographic level");
					// TODO:ADD LEVEL CODE HERE
					break;
				case "PLATFORM":
					System.out
							.println("Player.checkLevel change is setting level to a new platform level");
					// level.ms.changeLevel(new PlatformLevel(level.ms,
					// ((TeleZone) z).getDestination()));
					break;
				}

			}
		}
	}

	public void checkMovement() {

		Rectangle tempCollision;
		Boolean canMove = true;

		if (Gdx.input.isKeyPressed(Keys.W)) {
			animationState = AnimationState.Up;
			handleAnimation();
			for (Entity e : level.getCritters()) {
				tempCollision = new Rectangle(this.getCollision().x,
						this.getCollision().y + speed,
						this.getCollision().width,
						this.getCollision().height / 2);
				if (Intersector.overlaps(tempCollision, e.getCollision())) {
					canMove = false;
					e.handleCollision(this);
				}
			}
			for (Rectangle r : level.getColliders()) {
				tempCollision = new Rectangle(this.getCollision().x,
						this.getCollision().y + speed,
						this.getCollision().width,
						this.getCollision().height / 2);
				if (Intersector.overlaps(tempCollision, r)) {
					canMove = false;
				}
			}
			if (canMove) {
				location.y += speed;
			}
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			animationState = AnimationState.Down;
			handleAnimation();
			for (Entity e : level.getCritters()) {
				tempCollision = new Rectangle(this.getCollision().x,
						this.getCollision().y - speed,
						this.getCollision().width, this.getCollision().height);
				if (Intersector.overlaps(tempCollision, e.getCollision())) {
					canMove = false;
					e.handleCollision(this);
				}
			}
			for (Rectangle r : level.getColliders()) {
				tempCollision = new Rectangle(this.getCollision().x,
						this.getCollision().y - speed,
						this.getCollision().width, this.getCollision().height);
				if (Intersector.overlaps(tempCollision, r)) {
					canMove = false;
				}
			}
			if (canMove) {
				location.y -= speed;
			}
		} else if (Gdx.input.isKeyPressed(Keys.A)) {
			animationState = AnimationState.Left;
			handleAnimation();
			for (Entity e : level.getCritters()) {
				tempCollision = new Rectangle(this.getCollision().x - speed,
						this.getCollision().y, this.getCollision().width,
						this.getCollision().height);
				if (Intersector.overlaps(tempCollision, e.getCollision())) {
					canMove = false;
					e.handleCollision(this);
				}
			}
			for (Rectangle r : level.getColliders()) {
				tempCollision = new Rectangle(this.getCollision().x - speed,
						this.getCollision().y, this.getCollision().width,
						this.getCollision().height);
				if (Intersector.overlaps(tempCollision, r)) {
					canMove = false;
				}
			}
			if (canMove) {
				location.x -= speed;
			}
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			animationState = AnimationState.Right;
			handleAnimation();
			for (Entity e : level.getCritters()) {
				tempCollision = new Rectangle(this.getCollision().x + speed,
						this.getCollision().y, this.getCollision().width,
						this.getCollision().height);
				if (Intersector.overlaps(tempCollision, e.getCollision())) {
					canMove = false;
					e.handleCollision(this);
				}
			}
			for (Rectangle r : level.getColliders()) {
				tempCollision = new Rectangle(this.getCollision().x + speed,
						this.getCollision().y, this.getCollision().width,
						this.getCollision().height);
				if (Intersector.overlaps(tempCollision, r)) {
					canMove = false;
				}
			}
			if (canMove) {
				location.x += speed;
			}
		}

		else if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
			currentState = State.Inventory;
			// ms.getCamera().setToOrtho(true);

		}

	}

	public int checkNPCID() {
		for (int i = 0; i < characterKnowledge.length; i++) {
			if (characterKnowledge[i][0].equals(activeEntity.getName())) {
				return i;
			}
		}
		System.out.println("No npc yo");
		return 99999999;
	}

	public void checkTrading() {
		if (tradeSetup) {
			th.Update();
		} else {
			setupTrading();
		}
	}

	public void dispose() {
		img.dispose();
	}

	public int findEmptyKnowledge(int inputNPCNumber) {
		for (int i = 0; i < characterKnowledge[inputNPCNumber].length; i++) {
			if (characterKnowledge[inputNPCNumber][i] == null) {
				return i;
			}
		}
		System.out.println("No available spaces for new info on this NPC yo");
		return 999999;
	}

	public void fullStop() {
		location = previousLocation;
		System.out.println("FULL STOP!");
	}

	public Entity getActiveEntity() {
		return activeEntity;
	}

	public int getChatSelection() {
		return chatSelection;
	}

	public int getCoins() {
		return coins;
	}

	public Rectangle getCollision() {
		return boundingRectangle;
	}

	public ArrayList<DustCloud> getDustList() {
		return dustList;
	}

	public TextureRegion getFrame() {
		return currentFrame;
	}

	public Circle getInteraction() {
		return interactCircle;
	}

	public InventoryManager getInventoryManager() {
		return im;
	}

	public State getState() {
		return currentState;
	}

	public int getStats(String inputStat)

	{
		switch (inputStat) {
		case "Strength":
			return str;
		case "Wisdom":
			return wis;
		case "Intelligence":
			return intel;
		case "Agility":
			return agility;
		case "Vitality":
			return vit;
		}
		return 0;

	}

	public Texture getTexture() {
		return null;
	}

	public TextureRegion getTextureRegion() {
		return currentFrame;
	}

	public TradeHandler getTradeHandler() {
		return th;
	}

	public Boolean getTradeSetup() {
		return tradeSetup;
	}

	@Override
	public float getX() {
		return location.x;
	}

	@Override
	public float getY() {
		return location.y;
	}

	public void handleAnimation() {
		stateTime += Gdx.graphics.getDeltaTime();
		switch (animationState) {
		case Left:
			// System.out.println("State is left");
			currentFrame = walkLeft.getKeyFrame(stateTime, true);
			interactCircleLocation.set(
					location.x - currentFrame.getRegionWidth() / 3, location.y
							+ currentFrame.getRegionHeight() / 4);
			break;
		case Right:
			// System.out.println("State is right");
			currentFrame = walkRight.getKeyFrame(stateTime, true);
			interactCircleLocation.set(
					location.x + currentFrame.getRegionWidth() / 3, location.y
							+ currentFrame.getRegionHeight() / 4);
			break;
		case Up:
			// System.out.println("State is up");
			currentFrame = walkUp.getKeyFrame(stateTime, true);
			interactCircleLocation.set(location.x,
					location.y + currentFrame.getRegionHeight() / 2);
			break;
		case Down:
			// System.out.println("State is down");
			currentFrame = walkDown.getKeyFrame(stateTime, true);
			interactCircleLocation.set(location.x,
					location.y - currentFrame.getRegionHeight() / 2);
			break;
		}
	}

	public void handleCollision() {
		previousLocation = tempLocation;
		tempLocation = location;
		boundingRectangle.set(location.x - currentFrame.getRegionWidth() / 4,
				location.y, currentFrame.getRegionWidth() / 2,
				currentFrame.getRegionHeight());
	}

	public void handleInput() {

		switch (getState()) {
		case Chatting:
			checkChatting();
			break;
		case Fishing:
			break;
		case Inventory:
			checkInventory();
			break;
		case Moving:
			checkMovement();
			interact();
			break;
		case Trading:
			checkTrading();
			break;
		default:
			break;
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			writeData();
			System.exit(0);
		}

	}

	public void handleMoving() {

		if (Gdx.input.isKeyPressed(Keys.A)) {

			animationState = AnimationState.Left;
			handleAnimation();
			moveDirection(Direction.Left);
		} else if (Gdx.input.isKeyPressed(Keys.D)) {

			animationState = AnimationState.Right;
			applySpeed(Direction.Right);
			handleAnimation();
		} else {
			applyFriction();
		}
		if (checkColliders()) {
			location.x += horVelocity;
		}

		if (Gdx.input.isKeyPressed(Keys.W)) {
			animationState = AnimationState.Up;
			handleAnimation();
			if (fs == floorState.onLadder && !checkCollision(speed / 2f)) {
				location.y += speed / 2;
			}
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			animationState = AnimationState.Up;
			handleAnimation();
			if (fs == floorState.onLadder && !checkCollision(speed / -2f)) {
				location.y -= speed / 2;
			}
		}

		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			if (fs == floorState.onGround || fs == floorState.onLadder) {
				if (jumpCooldown < 0) {
					jump();
				}
			}
		}

		if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
			if (fs == floorState.onGround || fs == floorState.onLadder) {
				currentState = State.Inventory;
			}
		}

		if (Gdx.input.isKeyJustPressed(Keys.Q)) {
			System.out.println(fs.toString());
		}

	}

	public void interact() {
		interactCircle.setPosition(location);
		interactTimer -= Gdx.graphics.getDeltaTime();

		if (interactTimer <= 0) {
			if (Gdx.input.isKeyPressed(Keys.E)) {
				// System.out.println("Player has pressed E!");
				interactTimer = 0.25f;
				interactCircle.setPosition(interactCircleLocation);

				for (Iterator<Item> iterator = level.getItems().iterator(); iterator
						.hasNext();) {
					Item i = iterator.next();
					if (Intersector.overlaps(interactCircle,
							i.getCollisionShape())) {
						if (im.addItem(i)) {
							System.out.println("Item added successfully");
							iterator.remove();
							i.setOwner("PLAYER");
							// itemsInInventory.add(i);
						} else {
							System.out
									.println("Item was not added successfully");
						}
					}
					// System.out.println("Player inv size: " +
					// itemsInInventory.size());
					// System.out.println("World inv size: " +
					// ms.getItems().size());
				}

				for (Entity e : level.getCritters()) {
					if (Intersector.overlaps(interactCircle, e.getCollision())) {
						for (int i = 0; i < characterKnowledge.length; i++) {
							if (characterKnowledge[i][0] == e.getName()) {
								foundName = true;
								System.out.println("Found Name!");
								break;
							} else {
								foundName = false;
								// System.out.println("No name found!");
								// System.out.println(i);
							}
						}

						if (foundName) {
							e.interact();
						} else {
							System.out.println("Firing up adding name system");
							// boolean charKnown = false;
							for (int i = 0; i < characterKnowledge.length; i++) {
								System.out
										.println("I don't know the name yet!");
								System.out.println("For loop at location " + i);
								if (characterKnowledge[i][0] == null) {
									characterKnowledge[i][0] = e.getName();
									// charKnown = true;
									e.getDialogueHandler()
											.setPlayerKnowledgeCopy(
													characterKnowledge);
									System.out
											.println("I know the name now! Stored at location "
													+ i);
									break;
								}
							}
							e.interact();
						}

					}
				}

			}
		}

		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			speed = 40;
		} else {
			speed = agility;
		}
	}

	public void jump() {
		fs = floorState.inAir;
		vertVelocity = agility + 10f;
		jumpCooldown = 0.1f;
	}

	public void loadMap() {
		mapProperties = level.getCurrentMapProperties();
		int mapGridWidth = mapProperties.get("width", Integer.class);
		int mapGridHeight = mapProperties.get("height", Integer.class);
		int tilePixelWidth = mapProperties.get("tilewidth", Integer.class);
		int tilePixelHeight = mapProperties.get("tileheight", Integer.class);

		mapWidth = mapGridWidth * tilePixelWidth;
		mapHeight = mapGridHeight * tilePixelHeight;
	}

	public void moveDirection(Direction inputDirection) {
		switch (inputDirection) {
		case Left:
			applySpeed(inputDirection);
			break;
		case Right:
			applySpeed(inputDirection);

			break;
		}

	}

	public void readData() {
		readStats();
		im = new InventoryManager(this);
		readItems();
	}

	public void readItems() {
		Element root;
		try {
			// TODO: FIX THIS TO BE LOCAL, NOT INTERNAL
			root = reader.parse(Gdx.files.local("player.xml"));
			Element inventory = root.getChildByName("Inventory");
			coins = inventory.getInt("Coins");
			Element items = inventory.getChildByName("Items");
			Array<Element> inventoryList = items.getChildrenByName("Item");
			for (Element e : inventoryList) {
				System.out.println("Name : " + e.getName());
				System.out.println("Text: " + e.getText());
			}
			for (Element e : inventoryList) {
				Item i = new Item(e.getText(), "PLAYER");
				im.addItem(i);
			}
		} catch (IOException e1) {
			System.out.println("Player.readItems() could find no file");
			e1.printStackTrace();
		}
	}

	public void readStats() {

		Element root;
		try {
			root = reader.parse(Gdx.files.local("player.xml"));
			// Build player stats
			Element stats = root.getChildByName("Stats");

			str = stats.getInt("Strength");
			System.out.println("Str is : " + str);
			wis = stats.getInt("Wisdom");
			intel = stats.getInt("Intelligence");
			agility = stats.getInt("Agility");
			vit = stats.getInt("Vitality");
		} catch (IOException e) {
			System.out.println("Player.readStats() could find no file");
			e.printStackTrace();
		}

	}

	public void reduceJumpCooldown() {
		jumpCooldown -= Gdx.graphics.getDeltaTime();
	}

	public void screenEdging() {
		if (location.x < currentFrame.getRegionWidth() / 4) {
			location.x = currentFrame.getRegionWidth() / 4;
		}
		if (location.x > mapWidth - currentFrame.getRegionWidth() / 4) {
			location.x = mapWidth - currentFrame.getRegionWidth() / 4;
		}
		if (location.y < 0) {
			location.y = 0;
		}
		if (location.y > mapHeight - currentFrame.getRegionHeight()) {
			location.y = mapHeight - currentFrame.getRegionHeight();
		}
	}

	public void setActiveEntity(Entity inputActiveEntity) {
		activeEntity = inputActiveEntity;
	}

	public void setCoins(int inputCoins) {
		coins = inputCoins;
	}

	public void setFirstName(String inputString) {
		firstName = inputString;
	}

	public void setInventoryManager(InventoryManager inputIM) {
		im = inputIM;
	}

	public void setLastName(String inputString) {
		lastName = inputString;
	}

	public void setStateChatting() {
		currentState = State.Chatting;
		th = null;
	}

	public void setStateFishing() {
		currentState = State.Fishing;
	}

	public void setStateInventory() {
		currentState = State.Inventory;
	}

	public void setStateMoving() {
		currentState = State.Moving;
	}

	public void setStateTrading() {
		currentState = State.Trading;
	}

	public void setStats(String inputStat, int inputInt) {
		switch (inputStat) {
		case "Strength":
			str += inputInt;
			break;
		case "Wisdom":
			wis += inputInt;
			break;
		case "Intelligence":
			intel += inputInt;
			break;
		case "Agility":
			agility += inputInt;
			break;
		case "Vitality":
			vit += inputInt;
			break;
		}
	}

	public void setupTrading() {
		if (!tradeSetup) {
			System.out
					.println("Setting up trading system in player setupTrading()");
			th = new TradeHandler(this, activeEntity);
			tradeSetup = true;
		} else if (tradeSetup) {
			th = null;
			tradeSetup = false;
		}
	}

	public void update() {

		handleInput();
		handleCollision();
		checkCollision();
		screenEdging();
		checkLevelChange();
		updateLists();

		switch (fs) {
		case onGround:
			reduceJumpCooldown();
			break;
		case inAir:
			applyGravity();
			adjustGravity();
			break;
		case onLadder:
			reduceJumpCooldown();
			break;
		}

	}

	public void updateLists() {
		Iterator<DustCloud> dl = dustList.iterator();
		while (dl.hasNext()) {
			DustCloud dc = dl.next();
			dc.update();
			if (dc.getTimer() <= 0) {
				dl.remove();
				System.out.println("Removed dustcloud from dust list");
			}
		}
	}

	public void writeData() {
		sw = new StringWriter();
		writer = new XmlWriter(sw);
		System.out.println("Player.writeData() is beginning");
		try {
			file = Gdx.files.local("player.xml");
			writeStats();
			writeItems();
			file.writeString(sw.toString(), false);
		} catch (IOException e) {
			System.out.println("Player.writeData() dun goofed");
			e.printStackTrace();
		}

		finally {
			try {
				writer.close();
			} catch (Exception ignore) {
				// IGNORED
			}
		}
		System.out.println("Player.writeData() is ending");
	}

	public void writeItems() throws IOException {
		writer.element("Inventory").element("Coins").text(coins).pop()
				.element("Items");
		for (Item i : im.getListOfItems()) {
			writer.element("Item").text(i.getInputString()).pop();
		}
		writer.pop().pop().pop();
		System.out.println(sw);
		// file.writeString(sw.toString(), true);
	}

	public void writeStats() throws IOException {
		writer.element("Player").element("Stats").element("Strength").text(str)
				.pop().element("Intelligence").text(intel).pop()
				.element("Wisdom").text(wis).pop().element("Agility")
				.text(agility).pop().element("Vitality").text(vit).pop().pop();
		System.out.println(sw);
	}

}
