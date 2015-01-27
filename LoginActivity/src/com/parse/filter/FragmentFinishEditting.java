package com.parse.filter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.integratingfacebooktutorial.Meal;
import com.parse.integratingfacebooktutorial.NewMealActivity;
import com.parse.integratingfacebooktutorial.R;
import com.parse.integratingfacebooktutorial.R.array;
import com.parse.integratingfacebooktutorial.R.id;
import com.parse.integratingfacebooktutorial.R.layout;

public class FragmentFinishEditting extends Fragment {
	private Button saveButton;
	private Button cancelButton;
	private TextView mealName;
	private Spinner mealRating;
	private ParseImageView mealPreview;
	Meal nma = new Meal();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle SavedInstanceState) {
		View v = inflater.inflate(R.layout.finnish_editting, parent, false);

		mealName = ((EditText) v.findViewById(R.id.meal_name));

		// The mealRating spinner lets people assign favorites of meals they've
		// eaten.
		// Meals with 4 or 5 ratings will appear in the Favorites view.
		mealRating = ((Spinner) v.findViewById(R.id.rating_spinner));
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.ratings_array,
						android.R.layout.simple_spinner_dropdown_item);
		mealRating.setAdapter(spinnerAdapter);


		saveButton = ((Button) v.findViewById(R.id.save_button));
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Meal meal = ((NewMealActivity) getActivity()).getCurrentMeal();
				Meal meal = new Meal();
				System.out.println("ffe icindeki "+meal.getPhotoFile().getName());
				// When the user clicks "Save," upload the meal to Parse
				// Add data to the meal object:
				meal.setTitle(mealName.getText().toString());

				// Associate the meal with the current user
				meal.setAuthor(ParseUser.getCurrentUser());
				
				//Associate the meal with the current users name
				meal.setUsername(ParseUser.getCurrentUser().getUsername());

				// Add the rating
				meal.setRating(mealRating.getSelectedItem().toString());

				// If the user added a photo, that data will be
				// added in the CameraFragment

				// Save the meal and return
				meal.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							getActivity().setResult(Activity.RESULT_OK);
							getActivity().finish();
						} else {
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Error saving: " + e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}

				});

			}
		});

		cancelButton = ((Button) v.findViewById(R.id.cancel_button));
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().setResult(Activity.RESULT_CANCELED);
				getActivity().finish();
			}
		});

		// Until the user has taken a photo, hide the preview
		mealPreview = (ParseImageView) v.findViewById(R.id.meal_preview_image);
		mealPreview.setVisibility(View.INVISIBLE);

		return v;
	}

	/*
	 * All data entry about a Meal object is managed from the NewMealActivity.
	 * When the user wants to add a photo, we'll start up a custom
	 * CameraFragment that will let them take the photo and save it to the Meal
	 * object owned by the NewMealActivity. Create a new CameraFragment, swap
	 * the contents of the fragmentContainer (see activity_new_meal.xml), then
	 * add the NewMealFragment to the back stack so we can return to it when the
	 * camera is finished.
	 */

	/*
	 * On resume, check and see if a meal photo has been set from the
	 * CameraFragment. If it has, load the image in this fragment and make the
	 * preview image visible.
	 */
	@Override
	public void onResume() {
		super.onResume();
		//ParseFile photoFile = ((NewMealActivity) getActivity())
				//.getCurrentMeal().getPhotoFile();
		ParseFile photoFile = nma.getPhotoFile();
		if (photoFile != null) {
			mealPreview.setParseFile(photoFile);
			mealPreview.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					mealPreview.setVisibility(View.VISIBLE);
				}
			});
		}
	}
	
	public void addPhotoToMealAndReturn(ParseFile photoFile) {
		((NewMealActivity) getActivity()).getCurrentMeal().setPhotoFile(
				photoFile);
		getActivity().finish();
	}


}
