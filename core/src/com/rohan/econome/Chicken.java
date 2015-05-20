package com.rohan.econome;

public class Chicken extends ComponentEntity {

	Chicken(float inputX, float inputY, Level inputLevel) {
		super("Chicken", inputLevel);
		System.out.println("Chicken super constructor ending");
	}

}
