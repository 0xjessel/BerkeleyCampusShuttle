package net.jessechen.ucberkeleyshuttle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ManHandler extends DefaultHandler {
	private boolean in_name = false;
	private boolean in_hour = false;
	private boolean in_minute = false;
	private boolean correct_stop = false;
	private boolean correct_hour = false;
	private boolean same_hour = false;

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
			this.in_minute = true;
		} else if (localName.equals("name")) {
			this.in_name = true;
		} else if (localName.equals("hour")) {
			this.in_hour = true;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		String str = new String(ch);
		if (in_name) {
			if (str.equals(Stop.getBusStop())) { 
				correct_stop = true; 
			}
		} else if (correct_stop) { // we are in the correct stop
			String str2 = new String(ch, 0, 1);
			int curHour = Integer.parseInt(Stop.getCurHour());
			if (in_hour) { // looking at a hour
				try {
					if (str2.equals(Stop.getCurHour())) {
						result[0] = str2;
						correct_hour = true;
						same_hour = true;
					} else if (Integer.parseInt(str2) > curHour) {
						result[0] = str2;
						correct_hour = true;
					} else if (correct_hour) {
						if (in_minute) { 
							int curMinute = Integer.parseInt(Stop.getCurMinute());
							if (!same_hour) {
								result[1] = str2; // get the first minute field
							} else if (str2.equals(Stop.getCurMinute()) || 
									Integer.parseInt(str2) > curMinute) {
								result[1] = str2;
							} 
						}
					}	
				} catch (NumberFormatException e) {
					
				}
			}
		}
	}
	
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("minute")) {
			this.in_minute = false;
		} else if (localName.equals("hour")) {
			this.in_hour = false;
		} else if (localName.equals("hour") && correct_hour) {
			this.correct_hour = false;
		} else if (localName.equals("name")) {
			this.in_name = false;
		} else if (localName.equals("stop") && correct_stop) {
			this.correct_stop = false;
		}
		 
	}
}
