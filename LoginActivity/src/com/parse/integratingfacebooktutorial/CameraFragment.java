package com.parse.integratingfacebooktutorial;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

public class CameraFragment extends Fragment {

	public static final String TAG = "CameraFragment";

	private SurfaceView surfaceView;
	private ParseFile photoFile;
	private ImageButton photoButton;
	private int CAMERA_REQUEST = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);
		super.onCreate(savedInstanceState);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			photo = Bitmap.createScaledBitmap(photo, 300,
					300, false);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			
			MediaStore.Images.Media.insertImage(getActivity().getApplicationContext().getContentResolver(), photo, "meal.jpeg" , "yourDescription");
//            
//			Intent cameraIntent = new Intent(getActivity(), EditImage.class);
//			cameraIntent.setType("/*image");
//			cameraIntent.setData(data.getData());
//			// if edit mode is 1, intent is from camera
//			cameraIntent.putExtra("EditMode", true);
//			startActivity(cameraIntent);
			
			byte[] scaledData = bos.toByteArray();
			photoFile = new ParseFile("meal_photo.jpg", scaledData);
			photoFile.saveInBackground(new SaveCallback() {

				public void done(ParseException e) {
					if (e != null) {
						Toast.makeText(getActivity(),
								"Error saving: " + e.getMessage(),
								Toast.LENGTH_LONG).show();
					} else {
						addPhotoToMealAndReturn(photoFile);
					}
				}
			});
		}
	}

	/*
	 * ParseQueryAdapter loads ParseFiles into a ParseImageView at whatever size
	 * they are saved. Since we never need a full-size image in our app, we'll
	 * save a scaled one right away.
	 */
	
	/*
	 * Once the photo has saved successfully, we're ready to return to the
	 * NewMealFragment. When we added the CameraFragment to the back stack, we
	 * named it "NewMealFragment". Now we'll pop fragments off the back stack
	 * until we reach that Fragment.
	 */
	private void addPhotoToMealAndReturn(ParseFile photoFile) {
		((NewMealActivity) getActivity()).getCurrentMeal().setPhotoFile(
				photoFile);
		FragmentManager fm = getActivity().getFragmentManager();
		fm.popBackStack("NewMealFragment",
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	
}
