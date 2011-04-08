package net.jessechen.berkeleycampusshuttle.routes;

import android.content.Context;
import net.jessechen.berkeleycampusshuttle.R;

public class CENTRAL {
	private final static String name = "Central Campus Southside";
	private final static int xml = R.raw.ctimes;
	private static String[] stops;
	
	public static String getName() {
		return CENTRAL.name;
	}
	
	public static int getXML() {
		return CENTRAL.xml;
	}
	
	public static String[] getStops(Context c) {
		stops = c.getResources().getStringArray(R.array.c_stops);
		return stops;
	}
}
