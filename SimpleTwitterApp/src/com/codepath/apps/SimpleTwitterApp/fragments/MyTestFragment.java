package com.codepath.apps.SimpleTwitterApp.fragments;

import com.codepath.apps.SimpleTwitterApp.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyTestFragment extends Fragment {
	
	TextView tv;

	public static Fragment newInstance(String name) {
		MyTestFragment mtf = new MyTestFragment();
		Bundle b = new Bundle();
		b.putString("Name", name);
		mtf.setArguments(b);
		return mtf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		String name = getArguments().getString("Name");
		View v = inflater.inflate(R.layout.fragment_my_test_fragment, container, false);
		tv = (TextView) v.findViewById(R.id.tvProfileName);
		tv.setText(""+name);
		return v;
	}

}
