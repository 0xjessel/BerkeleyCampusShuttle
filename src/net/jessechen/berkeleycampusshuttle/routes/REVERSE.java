package net.jessechen.berkeleycampusshuttle.routes;

import android.content.Context;
import net.jessechen.berkeleycampusshuttle.R;

public class REVERSE {
	private final static String name = "Reverse";
	private final static int xml = R.raw.rtimes;
	private static String[] stops;
	
	public static String getName() {
		return REVERSE.name;
	}
	
	public static int getXML() {
		return REVERSE.xml;
	}
	
	public static String[] getStops(Context c) {
		stops = c.getResources().getStringArray(R.array.r_stops);
		return stops;
	}
}
