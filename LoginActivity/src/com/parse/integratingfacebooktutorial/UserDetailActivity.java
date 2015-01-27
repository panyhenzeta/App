package com.parse.integratingfacebooktutorial;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class UserDetailActivity extends Activity {

	private ParseUser currentUser;
	private ParseUser user;
	private UserAdapter mainAdapter;
	private TextView username;
	private ProfilePictureView userProfilePictureView;
	private SwipeMenuListView userImageList;
	private MealHelper mealHelper;
	private List<ApplicationInfo> mAppList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);

		mAppList = getPackageManager().getInstalledApplications(0);
		currentUser = ParseUser.getCurrentUser();
		user = MealHelper.getMealUser();

		try {
			user.fetchIfNeeded();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		username = (TextView) findViewById(R.id.userName);
		userProfilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
		userImageList = (SwipeMenuListView) findViewById(R.id.liste);

		// Delete option of current user's meals
		if (user == currentUser) {
			// Swipe List View
			SwipeMenuCreator creator = new SwipeMenuCreator() {

				@Override
				public void create(SwipeMenu menu) {
					// create "delete" item
					SwipeMenuItem deleteItem = new SwipeMenuItem(
							getApplicationContext());
					// set item background
					deleteItem.setBackground(new ColorDrawable(Color
							.parseColor("#990000")));
					// set item width
					deleteItem.setWidth(dp2px(90));
					// set a icon
					deleteItem.setIcon(R.drawable.ic_delete);
					// add to menu
					menu.addMenuItem(deleteItem);
				}
			};
			// set creator
			userImageList.setMenuCreator(creator);
			userImageList
					.setOnMenuItemClickListener(new OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(int position,
								SwipeMenu menu, int index) {
							ApplicationInfo item = mAppList.get(position);
							switch (index) {
							case 0:
								Meal selectedFromList = (Meal) (userImageList
										.getItemAtPosition(position));
								selectedFromList.deleteInBackground();
								updateMealList();
								break;
							}
							return false;
						}
					});
			// set SwipeListener
			userImageList.setOnSwipeListener(new OnSwipeListener() {

				@Override
				public void onSwipeStart(int position) {
					// swipe start
				}

				@Override
				public void onSwipeEnd(int position) {
					// swipe end
				}
			});
		}

		setUserProfile();
		mainAdapter = new UserAdapter(this);

		// Default view is all current user's meals
		userImageList.setAdapter(mainAdapter);
	}

	private void setUserProfile() {
		userProfilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
		username = (TextView) findViewById(R.id.userName);

		username.setText(user.getUsername());
		if (ParseFacebookUtils.isLinked(user)) {
			Request request = Request.newMeRequest(
					ParseFacebookUtils.getSession(),
					new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							if (user != null) {
								String facebookId = user.getId();
								userProfilePictureView.setProfileId(facebookId);
							} else
								userProfilePictureView.setProfileId(null);
						}
					});
			request.executeAsync();
		}
	}

	protected void updateMealList() {
		mainAdapter.loadObjects();
		userImageList.setAdapter(mainAdapter);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}
