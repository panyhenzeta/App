package com.parse.integratingfacebooktutorial;

import com.parse.ParseUser;

public final class MealHelper {

	private static ParseUser user;
	
	public static void setMealUser(ParseUser cUser){
		user = cUser;		
	}
	
	public static ParseUser getMealUser(){
		return user;
	}
	
}
