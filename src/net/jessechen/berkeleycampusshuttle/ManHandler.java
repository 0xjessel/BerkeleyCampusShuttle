package net.jessechen.berkeleycampusshuttle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ManHandler extends DefaultHandler {
	private boolean correct_stop = false;
	private boolean correct_hour = false;
	private boolean correct_minute = false;
	private boolean same_hour = false;

	private int hour = -1;
	private StringBuilder buf = null;
	private static int[] result;
	
	public static int[] getResult() {
		return result;
	}
	
	@Override
	public void startDocument() throws SAXException {
		// Some sort of setting up work
		result = new int[2];
	}

	@Override
	public void endDocument() throws SAXException {
		// Some sort of finishing up work
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("minute")) {
			buf = new StringBuilder();
		} else if (localName.equals("name")) {
			buf = new StringBuilder();
		} else if (localName.equals("hour")) {
			hour = Integer.parseInt(atts.getValue("value"));
			if (correct_stop && !correct_hour) {
				int curHour = Stop.getCurHour();
				if (hour == curHour) {
					result[0] = hour;
					correct_hour = true;
					same_hour = true;
				} else if (hour > curHour) {
					result[0] = hour;
					correct_hour = true;
					same_hour = false;
				}
			}
			hour = -1;
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
			if (correct_hour && !correct_minute) {
				int curMinute = Stop.getCurMinute();
				int minute = Integer.parseInt(buf.toString());
				if (!same_hour) {
					result[1] = minute; // get the first minute field
					correct_minute = true;
				} else if (minute == curMinute || minute > curMinute) {
					result[1] = minute;
					correct_minute = true;
				} 
			}
		} if (localName.equals("hour")) {
			if (!correct_minute) {
				same_hour = false;
				correct_hour = false;
			}
		
		} else if (localName.equals("name")) {
			if (buf.toString().equals(Stop.getBusStop())) {
				correct_stop = true;
			}
		} else if (localName.equals("stop") && correct_stop) {
			this.correct_stop = false;
		}
		buf = null;
	}
}