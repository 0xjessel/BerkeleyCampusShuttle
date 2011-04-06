package net.jessechen.berkeleycampusshuttle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParser extends DefaultHandler {
	private boolean correct_stop = false;
	private boolean correct_hour = false;
	private boolean done = false;
	private boolean same_hour = false;
	private int hour, numResults = 0;
	private StringBuilder buf = null;
	public static final int TOTAL_MINS = 3;
	private static int result[][] = {{-1, -1}, {-1, -1}, {-1, -1}}; // return 3 predictions, each with hour and minute
	
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
			hour = Short.parseShort(atts.getValue("value")); // get the hour value
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
					result[numResults][1] = minute; // get the first minute value if not in the same hour
					numResults++;
				} else if ((minute > curMinute) && numResults != TOTAL_MINS) { //minute == curMinute || 
					result[numResults][0] = hour;
					result[numResults][1] = minute;
					numResults++;
				}
				if (numResults == TOTAL_MINS) {
					done = true;
				}
			}
		} if (localName.equals("hour")) {
			if (!done) { // did not find a valid minute value in this hour
				same_hour = false; // reset boolean values
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