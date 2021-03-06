package com.parse.integratingfacebooktutorial;


import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.integratingfacebooktutorial.Meal;


public class IntegratingFacebookTutorialApplication extends Application {

  static final String TAG = "MyApp";

  @Override
  public void onCreate() {
    super.onCreate();
    
    ParseObject.registerSubclass(Meal.class);
    Parse.initialize(this, 
    		"Q1PGvrDQNflpPL2Njn12MKkrLQVG9g5CHp5ZE08m", 
    		"quweXCTD3QXxEJoqHmH8HmNfpD1s4HLdMPzR9kW5"
    );
  }
}
