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

	InventoryManager myInventoryManager;

	ComponentEntity(String inputID) {
		entityID = inputID;
	}

	public void init() {

		xml = new XmlReader();
		try {
			gatherData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		myInventoryManager = new InventoryManager(this);

	}

	public void gatherData() throws IOException {
		Element root = xml.parse(Gdx.files.local("Regions"
				+ Level.getRegionName() + "/Entities/EntityList.xml"));
		Element entityXML = root.getChildByName(entityID);
		if (entityXML.get("Body").equals("None")) {
			// Stare at dong
		} else {
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
			body.setStrength(eBody.getInt("Strength"));
			body.setAgility(eBody.getInt("Agility"));
			body.setVitality(eBody.getInt("Vitality"));
		}

		if (entityXML.get("Mind").equals("None")) {
			// Stare at dong some more
		} else {
			mind = new Mind(this);

			Element eMind = entityXML.getChildByName("Mind");
			mind.setIntelligence(eMind.getInt("Intelligence"));
			mind.setMood(eMind.getInt("Mood"));
			mind.setMyType(eMind.get("Type"));

		}
		if (entityXML.get("Soul").equals("None")) {
			// STARING INCREASES
		} else {
			soul = new Soul(entityXML.get("Soul"));
		}
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
