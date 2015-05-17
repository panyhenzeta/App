package com.parse.integratingfacebooktutorial;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/*
 * The FavoriteMealAdapter is an extension of ParseQueryAdapter
 * that has a custom layout for favorite meals, including a 
 * bigger preview image, the meal's rating, and a "favorite"
 * star. 
 */

public class UserAdapter extends ParseQueryAdapter<Meal> {

	private static ParseUser user;

	public UserAdapter(Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<Meal>() {
			public ParseQuery<Meal> create() {
				// Here we can configure a ParseQuery to display
				// only user's meals.
				user = MealHelper.getMealUser();
				try {
					user.fetchIfNeeded();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				ParseQuery query = new ParseQuery("Meal");
				query.whereContains("username", user.getUsername());
				return query;
			}
		});
	}

	@Override
	public View getItemView(Meal meal, View v, ViewGroup parent) {

		if (v == null) {
			v = View.inflate(getContext(), R.layout.stream_list, null);
		}

		super.getItemView(meal, v, parent);

		ParseImageView mealImage = (ParseImageView) v.findViewById(R.id.icon);
		ParseFile photoFile = meal.getParseFile("photo");
		if (photoFile != null) {
			mealImage.setParseFile(photoFile);
			mealImage.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					// nothing to do
				}
			});
		}

		TextView titleTextView = (TextView) v.findViewById(R.id.text1);
		titleTextView.setText(meal.getTitle());

		TextView ratingTextView = (TextView) v.findViewById(R.id.image_rating);
		ratingTextView.setText(meal.getRating());
		return v;
	}

}