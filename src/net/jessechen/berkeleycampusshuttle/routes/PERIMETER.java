package net.jessechen.berkeleycampusshuttle.routes;

import net.jessechen.berkeleycampusshuttle.R;
import android.content.Context;

/**
 * PERIMETER is the class that contains all relevant data for the P route.
 * Contains the name of the route, XML ID of the schedule, and an array of the
 * names of the stops they go to. 
 * 
 * @author Jesse Chen
 *
 */
public class PERIMETER extends GenericRoute {
	
	public PERIMETER() {
		name = "Perimeter";
		xml = R.raw.ptimes;
	}

	public static String[] getStops(Context c) {
		stops = c.getResources().getStringArray(R.array.p_stops);
		return stops;
	}
}
