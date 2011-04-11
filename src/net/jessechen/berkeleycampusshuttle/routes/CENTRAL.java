package net.jessechen.berkeleycampusshuttle.routes;

import android.content.Context;
import net.jessechen.berkeleycampusshuttle.R;

/**
 * CENTRAL is the class that contains all relevant data for the C route.
 * Contains the name of the route, XML ID of the schedule, and an array of the
 * names of the stops they go to.  
 * 
 * @author Jesse Chen
 * 
 */
public class CENTRAL extends GenericRoute {
	
	public CENTRAL() {
		name = "Central Campus Southside";
		xml = R.raw.ctimes;
	}
	
	public static String[] getStops(Context c) {
		stops = c.getResources().getStringArray(R.array.c_stops);
		return stops;
	}
}