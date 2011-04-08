package net.jessechen.berkeleycampusshuttle.routes;

import net.jessechen.berkeleycampusshuttle.R;
import android.content.Context;

public class PERIMETER {
	private final static String name = "Perimeter";
	private final static int xml = R.raw.ptimes;
	private static String[] stops;
	
	public static String getName() {
		return PERIMETER.name;
	}
	
	public static int getXML() {
		return PERIMETER.xml;
	}
	
	public static String[] getStops(Context c) {
		stops = c.getResources().getStringArray(R.array.p_stops);
		return stops;
	}
}
