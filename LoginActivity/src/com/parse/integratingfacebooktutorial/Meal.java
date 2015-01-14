package com.parse.integratingfacebooktutorial;

import java.io.Serializable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Meal 
 */

@ParseClassName("Meal")
public class Meal extends ParseObject implements Serializable{

	public Meal() {
		// A default constructor is required.
	}

	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String title) {
		put("title", title);
	}

	public ParseUser getAuthor() {
		return getParseUser("author");
	}

	public void setAuthor(ParseUser author) {
		put("author", author);
	}

	public String getUsername() {
		return getString("username");
	}

	public void setUsername(String username) {
		put("username", username);
	}

	public String getRating() {
		return getString("rating");
	}

	public void setRating(String rating) {
		put("rating", rating);
	}

	public ParseFile getPhotoFile() {
		return getParseFile("photo");
	}

	public void setPhotoFile(ParseFile file) {
		put("photo", file);
	}

}
