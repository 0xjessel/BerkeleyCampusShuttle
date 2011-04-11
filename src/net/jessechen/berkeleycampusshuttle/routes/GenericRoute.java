package net.jessechen.berkeleycampusshuttle.routes;

import java.util.LinkedList;

import android.content.Context;

public class GenericRoute {

	protected static String name;
	protected static int xml;
	protected static String[] stops;

	public static String getName() {
		return name;
	}

	public static int getXML() {
		return xml;
	}

	public static String[] getStops() {
		return stops;
	}
	
	/**
	 * Pulls every route's stops array and concatenates them, takes care of
	 * duplicates.
	 * 
	 * @return a String[] that contains all the stops that Berkeley Campus
	 *         Shuttle supports
	 */
	public static String[] getAllStops(Context c) {
		LinkedList<String> allStops = new LinkedList<String>();
		addToAllStops(allStops, PERIMETER.getStops(c));
		addToAllStops(allStops, REVERSE.getStops(c));
		addToAllStops(allStops, HILL.getStops(c));
		addToAllStops(allStops, CENTRAL.getStops(c));
		
		String[] toReturn = allStops.toArray(new String[allStops.size()]);
		java.util.Arrays.sort(toReturn);
		return toReturn;
	}

	private static void addToAllStops(LinkedList<String> allStops, String[] stops) {
		for (int i = 0; i < stops.length; i++) {
			if (!allStops.contains(stops[i])) {
				allStops.add(stops[i]);
			}
		}
	}
}
