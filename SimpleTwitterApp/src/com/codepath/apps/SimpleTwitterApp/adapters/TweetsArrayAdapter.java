package com.codepath.apps.SimpleTwitterApp.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.activities.ProfileViewActivity;
import com.codepath.apps.SimpleTwitterApp.fragments.ComposeTweetFragment;
import com.codepath.apps.SimpleTwitterApp.models.Tweet;
import com.codepath.apps.SimpleTwitterApp.utils.DateTimeFormatter;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
	ImageLoader imgLoader;
	DateTimeFormatter dtf;
	public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, R.layout.tweet_item_view, tweets);
		imgLoader = ImageLoader.getInstance();
		dtf = DateTimeFormatter.getInstance();
	}
	private static class ViewHolder {
		ImageView ivRetweetIcon;
		ImageView ivAuthorImg;
		TextView tvReTweetUserName;
		TextView tvAuthorName;
		TextView tvAuthorTwitterHandle;
		TextView tvTimeStamp;
		TextView tvTweetText;
		ImageView ivMedia;
		Button btnReply2Tweet;
		Button btnRetweetCount;
		Button btnFavCount;
		String authorId;
		String authorImgUrl;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet t = getItem(position);
		final ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item_view, parent, false);
			viewHolder.ivRetweetIcon = (ImageView) convertView.findViewById(R.id.ivRetweetIcon);
			viewHolder.ivAuthorImg = (ImageView) convertView.findViewById(R.id.ivTweetItemUserImg);
			viewHolder.tvReTweetUserName = (TextView) convertView.findViewById(R.id.tv_RetweetUserName);
			viewHolder.tvAuthorName = (TextView) convertView.findViewById(R.id.tvTweetItemProfileName);
			viewHolder.tvAuthorTwitterHandle = (TextView) convertView.findViewById(R.id.tvTweetItemUserHandle);
			viewHolder.tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
			viewHolder.tvTweetText = (TextView) convertView.findViewById(R.id.tvTweetText);
			viewHolder.ivMedia = (ImageView) convertView.findViewById(R.id.ivMedia);
			viewHolder.btnReply2Tweet = (Button) convertView.findViewById(R.id.btnInlineReply);
			viewHolder.btnRetweetCount = (Button) convertView.findViewById(R.id.btnInlineRetweet);
			viewHolder.btnFavCount = (Button) convertView.findViewById(R.id.btnInlineFav);
			viewHolder.ivAuthorImg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(v.getContext(), ProfileViewActivity.class);
					i.putExtra("name", viewHolder.tvAuthorName.getText());
					i.putExtra("hndl", viewHolder.tvAuthorTwitterHandle.getText());
					v.getContext().startActivity(i); 
				}
				
			});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.ivRetweetIcon.setVisibility(View.GONE);
		String normalImgUrl = t.getAuthor().getImgUrl();
		String imgUrl = normalImgUrl.replace("_normal", "");
		imgLoader.displayImage(imgUrl , viewHolder.ivAuthorImg);
		viewHolder.tvReTweetUserName.setVisibility(View.GONE);
		viewHolder.tvAuthorName.setText(t.getAuthor().getName());
		viewHolder.tvAuthorTwitterHandle.setText("@"+t.getAuthor().getHandle());
		viewHolder.tvTweetText.setText(t.getText());
		if (t.getMediaUrl()!=null) {
			//Log.i("debug", "Making media visibile"+t.getMediaUrl());
			viewHolder.ivMedia.setVisibility(ImageView.VISIBLE);
			imgLoader.displayImage(t.getMediaUrl(), viewHolder.ivMedia);
		} else {
			//Log.i("debug", "Making media gone" + t.getMediaUrl());
			viewHolder.ivMedia.setVisibility(View.GONE);
		}
		viewHolder.btnFavCount.setText(""+t.getFavCount());
		viewHolder.btnRetweetCount.setText(""+t.getRetweetCount());
		String formattedTime = dtf.formatDateTime(t.getTimeStamp());
		viewHolder.tvTimeStamp.setText(dtf.getTwitterFormattedDateTime(formattedTime));
		setupListeners(viewHolder);
		return convertView;
	}
	
	private void setupListeners(final ViewHolder vh) {
		vh.btnReply2Tweet.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				FragmentManager fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
				ComposeTweetFragment ctf = ComposeTweetFragment.getInstance();
				Bundle args = ctf.getArguments();
				args.putString("replyTo", vh.tvAuthorTwitterHandle.getText().toString());
		        
				ctf.setArguments(args);
				ctf.show(fm, "dialog");
				
			}
			
		});
	}
}
