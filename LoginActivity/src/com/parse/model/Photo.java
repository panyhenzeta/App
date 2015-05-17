package com.parse.model;

import java.io.Serializable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Photo")
public class Photo extends ParseObject implements Serializable{
	
	public Photo() {
		// A default constructor is required.
	}
	
	public ParseFile getImage(){
		return getParseFile("image");		
	}
	
	public void setImage(ParseFile file){
		put("image", file);
	}
	
	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String title) {
		put("title", title);
	}
	
	public String getRating() {
		return getString("rating");
	}

	public void setRating(String rating) {
		put("rating", rating);
	}
	
	public ParseFile getThumbnail(){
		return getParseFile("thumbnail");
	}
	
	public void setThumbnail(ParseFile file){
		put("thumbnail", file);
	}
	
	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser user) {
		put("user", user);
	}

}
