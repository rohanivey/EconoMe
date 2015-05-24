package com.rohan.econome;

public class BodyComponent {

	private enum Hierarchy {
		Living, Undead, Elemental
	}

	private Eyes eyes;
	private Ears ears;
	private Nose nose;
	private Mouth mouth;

	private int Strength;
	private int Agility;
	private int Vitality;

	private ComponentEntity owner;

	public BodyComponent(ComponentEntity inputEntity) {
		owner = inputEntity;
		init();
	}

	public void init() {

		eyes = new Eyes("None");
		ears = new Ears("None");
		nose = new Nose("None");
		mouth = new Mouth("None");

	}

	public void setEyes(String inputString) {
		eyes = new Eyes(inputString);
	}

	public void setEars(String inputString) {
		ears = new Ears(inputString);
	}

	public void setNose(String inputString) {
		nose = new Nose(inputString);
	}

	public void setMouth(String inputString) {
		mouth = new Mouth(inputString);
	}

	public int getStrength() {
		return Strength;
	}

	public void setStrength(int strength) {
		Strength = strength;
	}

	public int getAgility() {
		return Agility;
	}

	public void setAgility(int agility) {
		Agility = agility;
	}

	public int getVitality() {
		return Vitality;
	}

	public void setVitality(int vitality) {
		Vitality = vitality;
	}

}
