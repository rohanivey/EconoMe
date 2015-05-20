package com.rohan.econome;

public class Chicken extends ComponentEntity {

	Chicken(String inputID, float inputX, float inputY, Level inputLevel) {
		super(inputID, inputLevel);
		System.out.println("Chicken super constructor ending");
	}

}
