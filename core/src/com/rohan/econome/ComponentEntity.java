package com.rohan.econome;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class ComponentEntity {

	XmlReader xml;
	Texture img;
	String entityID;

	Body body;

	Mind mind;

	Soul soul;

	Movement myMovement;
	Behaviour myBehaviour;
	Level level;

	InventoryManager myInventoryManager;

	ComponentEntity(String inputID, Level inputLevel) {
		entityID = inputID;
		level = inputLevel;
		init();
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
			body = new Body(this);
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
			myMovement = new MovementComponentFlying();
			break;
		case "Floating":
			myMovement = new MovementComponentFloating();
			break;
		case "Swimming":
			myMovement = new MovementComponentSwimming();
			break;
		case "Ground":
			myMovement = new MovementComponentGround();
			break;
		case "Spectral":
			myMovement = new MovementComponentSpectral();
			break;
		case "Underground":
			myMovement = new MovementComponentUnderground();
			break;
		}

		System.out.println("Movement: " + myMovement);

		System.out.println("ComponentEntity.gatherData() is ending");
	}

	public Body getBody() {
		return body;
	}

	public Mind getMind() {
		return mind;
	}

	public Soul getSoul() {
		return soul;
	}

}
