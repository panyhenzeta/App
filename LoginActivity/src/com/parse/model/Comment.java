package com.parse.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

	public Comment() {
		// empty constructor is required
	}
	
	public String getComment() {
		return getString("comment");
	}

	public void setComment(String c) {
		put("comment", c);
	}
	
	public ParseFile getPhotoCommented() {
		return getParseFile("photoCommented");
	}

	public void setPhotoCommented(ParseFile pf) {
		put("photoCommented", pf);
	}
	
	public ParseUser getCommentingUser() {
		return getParseUser("comUser");
	}

	public void setCommentingUser(ParseUser user) {
		put("comUser", user);
	}
}
