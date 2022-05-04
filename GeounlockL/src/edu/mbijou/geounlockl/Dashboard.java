package edu.mbijou.geounlockl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Dashboard extends ListActivity
{
	int i = 0;
	DBAdapter myloclog;
	static LatLng target;
	static int chosenid;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		openDB();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dbdmain);
		displayRecordSet();
	}
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		Cursor c = myloclog.getRowG(position);
		target = new LatLng(c.getDouble(2),c.getDouble(3));
		chosenid = c.getInt(0);
		try 
		{
			Class mapClass = Class.forName("edu.mbijou.geounlockl.Map");
			Intent menuIntent = new Intent(this, mapClass);
			startActivity(menuIntent);
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	public static LatLng getTarget()
	{
		return target;
	}
	public static int getID()
	{
		return chosenid;
	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();	
		closeDB();
	}
	private void openDB() {
		
		myloclog = new DBAdapter(this);
		myloclog.open();
	}
	private void closeDB() {
		
		myloclog.close();
	}
	private void displayRecordSet() {
		Cursor cursor = myloclog.getAllRowsG();
		ArrayAdapter<String> a;
		String s = " ";
		Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss", Locale.US);
		LinkedList <String> geo = new LinkedList<String>();
		if (cursor.moveToFirst()) {
			do {
					s += cursor.getInt(0) + ") " + String.format("%.2f", cursor.getDouble(2))
						   +", " + String.format("%.2f", cursor.getDouble(3)) + " at " + format.format(new Date(cursor.getLong(1)));
				geo.add(s);
				s = "";
			} while(cursor.moveToNext());
		}
		cursor.close();
		
		a = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,geo);
		setListAdapter(a);
	}
}
