package com.ricm.arboretum;

import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.app.Activity;
import android.content.Intent;

public class DescriptionArbo extends Activity {

	private WebView webview;
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//Creation de la webview
		webview = new WebView(this);
		setContentView(webview);
		//pour autoriser le zoom
		WebSettings webSettings = webview.getSettings(); 
		webSettings.setBuiltInZoomControls(true);
		//chargement depuis la racine de l'appareil
		webview.loadUrl("file:///" + Environment.getExternalStorageDirectory().toString() + "/Arboretum/Webview/void.html");

	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
