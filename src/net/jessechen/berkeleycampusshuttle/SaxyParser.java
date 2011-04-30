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
package net.jessechen.berkeleycampusshuttle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxyParser extends DefaultHandler {
	private boolean correct_stop = false;
	private boolean correct_hour = false;
	private boolean done = false;
	private boolean same_hour = false;
	private int hour, numResults = 0;
	private StringBuilder buf = null;
	public static final int TOTAL_MINS = 3;
	private static int result[][] = { { -1, -1 }, { -1, -1 }, { -1, -1 } };
	// return 3 predictions, each with hour and minute

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("minute") || localName.equals("name")) {
			buf = new StringBuilder();
		} else if (localName.equals("hour")) {
			// get the hour value
			hour = Short.parseShort(atts.getValue("value"));
			if (correct_stop && !correct_hour) {
				int curHour = Stop.getCurHour();
				if (hour == curHour) {
					correct_hour = true;
					same_hour = true;
				} else if (hour > curHour) {
					correct_hour = true;
					same_hour = false;
				} // do nothing for hour < curHour
			}
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		if (buf != null) {
			for (int i = start; i < start + length; i++) {
				buf.append(ch[i]);
			}
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("minute")) {
			if (correct_hour) {
				int curMinute = Stop.getCurMinute();
				int minute = Short.parseShort(buf.toString());
				if (!same_hour && numResults != TOTAL_MINS) {
					result[numResults][0] = hour;
					// get the first minute value if not in the same hour
					result[numResults][1] = minute;
					numResults++;
				} else if (same_hour && (minute > curMinute)
						&& numResults != TOTAL_MINS) {
					result[numResults][0] = hour;
					// get the first minute that is greater than current minute.
					result[numResults][1] = minute;
					numResults++;
				}
				if (numResults == TOTAL_MINS) {
					done = true;
				}
			}
		}
		if (localName.equals("hour")) {
			if (!done) {
				// did not find a valid minute value in this hour
				// then reset boolean values
				same_hour = false;
				correct_hour = false;
			}

		} else if (localName.equals("name")) {
			CharSequence s = Stop.getBusStop();
			if (buf.toString().equals(s)) {
				correct_stop = true;
			}
		} else if (localName.equals("stop") && correct_stop) {
			correct_stop = false;
		}
		buf = null;
	}

	public static int[][] getResult() {
		return result;
	}
}
