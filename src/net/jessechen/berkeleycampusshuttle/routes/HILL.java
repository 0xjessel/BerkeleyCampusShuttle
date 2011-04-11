package net.jessechen.berkeleycampusshuttle.routes;

import android.content.Context;
import net.jessechen.berkeleycampusshuttle.R;

/**
 * HILL is the class that contains all relevant data for the H route.
 * Contains the name of the route, XML ID of the schedule, and an array of the
 * names of the stops they go to. 
 * 
 * @author Jesse Chen
 *
 */
public class HILL extends GenericRoute {
	
	public HILL() {
		name = "Hill";
		xml = R.raw.htimes;
	}
	
	public static String[] getStops(Context c) {
		stops = c.getResources().getStringArray(R.array.h_stops);
		return stops;
	}
}
