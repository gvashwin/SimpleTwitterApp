package com.codepath.apps.SimpleTwitterApp.adapters;

import java.util.List;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.activities.ProfileViewActivity;
import com.codepath.apps.SimpleTwitterApp.models.Message;
import com.codepath.apps.SimpleTwitterApp.models.Tweet;
import com.codepath.apps.SimpleTwitterApp.utils.DateTimeFormatter;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageArrayAdapter extends ArrayAdapter<Message> {
	
	ImageLoader imgLoader;
	DateTimeFormatter dtf;
	private static class ViewHolder {
		ImageView ivMessageSenderImg;
		TextView tvSenderName;
		TextView tvSenderHandle;
		TextView tvTimeStamp;
		TextView tvMessageText;
	}
	public MessageArrayAdapter(Context context, List<Message> messages) {
		super(context, R.layout.message_item_view, messages);
		imgLoader = ImageLoader.getInstance();
		dtf = DateTimeFormatter.getInstance();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message msg = getItem(position);
		final ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item_view, parent, false);
			viewHolder.ivMessageSenderImg = (ImageView) convertView.findViewById(R.id.ivMessageProfilePic);
			viewHolder.tvSenderName = (TextView) convertView.findViewById(R.id.tvMessageSenderName);
			viewHolder.tvSenderHandle = (TextView) convertView.findViewById(R.id.tvMessageSenderHandle);
			viewHolder.tvTimeStamp = (TextView) convertView.findViewById(R.id.tvMessageTimeStamp);
			viewHolder.tvMessageText = (TextView) convertView.findViewById(R.id.tvMessageText);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String senderImgUrl = msg.getSender().getImgUrl().replace("_normal", "");
		imgLoader.displayImage(senderImgUrl, viewHolder.ivMessageSenderImg);
		viewHolder.tvSenderName.setText(msg.getSender().getName());
		viewHolder.tvSenderHandle.setText("@"+msg.getSender().getHandle());
		viewHolder.tvMessageText.setText(msg.getText());
		viewHolder.tvTimeStamp.setText(""+dtf.formatDateTime(msg.getTimeStamp()));
		return convertView;
	}
	

}
