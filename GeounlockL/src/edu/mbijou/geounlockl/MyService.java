package edu.mbijou.geounlockl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import edu.mbijou.geounlockl.Geolocations.geolocs;
import receiver.lockScreenReeiver;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service{
	
	BroadcastReceiver mReceiver;
	Geocoder gcoder = null;
	Context mc = this;
	Long startTime = (long) 0;
	LinkedList <geolocs> geologs = new LinkedList<geolocs>();
	ListIterator<geolocs> glogs;
	DBAdapter myloclog;
	// Intent myIntent;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


@Override
public void onCreate() {
	 KeyguardManager.KeyguardLock k1;

	 KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
     k1= km.newKeyguardLock("IN");
     k1.disableKeyguard();

     IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
     filter.addAction(Intent.ACTION_SCREEN_OFF);

     mReceiver = new lockScreenReeiver();
     registerReceiver(mReceiver, filter);

     handleCommand();
     
    super.onCreate();


}

@Override
public void onDestroy() {
	closeDB();
	unregisterReceiver(mReceiver);
	super.onDestroy();
}

public void handleCommand()
{
	gcoder = new Geocoder(mc);
	Log.d("Debug", "Presence:" + Geocoder.isPresent());
	
	/* Use the LocationManager class to obtain GPS locations */
    LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    LocationListener mlocListener = new MyLocationListener();
    mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1, 360000, mlocListener);
    Log.d("Debug", "Location services initialized.");
    
    openDB();
}

private void openDB() {
	
	myloclog = new DBAdapter(this);
	myloclog.open();
}

private void closeDB() {
	
	myloclog.close();
}

public class MyLocationListener implements LocationListener
{
	@Override
	public void onLocationChanged(Location location)
	{
		Log.d("Debug", "Location changed.");
		Geolocations x = new Geolocations();
		geolocs g = x.new geolocs(System.currentTimeMillis(),location.getLatitude(),location.getLongitude());
		geologs.add(g);
		AddRecord(g.getLoctime(), g.getLat(), g.getLng());		
		Toast.makeText(getApplicationContext(), "hue (success)!",
				   Toast.LENGTH_SHORT).show();
		if(checkNetwork())
			getAddress();
	}
	
	@Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Disable",
                Toast.LENGTH_SHORT).show();
	}
	
	public void AddRecord(long t,double lat, double lng)
	{			
		Geolocations x = new Geolocations();
		long newId = myloclog.insertRowG(t,lat,lng);
				
		// Query for the record we just added.
		// Use the ID:
		//Cursor cursor = myloclog.getRow(newId);
		//displayRecordSet(cursor);			
	}

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(), "Enable",
                Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}	
	private boolean checkNetwork()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(ni.isConnected() && ni.isAvailable())
			return true;
		else
			return false;
	}
	private void getAddress()
	{
		long loctime;
		String number;
		String street;
		String city;
		String zip;
		String state;
		String country;
		openDB();
		Cursor loc;
		Cursor add;
		loc = myloclog.getAllRowsG();
		loc.moveToLast();
		add = myloclog.getAllRowsA();
		add.moveToLast();
		while(loc.getPosition() != add.getPosition())
		{
			loc.moveToPosition(add.getPosition()+1);
			try 
			{
				List<Address> addresses = gcoder.getFromLocation(loc.getDouble(2), loc.getDouble(3), 1);
				if (addresses != null) 
				{
					for (Address namedLoc : addresses) 
					{
						loctime = System.currentTimeMillis();

						if(namedLoc.getSubThoroughfare() != null)
							number = namedLoc.getSubThoroughfare();
						else
							number = "";
						if(namedLoc.getThoroughfare() != null)
							street = namedLoc.getThoroughfare();
						else
							street = "";
						if(namedLoc.getLocality() != null)
							city = namedLoc.getLocality();
						else
							city = "";
						if(namedLoc.getAdminArea() != null)
							state = namedLoc.getAdminArea();
						else
							state = "";
						if(namedLoc.getPostalCode() != null)
							zip = namedLoc.getPostalCode();
						else
							zip = "";
						if(namedLoc.getCountryName() != null)
							country = namedLoc.getCountryName();
						else
							country = "";
						myloclog.insertRowA(loctime,number,street,city,state,zip,country);
					}
				}
			}
			catch (IOException e) 
			{
				Log.e("GPS", "Failed to get address", e);
			}
			add.close();
			add = myloclog.getAllRowsA();
			loc.moveToLast();
			add.moveToLast();
		}
		loc.close();
		add.close();
		closeDB();
		Toast.makeText(getApplicationContext(), "New address added", Toast.LENGTH_SHORT).show();
	}
}	

}
