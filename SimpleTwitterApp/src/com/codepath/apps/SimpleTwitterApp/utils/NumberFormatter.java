package com.codepath.apps.SimpleTwitterApp.utils;

import java.util.ArrayList;

import android.util.Log;

public class NumberFormatter {
	private static NumberFormatter numberFormatter;
	private NumberFormatter() {
		
	}
	public static NumberFormatter getInstance() {
		if (numberFormatter == null) {
			numberFormatter = new NumberFormatter();
		}
		return numberFormatter;
	}
	
	public static String getTwitterNumberFormat(String number) {
		Log.i("debug","FORMATTING NUMBER"+number);
		StringBuilder formattedNumber = new StringBuilder("");
		int count = 0;
		if(number.length() == 5) {
			formattedNumber.insert(0, "K");
			int dot=0;
			for (int i = number.length()-3; i >= 0; i-- ) {
				if (dot == 0 && number.charAt(i) == '0') {
					dot++;
				} else if (dot == 0 && number.charAt(i) != '0'){
					formattedNumber.insert(0, number.charAt(i));
					formattedNumber.insert(0, ".");
					dot++;
				} else {
					formattedNumber.insert(0, number.charAt(i));
				}
			}
		} else if(number.length() > 5 && number.length() < 7) {
			formattedNumber.insert(0, "K");
			for (int i = number.length()-4; i >= 0; i-- ) {
					formattedNumber.insert(0, number.charAt(i));
			}
		} else if(number.length() >= 7){
			formattedNumber.insert(0, "M");
			int dot=0;
			for (int i = number.length()-6; i >= 0; i-- ) {
				if (dot == 0 && number.charAt(i) == '0') {
					dot++;
				} else if (dot == 0 && number.charAt(i) != '0'){
					formattedNumber.insert(0, number.charAt(i));
					formattedNumber.insert(0, ".");
					dot++;
				} else {
					formattedNumber.insert(0, number.charAt(i));
				}
			}
		}
		else {
			for (int i = number.length()-1; i >=0; i-- ) {
				if (count < 3) {
					formattedNumber.insert(0, number.charAt(i));
					count++;
				} else {
					formattedNumber.insert(0,",");
					formattedNumber.insert(0,number.charAt(i));
					count = 0;
				}
			}
		}
		return formattedNumber.toString();
	}
}
