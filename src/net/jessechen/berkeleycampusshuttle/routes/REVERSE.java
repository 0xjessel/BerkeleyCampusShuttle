package net.jessechen.berkeleycampusshuttle.routes;

import android.content.Context;
import net.jessechen.berkeleycampusshuttle.R;

/**
 * REVERSE is the class that contains all relevant data for the R route.
 * Contains the name of the route, XML ID of the schedule, and an array of the
 * names of the stops they go to. 
 * 
 * @author Jesse Chen
 *
 */
public class REVERSE extends GenericRoute {
	
	public REVERSE() {
		name = "Reverse";
		xml = R.raw.rtimes;
	}

	public static String[] getStops(Context c) {
		stops = c.getResources().getStringArray(R.array.r_stops);
		return stops;
	}
}
