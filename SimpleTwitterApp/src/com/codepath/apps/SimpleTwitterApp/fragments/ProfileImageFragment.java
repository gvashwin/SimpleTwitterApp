package com.codepath.apps.SimpleTwitterApp.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProfileImageFragment extends Fragment {
	
	private ImageView ivProfilePic;
	private TextView tvProfileName;
	private TextView tvProfileHndl;
	private RelativeLayout rlProfileImgFragment;
	private ImageView ivVerifiedIcon;
	
	private ImageLoader imgLoader = ImageLoader.getInstance();
	public static Fragment newInstance() {
		ProfileImageFragment profileImgFrag = new ProfileImageFragment();
		Bundle b = new Bundle();
		profileImgFrag.setArguments(b);
		return profileImgFrag;
	}

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile_image, container, false);
		
		ivProfilePic = (ImageView) v.findViewById(R.id.ivProfilePic);
		//imgLoader.displayImage(getArguments().getString("ImgUrl"), ivProfilePic);
		
		tvProfileName = (TextView) v.findViewById(R.id.tvProfileName);
		//tvProfileName.setText(profileName);
		
		tvProfileHndl = (TextView) v.findViewById(R.id.tvProfileTwitterHndl);
		//tvProfileHndl.setText("@"+profileTwitterHndl);
		
		rlProfileImgFragment = (RelativeLayout) v.findViewById(R.id.rlFragmentProfileImg);
		ivVerifiedIcon = (ImageView) v.findViewById(R.id.ivVerifiedIcon);
		ivVerifiedIcon.setVisibility(ImageView.GONE);
		return v;
	}
	
	public void updateView(String name, String hndl, String url, String bannerUrl, Boolean isVerfied) {
		String hqUrl = url.replace("_normal", "");
		imgLoader.displayImage(hqUrl, ivProfilePic);
		tvProfileName.setText(name);
		if(hndl.startsWith("@")) {
			tvProfileHndl.setText(hndl); 
		} else {
			tvProfileHndl.setText("@"+hndl); 
		}
		if (isVerfied) {
			ivVerifiedIcon.setVisibility(ImageView.VISIBLE);
		}
		imgLoader.loadImage(bannerUrl, new SimpleImageLoadingListener() {
			@SuppressLint("NewApi") @SuppressWarnings("deprecation")
			@Override	
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					rlProfileImgFragment.setBackground(new BitmapDrawable(loadedImage));
				}
			});
		Log.i("debug", "updating views done "+name+ " "+hndl+" "+url+"banner url +"+bannerUrl);
	}

}
