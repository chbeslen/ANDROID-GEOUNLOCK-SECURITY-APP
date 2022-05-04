package edu.mbijou.geounlockl;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Map extends FragmentActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		placeMark(Dashboard.getTarget(), Dashboard.getID());
	}
	public void placeMark(LatLng target, int id)
    {
    	final GoogleMap mMap;
    	mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.maps)).getMap();
    	mMap.addMarker(new MarkerOptions()
    	        .position(target)
    	        .title("Location #" + id)
    	        .snippet("Latitude: " + String.format("%.4f", target.latitude) + ", Longitude: " + String.format("%.4f", target.longitude)));
     	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(target, BIND_ADJUST_WITH_ACTIVITY);
    	mMap.animateCamera(update);
    }
	
}
