package com.codepath.apps.SimpleTwitterApp.fragments;

import com.codepath.apps.SimpleTwitterApp.R;
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
import android.util.Log;

public class ProfileInfoFragment extends Fragment {

	TextView tvProfileBio;
	RelativeLayout rlPofileInfoFragment;
	private ImageLoader imgLoader = ImageLoader.getInstance();
	public static Fragment newInstance() {
		ProfileInfoFragment profileInfoFrag = new ProfileInfoFragment();
		Bundle b = new Bundle();
		//b.putString("Name", name);
		profileInfoFrag.setArguments(b);
		return profileInfoFrag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//return super.onCreateView(inflater, container, savedInstanceState);
		//String name = getArguments().getString("Name");
		View v = inflater.inflate(R.layout.fragment_profile_info, container, false);
		tvProfileBio = (TextView) v.findViewById(R.id.tvProfileBio);
		rlPofileInfoFragment = (RelativeLayout) v.findViewById(R.id.rlFragmentPofileInfo);
		return v;
	}
	
	public void updateView(String bio, String bannerUrl) {
		tvProfileBio.setText(bio);
		imgLoader.loadImage(bannerUrl, new SimpleImageLoadingListener() {
			@SuppressLint("NewApi") @SuppressWarnings("deprecation")
			@Override	
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					rlPofileInfoFragment.setBackground(new BitmapDrawable(loadedImage));
				}
			});
		Log.i("debug", "updating views done "+bio);
	}

}
