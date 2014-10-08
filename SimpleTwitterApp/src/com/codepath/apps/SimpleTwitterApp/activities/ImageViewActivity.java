package com.codepath.apps.SimpleTwitterApp.activities;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.R.layout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ortiz.touch.TouchImageView;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ImageViewActivity extends Activity {
	TouchImageView ivFullMedia;
	ActionBar actionBar;
	ImageLoader imgLoader =  ImageLoader.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		setupActionBar();
		ivFullMedia = (TouchImageView) findViewById(R.id.ivFullImage);
		String url = getIntent().getStringExtra("mediaUrl");
		//Toast.makeText(this, "Loading Image "+url, Toast.LENGTH_LONG).show();
		imgLoader.displayImage(url, ivFullMedia);
	}
	
	private void setupActionBar() {
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(TwitterApplication.D_BLUE_COLOR);
		actionBar.setStackedBackgroundDrawable(TwitterApplication.D_WHITE_COLOR);
		actionBar.setIcon(R.drawable.ic_actionbar_logo);
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Media </font>"));
	}
}
