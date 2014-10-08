package com.codepath.apps.SimpleTwitterApp.fragments;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.fragments.ComposeTweetFragment.PostTweetListener;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RetweetPromptFragment extends DialogFragment {
	
	Button btnCancel;
	Button btnRetweet;
	View view;
	String tweetId;
	private RetweetPromptFragment() {
	}
	
	public static RetweetPromptFragment getInstance() {
		RetweetPromptFragment dialog = new RetweetPromptFragment();
		Bundle args = new Bundle();
		dialog.setArguments(args);
		return dialog;
	}
	
	public interface PostRetweetListener {
		public void postRetweet(String id);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_retweet_prompt, container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		btnCancel = (Button) view.findViewById(R.id.btnCancelRetweet);
		btnRetweet = (Button) view.findViewById(R.id.btnRetweet);
		Bundle args = getArguments();
		tweetId = new String(args.getString("tweetId"));
		setupListener();
		
		return view;
	}

	private void setupListener() {
		btnRetweet.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(), "RetweetButtonClicked :"+tweetId, Toast.LENGTH_SHORT).show();
				PostRetweetListener baseActivity = (PostRetweetListener) getActivity();
				baseActivity.postRetweet(tweetId);
				dismiss();	
			}
			
		});
		btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(), "CancelButtonClicked :"+tweetId, Toast.LENGTH_SHORT).show();
				dismiss();	
			}
			
		});
		
	}

}
