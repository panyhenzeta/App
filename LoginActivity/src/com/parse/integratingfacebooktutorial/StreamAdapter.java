package com.parse.integratingfacebooktutorial;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.model.Like;

/*
 * The FavoriteMealAdapter is an extension of ParseQueryAdapter
 * that has a custom layout for favorite meals, including a 
 * bigger preview image, the meal's rating, and a "favorite"
 * star. 
 */

public class StreamAdapter extends ParseQueryAdapter<Meal> {
	public Like like = new Like();
	
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
	public View getItemView(final Meal meal, View v, ViewGroup parent) {

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
		
		ImageView star = (ImageView) v.findViewById(R.id.image_star);
		star.setImageResource(meal.getStar());
		
		TextView titleTextView = (TextView) v.findViewById(R.id.text1);
		titleTextView.setText(meal.getTitle());
		
		TextView ratingTextView = (TextView) v.findViewById(R.id.image_rating);
		ratingTextView.setText(meal.getRating());
		
		//Like
		star.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				meal.setStar(R.drawable.ic_action_favorite_pressed);
				meal.saveInBackground();
				
				Like like = new Like();
				like.setPhoto(meal.getPhotoFile());	
				like.setObjId(meal.getObjectId());
				like.setUserLike(meal.getAuthor());
				like.saveInBackground();				
			}
		});

//		final ImageView star = (ImageView) v.findViewById(R.id.image_star);
//		star.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				star.setImageResource(R.drawable.ic_action_favorite_pressed);
//				
//				Like like = new Like();
//				like.setPhoto(meal.getPhotoFile());
//				like.setUserLike(meal.getAuthor());
//				like.saveInBackground();
//			}
//		});

		return v;
	}

}