package net.jessechen.ucberkeleyshuttle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Stop extends Activity {
    /** Called when the activity is first created. */
	private static Bundle b;
	private static CharSequence busStop;
	private static CharSequence routeName;
	private static Calendar calendar;
	private static String curHour, curMinute;
	private static TextView title;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.stop);
        
        b = getIntent().getExtras();
        busStop = b.getCharSequence("stop");
        routeName = b.getCharSequence("route");

        calendar = Calendar.getInstance();
        curHour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        curMinute = Integer.toString(calendar.get(Calendar.MINUTE));
        
        String[] result = null;        
        try {
			result = getEventsFromAnXML(this, b.getInt("xml"), busStop);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        title = (TextView) findViewById(R.id.stop_title);
        title.setText(routeName + ": Predictions for " + busStop + " next one in " 
        		+ (Integer.parseInt(result[0]) - Integer.parseInt(curHour)) + ":" 
        		+ (Integer.parseInt(result[1]) - Integer.parseInt(curMinute)));
		
    }
    
    private String[] getEventsFromAnXML(Activity activity, int xml, CharSequence stop) throws XmlPullParserException, IOException {
		InputStream istream = null;
		String[] result = null;
		try {
			istream = activity.getResources().openRawResource(R.raw.ptimes);

			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			/* Create a new ContentHandler and apply it to the XML-Reader */
			ManHandler myExampleHandler = new ManHandler();
			xr.setContentHandler(myExampleHandler);
			/* Parse the xml-data from our URL. */
			xr.parse(new InputSource(istream));
			/* Parsing has finished. */
			result = ManHandler.getResult();
		} catch (Exception FileNotFoundException) {
			FileNotFoundException.printStackTrace();
		}
		result[0] = "3";
		result[1] = "4";
		return result;
    }
    
    public static CharSequence getBusStop() {
    	return busStop;
    }
    
    public static CharSequence getRouteName() {
    	return routeName;
    }
    
    public static String getCurHour() {
    	return curHour;
    }
    
    public static String getCurMinute() {
    	return curMinute;
    }
}