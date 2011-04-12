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
		stopsArray = R.array.r_stops;
	}
}
