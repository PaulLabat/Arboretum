package com.ricm.arboretum;

import java.io.File;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.ItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.OverlayList;
import org.mapsforge.core.GeoPoint;
import org.mapsforge.map.reader.header.FileOpenResult;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;


public class Direction2 extends MapActivity implements LocationListener{

	private MapView mapView;
	private LocationManager locationManager;
	private SensorManager sm;
	private Sensor my_sensor;
	private OverlayItem item;
	private OverlayItem item2;
	private OverlayItem item3;
	private GeoPoint arbo;
	//private GeoPoint test;

	/**
	 * Inner Class
	 * Elle sert pour la gestion des listes d'items, concretement, les markers jumele a une geolocalisation
	 * elle est obligatoire afin de redefinir ontap. En fait, quand l'utilisateur appuyera sur un marqueur,
	 * si celui-ci a ete stocke dans l'arraylist de type MyItemizedOverlay, le code va aller verifier si le doigt 
	 * a toucher un des items de la liste et verifier ontap.
	 * C'est la la grande difference avec mapsforge 0.3.1, ici on cree les markers de tout pieces, et on les stock apres 
	 * leur avoir assigner un geopoint
	 */
	private class MyItemizedOverlay extends ArrayItemizedOverlay {


		//Obligation de sauvegarde notre contexte
		private final Context context;

		/**
		 * Constructs a new MyItemizedOverlay.
		 * 
		 * @param defaultMarker
		 *            the default marker (may be null).
		 * @param context
		 *            the reference to the application context.
		 */		
		//constructeur de l'innerclass
		MyItemizedOverlay(Drawable defaultMarker, Context context) {
			super(defaultMarker);
			this.context = context;
		}



		/**
		 * Handles a tap event on the given item.
		 * le mafeux ontap
		 */
		@Override
		protected boolean onTap(int index) {
			OverlayItem item = createItem(index);
			if (item != null) {
				if(item.getPoint() == arbo){

					Intent v = new Intent(context, DescriptionArbo.class);
					startActivity(v);

				}

			}
			return true;
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);



		//chargement du fichier map
		File map = new File(Environment.getExternalStorageDirectory().toString() + "/Arboretum/Map/grenoble.map");
		//ajout de la map proprement
		mapView = new MapView(this);
		mapView.setMapFile(map);

		//on rend la map cliquable
		mapView.setClickable(true);
		//on rend la map zoomable
		mapView.setBuiltInZoomControls(true);

		//affichage de la map
		setContentView(mapView);	

		//Creation d'un marker, un objet Drawable
		Drawable defaultMarker = getResources().getDrawable(R.drawable.letter_a); 
		Drawable location = getResources().getDrawable(R.drawable.location_oriented); 

		//ici on les stock. La deuxieme liste n'est pas MyItemizdOverlay pour
		//la simple raison qu'elle va stocker notre curseur de position
		//dont on ne souhaite pas qu'il y ai de reaction si on tap dessus
		ArrayItemizedOverlay itemizedOverlay = new MyItemizedOverlay(defaultMarker, this);
		ArrayItemizedOverlay itemizedOverlay2 = new ArrayItemizedOverlay(location);

		// create a GeoPoint with the latitude and longitude coordinates
		GeoPoint myPos = new GeoPoint(0,0);
		arbo = new GeoPoint(45.193626,5.7783196);
		//test = new GeoPoint(45.1938761,5.7682984);

		// create an OverlayItem with title and description
		item = new OverlayItem(arbo, "Arboretum", "Localisation de l'arboretum.");
		//item3 = new OverlayItem(test, "Test", "Localisation de l'arboretum.");
		item2 = new OverlayItem(myPos, "Moi", "Ma position.");
		item.setMarker(ItemizedOverlay.boundCenter(defaultMarker));
		item2.setMarker(ItemizedOverlay.boundCenter(location));

		// add the OverlayItem to the ArrayItemizedOverlay
		itemizedOverlay.addItem(item);
		itemizedOverlay2.addItem(item2);
		//itemizedOverlay.addItem(item3);

		//Le GPS
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		if (sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
			//success! there's a magnetometer
		}else{
			Toast.makeText(this,"Noob accélérometer", Toast.LENGTH_LONG).show();
		}

		// add the ArrayItemizedOverlay to the MapView
		mapView.getOverlays().add(itemizedOverlay);
		mapView.getOverlays().add(itemizedOverlay2);

	}

	@Override
	protected void onResume() {
		super.onResume();
		//mise à jour du GPS
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this); 
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this); 
	}

	@Override
	protected void onPause() {
		super.onPause();
		//pause du GPS
		locationManager.removeUpdates(this); 
	}

	@Override
	protected void onDestroy() {
		super.onPause();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		GeoPoint my_pos=new GeoPoint(arg0.getLatitude(),arg0.getLongitude());
		item2.setPoint(my_pos);

		//pour raffraichir en touchant l'écran
		//mapView.refreshDrawableState();

		//pour recentrer à chaque changement de position
		mapView.setCenter(my_pos);


	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}

