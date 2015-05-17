package com.parse.integratingfacebooktutorial;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.parse.ParseUser;

public class MealListActivity extends Activity {

	private StreamAdapter mainAdapter;
	private FavoriteMealAdapter favoritesAdapter;
	private SwipeMenuListView swipeImageList;
	private List<ApplicationInfo> mAppList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stream_main_list);

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);
		getActionBar().setCustomView(R.layout.actionbar_settings);
		getActionBar().getCustomView().setOnClickListener(
				new OnClickListener() {
					@Override
					// User Logout
					public void onClick(View v) {
						ParseUser.logOut();
						userLogout();
					}
				});

		// Subclasses of ParseQueryAdapter
		mainAdapter = new StreamAdapter(this);
		favoritesAdapter = new FavoriteMealAdapter(this);

		mAppList = getPackageManager().getInstalledApplications(0);
		swipeImageList = (SwipeMenuListView) findViewById(R.id.mainList);

		// Default view is all meals
		updateMealList();
		swipeImageList.setAdapter(mainAdapter);

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
				deleteItem.setIcon(R.drawable.ic_user);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		swipeImageList.setMenuCreator(creator);
		swipeImageList
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						ApplicationInfo item = mAppList.get(position);
						switch (index) {
						case 0:							
							Meal selectedFromList = (Meal) (swipeImageList
									.getItemAtPosition(position));
							launchIntent(selectedFromList.getAuthor());
							break;
						}
						return false;
					}
				});
		// set SwipeListener
		swipeImageList.setOnSwipeListener(new OnSwipeListener() {

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

	protected void userLogout() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_meal_list, menu);
		return true;
	}

	/*
	 * Posting meals and refreshing the list will be controlled from the Action
	 * Bar.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {		
		case R.id.user_details: {
			Intent userIntent = new Intent(this, UserDetailActivity.class);
			MealHelper.setMealUser(ParseUser.getCurrentUser());
			startActivity(userIntent);
			break;
		}
		case R.id.action_refresh: {
			updateMealList();
			break;
		}

		case R.id.action_favorites: {
			showFavorites();
			break;
		}

		case R.id.action_new: {
			newMeal();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateMealList() {
		mainAdapter.loadObjects();
		swipeImageList.setAdapter(mainAdapter);
	}

	private void showFavorites() {
		favoritesAdapter.loadObjects();
		swipeImageList.setAdapter(favoritesAdapter);
	}

	private void newMeal() {
		Intent i = new Intent(this, NewMealActivity.class);
		startActivityForResult(i, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			// If a new post has been added, update
			// the list of posts
			updateMealList();
		}
	}

	private void launchIntent(ParseUser user) {
		Intent intent = new Intent(this, UserDetailActivity.class);
		MealHelper.setMealUser(user);
		startActivity(intent);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}