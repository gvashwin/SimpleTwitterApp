package com.codepath.apps.SimpleTwitterApp.fragments;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ComposeTweetFragment extends DialogFragment {
	View view;
	ImageView ivAuthorImg;
	TextView tvAuthorName;
	TextView tvAuthorTwitterHandle;
	TextView tvCharsRemaining;
	Button btnPostTweet;
	EditText etTweetText;
	private final int MAX_CHAR = 140;
	ImageLoader imgLoader = ImageLoader.getInstance();
	
	private ComposeTweetFragment() {
	}
	
	public static ComposeTweetFragment getInstance() {
		ComposeTweetFragment dialog = new ComposeTweetFragment();
		Bundle args = new Bundle();
		dialog.setArguments(args);
		return dialog;
	}
	
	public interface PostTweetListener {
		public void postComposedTweet(String tweetText);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_compose_tweet, container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		ivAuthorImg = (ImageView) view.findViewById(R.id.ivComposeTweetAuthorImg);
		tvAuthorName = (TextView) view.findViewById(R.id.tvComposeTweetAuthorName);
		tvAuthorTwitterHandle = (TextView) view.findViewById(R.id.tvComposeTweetAuthorHandle);
		tvCharsRemaining = (TextView) view.findViewById(R.id.charsRemaiing);
		btnPostTweet = (Button) view.findViewById(R.id.btnPostTweet);
		etTweetText = (EditText) view.findViewById(R.id.etTweetText);
		Bundle args = getArguments();
		String authorName = args.getString("name");
		String authorHandle = args.getString("handle");
		String authorImgUrl = args.getString("imgUrl");
		String replyTo = args.getString("replyTo");
		if(authorName == null || authorHandle == null || authorImgUrl == 	null ) {
			String url = TwitterApplication.accountImgUrl.replace("_normal", "");
			imgLoader.displayImage(url, ivAuthorImg);
			tvAuthorName.setText(TwitterApplication.accountName);
			tvAuthorTwitterHandle.setText(TwitterApplication.accountHandle);
		} else {
			String url = authorImgUrl.replace("_normal", "");
			imgLoader.displayImage(url, ivAuthorImg);
			tvAuthorName.setText(authorName);
			tvAuthorTwitterHandle.setText(authorHandle);
		}
		if(args.getString("replyTo")!=null) {
			etTweetText.setText(replyTo+" ");
			etTweetText.setSelection(etTweetText.getText().toString().length());
		}
				
		setupListener();
		return view;
	}

	private void setupListener() {
		etTweetText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//Nothing to do
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				//Nothing to do
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				int tweetLen = etTweetText.getText().toString().length();
				int charsLeft = MAX_CHAR - tweetLen;
				
				if(charsLeft <= 10) {
					tvCharsRemaining.setTextColor(Color.RED);
				} else if (charsLeft > 10 && charsLeft <= 40) {
					tvCharsRemaining.setTextColor(Color.parseColor("#FA9C05"));
				} else {
					tvCharsRemaining.setTextColor(Color.parseColor("#000000"));
				}
				tvCharsRemaining.setText(""+charsLeft);
				
			}
			
		});
		btnPostTweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("debug", "button clicked to post tweet");
				PostTweetListener homeActivity = (PostTweetListener) getActivity();
				homeActivity.postComposedTweet(etTweetText.getText().toString());
				dismiss();
			}
			
		});
	}


}
