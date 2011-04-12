/*******************************************************************************
 * Authors:
 *     Jesse Chen <contact@jessechen.net>
 * 
 * Copyright (c) 2011 Jesse Chen.
 * 
 * Berkeley Campus Shuttle is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Berkeley Campus Shuttle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Berkeley Campus Shuttle.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/
package net.jessechen.berkeleycampusshuttle.routes;

import java.util.LinkedList;

import android.content.Context;

public class GenericRoute {

	protected static String name;
	protected static int xml, stopsArray;
	protected static String[] stops;

	public static String getName() {
		return name;
	}

	public static int getXML() {
		return xml;
	}

	public static String[] getStops(Context c) {
		stops = c.getResources().getStringArray(stopsArray);
		java.util.Arrays.sort(stops);
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
