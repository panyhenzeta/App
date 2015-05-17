package com.parse.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Like")
public class Like extends ParseObject {
	   public Like() {
	        //empty constructor required for parseobject
	    }

	    public void setPhoto(ParseFile photo) {
	        put("photo", photo);
	    }
	    
	    public ParseFile getPhoto() {
	        return getParseFile("photo");
	    }
	    
	    public String getObjId(){
			return getString("objId");
		}
		
		public void setObjId (String obj){
			put("objId", obj);
		}

	    public void setUserLike(ParseUser user) {
	        put("user", user);
	    }	   

	    public ParseUser getUser() {
	        return getParseUser("user");
	    }

}
