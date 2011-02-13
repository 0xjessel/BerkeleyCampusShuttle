package net.jessechen.ucberkeleyshuttle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ManHandler extends DefaultHandler {
	private boolean correct_stop = false;
	private boolean correct_hour = false;
	private boolean correct_minute = false;
	private boolean same_hour = false;

	private String hour = null;
	private StringBuilder buf = null;
	private static String[] result;
	
	public static String[] getResult() {
		return result;
	}
	
	@Override
	public void startDocument() throws SAXException {
		// Some sort of setting up work
		result = new String[2];
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
			hour = new String(atts.getValue("value"));
			if (correct_stop && !correct_hour) {
				int curHour = Integer.parseInt(Stop.getCurHour());
				if (hour.equals((Stop.getCurHour()))) {
					result[0] = hour.toString();
					correct_hour = true;
					same_hour = true;
				} else if (Integer.parseInt(hour.toString()) > curHour) {
					result[0] = hour.toString();
					correct_hour = true;
					same_hour = false;
				}
			}
			hour = null;
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
				String curMinute = Stop.getCurMinute();
				String minute = buf.toString();
				if (!same_hour) {
					result[1] = minute; // get the first minute field
					correct_minute = true;
				} else if (minute.equals(curMinute) || 
						Integer.parseInt(minute) > Integer.parseInt(curMinute)) {
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