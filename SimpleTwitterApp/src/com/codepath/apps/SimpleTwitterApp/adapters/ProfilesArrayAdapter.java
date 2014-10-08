package com.codepath.apps.SimpleTwitterApp.adapters;

import java.util.List;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.models.Profile;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilesArrayAdapter extends ArrayAdapter<Profile> {
	ImageLoader imgLoader;
	
	public ProfilesArrayAdapter(Context context, List<Profile> profiles) {
		super(context, R.layout.profile_item_view, profiles);
		imgLoader = ImageLoader.getInstance();
	}
	
	private static class ViewHolder {
		ImageView ivProfileImg;
		TextView tvProfileName;
		TextView tvProfileTwitterHandle;
		TextView tvProfileBio;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Profile p = getItem(position);
		final ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_item_view, parent, false);
			viewHolder.ivProfileImg = (ImageView) convertView.findViewById(R.id.ivProfileImg);
			viewHolder.tvProfileName = (TextView) convertView.findViewById(R.id.tvProfileName);
			viewHolder.tvProfileTwitterHandle = (TextView) convertView.findViewById(R.id.tvProfileHandle);
			viewHolder.tvProfileBio = (TextView) convertView.findViewById(R.id.tvProfileBio);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String hqImgUrl = p.getImgUrl().replace("_normal", "");
		imgLoader.displayImage(hqImgUrl, viewHolder.ivProfileImg);
		viewHolder.tvProfileName.setText(p.getName());
		viewHolder.tvProfileTwitterHandle.setText("@"+p.getHandle());
		viewHolder.tvProfileBio.setText(p.getBio());
		return convertView;
	}
}
