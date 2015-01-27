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

/*
 * The FavoriteMealAdapter is an extension of ParseQueryAdapter
 * that has a custom layout for favorite meals, including a 
 * bigger preview image, the meal's rating, and a "favorite"
 * star. 
 */

public class StreamAdapter extends ParseQueryAdapter<Meal> {
	
	public StreamAdapter(Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<Meal>() {
			public ParseQuery<Meal> create() {
				// Here we can configure a ParseQuery to display
				// only last-uploaded meals.
				ParseQuery query = new ParseQuery("Meal");
				query.orderByDescending("createdAt");
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

		TextView usernameTextView = (TextView) v.findViewById(R.id.tvUsername);
		usernameTextView.setText(meal.getUsername());
		
		TextView titleTextView = (TextView) v.findViewById(R.id.text1);
		titleTextView.setText(meal.getTitle());
		TextView ratingTextView = (TextView) v
				.findViewById(R.id.image_rating);
		ratingTextView.setText(meal.getRating());
		return v;
	}

}
