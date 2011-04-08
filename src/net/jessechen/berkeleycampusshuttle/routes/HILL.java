package net.jessechen.berkeleycampusshuttle.routes;

import android.content.Context;
import net.jessechen.berkeleycampusshuttle.R;

public class HILL {
	private final static String name = "Hill";
	private final static int xml = R.raw.htimes;
	private static String[] stops;
	
	public static String getName() {
		return HILL.name;
	}
	
	public static int getXML() {
		return HILL.xml;
	}
	
	public static String[] getStops(Context c) {
		stops = c.getResources().getStringArray(R.array.h_stops);
		return stops;
	}
}
