package com.rohan.econome;

public class Mind {

	private ComponentEntity owner;
	private Behaviour myBehaviour;
	private int intelligence;
	private int mood;

	private enum mindType {
		Simple, Complex
	}

	private mindType myType;

	public Mind(ComponentEntity inputEntity) {
		owner = inputEntity;
		init();
	}

	public void init() {
		setIntelligence(1);
		setMood(100);
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public int getMood() {
		return mood;
	}

	public void setMood(int mood) {
		this.mood = mood;
	}

	public mindType getMyType() {
		return myType;
	}

	public void setMyType(String inputString) {
		switch (inputString) {
		case "Simple":
			myType = mindType.Simple;
			break;
		case "Complex":
			myType = mindType.Complex;
			break;

		}
	}

}
