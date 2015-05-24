package com.rohan.econome;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class ComponentEntity {

	XmlReader xml;
	Sprite sprite;
	Texture img;
	String entityID;

	BodyComponent body;

	Mind mind;

	Soul soul;

	Movement myMovement;
	Behaviour myBehaviour;
	Level level;

	InventoryManager myInventoryManager;

	Body physicsBody;

	ComponentEntity(String inputID, Level inputLevel) {
		entityID = inputID;
		level = inputLevel;

		init();
		setupPhysics();

	}

	public void setupPhysics() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(sprite.getX(), sprite.getY());
		physicsBody = level.getWorld().createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;

		Fixture fixture = physicsBody.createFixture(fixtureDef);

		shape.dispose();

	}

	public void init() {

		xml = new XmlReader();
		try {
			gatherData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out
					.println("ComponentEntity.init() didn't intialize properly");
		}

		sprite = new Sprite(myMovement.getFrame());
		sprite.setPosition(100, 200);

		myInventoryManager = new InventoryManager(this);

	}

	public void gatherData() throws IOException {
		System.out.println("ComponentEntity.gatherData() is beginning");
		Element root = xml.parse(Gdx.files.local("Regions/"
				+ level.getRegionName() + "/Entities.xml"));
		System.out.println("Root: " + root);
		Element entityXML = root.getChildByName(entityID);
		System.out.println(entityXML);

		if (entityXML.get("Body", "Exists") != "None") {
			body = new BodyComponent(this);
			Element eBody = entityXML.getChildByName("Body");
			Element eNose = eBody.getChildByName("Nose");
			Element eEyes = eBody.getChildByName("Eyes");
			Element eEars = eBody.getChildByName("Ears");
			Element eMouth = eBody.getChildByName("Mouth");

			body.setEyes(eEyes.getText());
			body.setEars(eEars.getText());
			body.setNose(eNose.getText());
			body.setMouth(eMouth.getText());
			// body.setStrength(eBody.getInt("Strength"));
			// body.setAgility(eBody.getInt("Agility"));
			// body.setVitality(eBody.getInt("Vitality"));

			System.out.println("Body: " + body);
		} else {
			// Stare at dong
			System.out.println("No body found");
		}

		if (entityXML.get("Mind", "Exists") != "None") {
			mind = new Mind(this);

			Element eMind = entityXML.getChildByName("Mind");
			// mind.setIntelligence(eMind.getInt("Intelligence"));
			// mind.setMood(eMind.getInt("Mood"));
			// mind.setMyType(eMind.get("Type"));
			System.out.println("Mind: " + mind);
		} else {
			// Stare at dong
		}
		if (entityXML.get("Soul").equals("None")) {
			soul = new Soul(entityXML.get("Soul"));
			System.out.println("Soul: " + soul);
		} else {
			// Probably put an eye out by now
		}

		// Set up entity behaviour
		switch (entityXML.get("Behaviour")) {
		case "Passive":
			myBehaviour = new BehaviourComponentPassive();
			break;
		case "Neutral":
			myBehaviour = new BehaviourComponentNeutral();
			break;
		case "Aggressive":
			myBehaviour = new BehaviourComponentAggressive();
			break;
		}
		System.out.println("Behaviour: " + myBehaviour);

		// Set up entity Movement

		switch (entityXML.get("Movement")) {
		case "Flying":
			myMovement = new MovementComponentFlying(new Texture(
					Gdx.files.local("Regions/" + level.getRegionName()
							+ "/Entities/" + entityID)));
			break;
		case "Floating":
			myMovement = new MovementComponentFloating(new Texture(
					Gdx.files.local("Regions/" + level.getRegionName()
							+ "/Entities/" + entityID + "_Floating")));
			break;
		case "Swimming":
			myMovement = new MovementComponentSwimming(new Texture(
					Gdx.files.local("Regions/" + level.getRegionName()
							+ "/Entities/" + entityID)));
			break;
		case "Ground":
			myMovement = new MovementComponentGround(new Texture(
					Gdx.files.local("Regions/" + level.getRegionName()
							+ "/Entities/" + entityID + "/" + entityID
							+ "_Ground.png")));

			break;
		case "Spectral":
			myMovement = new MovementComponentSpectral(new Texture(
					Gdx.files.local("Regions/" + level.getRegionName()
							+ "/Entities/" + entityID)));
			break;
		case "Underground":
			myMovement = new MovementComponentUnderground(new Texture(
					Gdx.files.local("Regions/" + level.getRegionName()
							+ "/Entities/" + entityID)));
			break;
		}

		System.out.println("Movement: " + myMovement);

		System.out.println("ComponentEntity.gatherData() is ending");
	}

	public BodyComponent getBody() {
		return body;
	}

	public Mind getMind() {
		return mind;
	}

	public Soul getSoul() {
		return soul;
	}

	public void draw() {

	}

	public TextureRegion getTexture() {
		// TODO Auto-generated method stub
		return myMovement.getFrame();
	}

	public float getX() {
		// TODO Auto-generated method stub
		return 100;
	}

	public float getY() {
		// TODO Auto-generated method stub
		return 100;
	}

	public void update() {
		sprite.setPosition(physicsBody.getPosition().x,
				physicsBody.getPosition().y);
		myMovement.update();
	}

	public Rectangle getCollision() {
		// JANKY PATCHWORK JAZZ
		Rectangle tempR = new Rectangle(100, 100, 100, 100);
		return tempR;
	}

	public void handleCollision(Player player) {
		// TODO Auto-generated method stub

	}

	public DialogueHandler getDialogueHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	public void interact() {
		// TODO Auto-generated method stub

	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
