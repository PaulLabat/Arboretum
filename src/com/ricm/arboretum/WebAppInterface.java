package com.ricm.arboretum;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
	Context mContext;
	/** Instantiate the interface and set the context */
	WebAppInterface(Context c) {
		mContext = c;
	}

	/** Show a toast from the web page */
	@JavascriptInterface
	public void readText(String text) {
		//Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		DescriptionArbo.speakOut(text);
		}

}
