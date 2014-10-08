package com.codepath.apps.SimpleTwitterApp.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.TwitterClient;
import com.codepath.apps.SimpleTwitterApp.R.layout;
import com.codepath.apps.SimpleTwitterApp.fragments.ComposeTweetFragment;
import com.codepath.apps.SimpleTwitterApp.fragments.RetweetPromptFragment;
import com.codepath.apps.SimpleTwitterApp.fragments.RetweetPromptFragment.PostRetweetListener;
import com.codepath.apps.SimpleTwitterApp.utils.DateTimeFormatter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ortiz.touch.TouchImageView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedTweetViewActivity extends FragmentActivity implements PostRetweetListener{
	ActionBar actionBar;
	Bundle extras;
	ImageView ivAuthorPic;
	TextView tvAuthorName;
	TextView tvAuthorHandle;
	TextView tvTweetText;
	TextView tvTimeStamp;
	TextView tvRetweetCount;
	TextView tvFavCount;
	Button btnReply;
	Button btnRetweet;
	Button btnFav;	
	Button btnShare;
	String tweetId;
	ImageView ivVerifiedIcon;
	ImageView ivMedia;
	ImageLoader imLoader = ImageLoader.getInstance();
	TwitterClient client = TwitterApplication.getRestClient();
	DateTimeFormatter dtf = DateTimeFormatter.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_tweet_view);
		extras = getIntent().getExtras();
		ivAuthorPic = (ImageView) findViewById(R.id.tvDetailTweetProfileImg);
		tvAuthorName = (TextView) findViewById(R.id.tvDetailTweetProfileName);
		tvAuthorHandle = (TextView) findViewById(R.id.tvDetailViewProfileHandle);
		tvTweetText = (TextView) findViewById(R.id.tvDetaiViewTweetText);
		tvTimeStamp = (TextView) findViewById(R.id.tvDetailViewTimeStamp);
		tvRetweetCount = (TextView) findViewById(R.id.tvDetailViewRetweetCount);
		tvFavCount = (TextView) findViewById(R.id.tvDetailViewFavCount);
		btnReply = (Button) findViewById(R.id.btnDetailViewInlineReply);
		btnRetweet = (Button) findViewById(R.id.btnDetailViewInLineRetweet);
		btnFav = (Button) findViewById(R.id.btnDetailViewInlineFav);
		btnShare = (Button) findViewById(R.id.btnDetailViewInlineShare);
		ivVerifiedIcon = (ImageView) findViewById(R.id.ivVerifiedBadge);
		ivMedia = (ImageView) findViewById(R.id.ivDetailedViewMedia);
		setupActionBar();
		updateView();
		setupListeners();
	}
	
	private void setupListeners() {	
		btnReply.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
				ComposeTweetFragment ctf = ComposeTweetFragment.getInstance();
				Bundle args = ctf.getArguments();
				args.putString("replyTo", tvAuthorHandle.getText().toString());
				ctf.setArguments(args);
				ctf.show(fm, "dialog");
			}
			
		});
		btnRetweet.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
				RetweetPromptFragment rpf = RetweetPromptFragment.getInstance();
				Bundle args = rpf.getArguments();
				args.putString("tweetId", tweetId);
				rpf.setArguments(args);
				rpf.show(fm, "dialog");
			}
			
		});
	}

	private void updateView() {
		String hqImgUrl = extras.getString("authorImgUrl").replace("_normal", "");
		imLoader.displayImage(hqImgUrl, ivAuthorPic);
		tvAuthorName.setText(extras.getString("authorName"));
		tvAuthorHandle.setText("@"+extras.getString("authorHandle"));
		tvTweetText.setText(extras.getString("tweetText"));
		//Toast.makeText(this, "Time Stamp is "+dtf.formatDateTimeinDetailView(extras.getString("timeStamp")), Toast.LENGTH_LONG).show();
		tvTimeStamp.setText(dtf.formatDateTimeinDetailView(extras.getString("timeStamp")));
		tvRetweetCount.setText(""+extras.getInt("retweetCount"));
		tvFavCount.setText(""+extras.getInt("favCount"));
		tweetId = new String(extras.getString("tweetId"));
		if(extras.getString("mediaUrl")!=null) {
			ivMedia.setVisibility(ImageView.VISIBLE);
			imLoader.displayImage(extras.getString("mediaUrl"), ivMedia);
			ivMedia.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(DetailedTweetViewActivity.this, ImageViewActivity.class);
					i.putExtra("mediaUrl", extras.getString("mediaUrl"));
					startActivity(i);
				}
				
			});
		} else {
			ivMedia.setVisibility(ImageView.GONE);
		}
		if(extras.getBoolean("authorVerified")) {
			ivVerifiedIcon.setVisibility(ImageView.VISIBLE);
		} else {
			ivVerifiedIcon.setVisibility(ImageView.GONE);
		}
	}

	private void setupActionBar() {
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(TwitterApplication.D_BLUE_COLOR);
		actionBar.setStackedBackgroundDrawable(TwitterApplication.D_WHITE_COLOR);
		actionBar.setIcon(R.drawable.ic_actionbar_logo);
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Tweet </font>"));
	}

	@Override
	public void postRetweet(String id) {
		//Toast.makeText(this, "retweeting "+id, Toast.LENGTH_SHORT).show();
		client.postRetweet(id, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(Throwable e, String s) {
				Log.i("debug",e.toString());
				Log.i("debug",s.toString());
			}

			@Override
			public void onSuccess(JSONObject jsonObject) {
				Log.i("debug","Posted retweet success:"+jsonObject.toString());
			}
			
		});
	}
}
