package com.project.vactionbook;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

	GoogleMap googleMap;
	int idToBeShown;
	int reqCode=0;
	int isMarkable=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Bundle bundle=getIntent().getExtras();
		idToBeShown=bundle.getInt("id");
		isMarkable=bundle.getInt("allowMarking");
		idToBeShown++;

	//	getActionBar().setTitle("Add Location");
		checkPermissions(findViewById(R.id.fab));

        

    }
	
	
	private void drawMarker(LatLng point){
    	// Creating an instance of MarkerOptions
    	MarkerOptions markerOptions = new MarkerOptions();
    		
    	// Setting latitude and longitude for the marker
    	markerOptions.position(point);
    		
    	// Adding marker on the Google Map
    	googleMap.addMarker(markerOptions);    		
    }
	
	
	private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void>{
		@Override
		protected Void doInBackground(ContentValues... contentValues) {
			
			/** Setting up values to insert the clicked location into SQLite database */           
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);			
			return null;
		}		
	}
	
	private class LocationDeleteTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			
			/** Deleting all the locations stored com SQLite database */
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);			
			return null;
		}		
	}	


	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/


	@Override
	public Loader<Cursor> onCreateLoader(int arg0,
										 Bundle arg1) {
		
		// Uri to the content provider LocationsContentProvider
		Uri uri = LocationsContentProvider.CONTENT_URI;
		
		// Fetches all the rows from locations table
        return new CursorLoader(this, uri, null, null, null, null);

	}


	@Override
	public void onLoadFinished(Loader<Cursor> arg0,
			Cursor arg1) {
		int locationCount = 0;
		double lat=0;
		double lng=0;
		double latPoint=0;
		double lngPoint=0;
		float zoom=0,zoomPOint=0;
		int id;
		// Number of locations available com the SQLite database table
		locationCount = arg1.getCount();
		
		// Move the current record pointer to the first row of the table
		arg1.moveToFirst();
		
		for(int i=0;i<locationCount;i++){
			id = arg1.getInt(arg1.getColumnIndex(LocationsDB.FIELD_ROW_ID));

			// Get the latitude
			lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));
			
			// Get the longitude
			lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));
			
			// Get the zoom level
			zoom = arg1.getFloat(arg1.getColumnIndex(LocationsDB.FIELD_ZOOM));
			
			// Creating an instance of LatLng to plot the location com Google Maps
			LatLng location = new LatLng(lat, lng);
			
			// Drawing the marker com the Google Maps
			if(id==idToBeShown){
				drawMarker(location);
				latPoint=lat;
				lngPoint=lng;
				zoomPOint=zoom;
			}else{

			}
			// Traverse the pointer to the next row
			arg1.moveToNext();
		}
		
		if(locationCount>0){
			// Moving CameraPosition to last clicked position
	        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latPoint,lngPoint)));
	        
	        // Setting the zoom level com the map on last position  is clicked
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoomPOint));
            
		}		
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onBackPressed() {
		Intent intent=new Intent(MainActivity.this,VacationPic.class);
		intent.putExtra("showEnteries","true");
		startActivity(intent);

		finish();
	}

private void setAppAsDefault(){
	int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

	// Showing status
	if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available

		int requestCode = 10;
		Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
		dialog.show();

	}else { // Google Play Services are available

		// Getting reference to the SupportMapFragment of activity_main.xml
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		// Getting GoogleMap object from the fragment
		googleMap = fm.getMap();

		// Enabling MyLocation Layer of Google Map
		googleMap.setMyLocationEnabled(true);

		// Invoke LoaderCallbacks to retrieve and draw already saved locations com map
		getSupportLoaderManager().initLoader(0, null, this);
	}

	if(isMarkable==1){
	googleMap.setOnMapClickListener(new OnMapClickListener() {

		@Override
		public void onMapClick(LatLng point) {


			// Drawing marker on the map
			drawMarker(point);

			// Creating an instance of ContentValues
			ContentValues contentValues = new ContentValues();

			// Setting latitude com ContentValues
			contentValues.put(LocationsDB.FIELD_LAT, point.latitude );

			// Setting longitude com ContentValues
			contentValues.put(LocationsDB.FIELD_LNG, point.longitude);

			// Setting zoom com ContentValues
			contentValues.put(LocationsDB.FIELD_ZOOM, googleMap.getCameraPosition().zoom);

			// Creating an instance of LocationInsertTask
			LocationInsertTask insertTask = new LocationInsertTask();

			// Storing the latitude, longitude and zoom level to SQLite database
			insertTask.execute(contentValues);

			Toast.makeText(getBaseContext(), "Location saved !! Press back !!", Toast.LENGTH_SHORT).show();

			googleMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng point) {


					Toast.makeText(getBaseContext(), " You have marked the location ", Toast.LENGTH_SHORT).show();

				}
			});

		}
	});}
}
	public void checkPermissions(View view) {
		// Check if the  permission is already available.
		if (
				(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
						!= PackageManager.PERMISSION_GRANTED) ||
						(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
								!= PackageManager.PERMISSION_GRANTED)){

			requestPermission();

		} else {
			setAppAsDefault();
		}
	}
	private void requestPermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
				Manifest.permission.ACCESS_COARSE_LOCATION)) {

			Snackbar.make(findViewById(R.id.fab), "Grant Permission",
					Snackbar.LENGTH_INDEFINITE)
					.setAction("OK", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							ActivityCompat.requestPermissions(MainActivity.this,
									new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
									reqCode);
						}
					})
					.show();
		} else {

			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
					reqCode);
		}

	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
										   int[] grantResults) {

		Log.i("ReqCode",String.valueOf(requestCode));
		if (requestCode == reqCode
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			setAppAsDefault();
			//  checkPermissions(btnSent);
		}else{
			Snackbar.make(findViewById(R.id.fab), "Grant Permission",
					Snackbar.LENGTH_INDEFINITE)
					.setAction("OK", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							ActivityCompat.requestPermissions(MainActivity.this,
									new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
									reqCode);
						}
					})
					.show();
		}
	}
}