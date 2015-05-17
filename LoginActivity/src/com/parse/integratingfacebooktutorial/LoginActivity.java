package com.parse.integratingfacebooktutorial;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	private Dialog progressDialog;
	private TextView mErrorField;
	private EditText mUsernameField;
	private EditText mPasswordField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().hide();
		setContentView(R.layout.main);

		mUsernameField = (EditText) findViewById(R.id.login_username);
		mPasswordField = (EditText) findViewById(R.id.login_password);
		mErrorField = (TextView) findViewById(R.id.error_messages);

		// Check if there is a currently logged in user
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null)) {
			// Go to the user info activity
			showUserDetailsActivity();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	public void onLoginClick(View v) {
		progressDialog = ProgressDialog.show(LoginActivity.this, "",
				"Logging in...", true);

		List<String> permissions = Arrays.asList("public_profile", "email");
		// NOTE: for extended permissions, like "user_about_me", your app must
		// be reviewed by the Facebook team
		// (https://developers.facebook.com/docs/facebook-login/permissions/)

		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				progressDialog.dismiss();
				if (user == null) {
					Log.d(IntegratingFacebookTutorialApplication.TAG,
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {

					// Take users facebook firstname and lastname as its
					// username
					makeMeRequest();
					Log.d(IntegratingFacebookTutorialApplication.TAG,
							"User signed up and logged in through Facebook!");
					showUserDetailsActivity();
				} else {
					Log.d(IntegratingFacebookTutorialApplication.TAG,
							"User logged in through Facebook!");
					showUserDetailsActivity();
				}
			}
		});
	}

	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							try {
								ParseUser currentUser = ParseUser
										.getCurrentUser();
								currentUser.setUsername(user.getFirstName() + " "
										+ user.getLastName());
								currentUser.saveInBackground();
							} catch (Exception e) {
								Toast.makeText(LoginActivity.this,
										"json error ", Toast.LENGTH_LONG)
										.show();
							}
						} else if (response.getError() != null) {

							showUserDetailsActivity();
						} else {
							Toast.makeText(LoginActivity.this, "error",
									Toast.LENGTH_LONG).show();

						}
					}

				});
		request.executeAsync();
	}

	private void showUserDetailsActivity() {
		Intent intent = new Intent(this, MealListActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void signIn(final View v) {
		v.setEnabled(false);
		ParseUser.logInInBackground(mUsernameField.getText().toString(),
				mPasswordField.getText().toString(), new LogInCallback() {
					@Override
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							Intent intent = new Intent(LoginActivity.this,
									MealListActivity.class);
							startActivity(intent);
							finish();
						} else {
							// Signup failed. Look at the ParseException to see
							// what happened.
							switch (e.getCode()) {
							case ParseException.USERNAME_TAKEN:
								mErrorField
										.setText("Sorry, this username has already been taken.");
								break;
							case ParseException.USERNAME_MISSING:
								mErrorField
										.setText("Sorry, you must supply a username to register.");
								break;
							case ParseException.PASSWORD_MISSING:
								mErrorField
										.setText("Sorry, you must supply a password to register.");
								break;
							case ParseException.OBJECT_NOT_FOUND:
								mErrorField
										.setText("Sorry, those credentials were invalid.");
								break;
							default:
								mErrorField.setText(e.getLocalizedMessage());
								break;
							}
							v.setEnabled(true);
						}
					}
				});
	}

	public void showRegistration(View v) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
		finish();
	}

}