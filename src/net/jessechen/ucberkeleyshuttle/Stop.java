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
import android.os.CountDownTimer;
import android.widget.TextView;

public class Stop extends Activity {
    /** Called when the activity is first created. */
	private static Bundle b;
	private static CharSequence busStop;
	private static CharSequence routeName;
	private static Calendar calendar;
	private static String curHour, curMinute;
	private static TextView title;
	private static TextView countdown;
	private static String[] result = null;
	private static int hourRemaining, minuteRemaining;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.stop);
        //final TextView tv = new TextView(this);
        //this.setContentView(tv);
        
        b = getIntent().getExtras();
        busStop = b.getCharSequence("stop");
        routeName = b.getCharSequence("route");

        calendar = Calendar.getInstance();
        curHour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        curMinute = Integer.toString(calendar.get(Calendar.MINUTE));
            
        try {
			result = getEventsFromAnXML(this, b.getInt("xml"), busStop);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* TODO: Shuttles only run M-F, have to check what day it is */
        title = (TextView) findViewById(R.id.stop_title);
        title.setText(routeName + ": Predictions for " + busStop + ": " + hourRemaining 
        		+ " hours and " + minuteRemaining + " minutes");
        
		if (result[0] != null && result[1] != null) {
			hourRemaining = Integer.parseInt(result[0]) - Integer.parseInt(curHour);
			minuteRemaining = Integer.parseInt(result[1]) - Integer.parseInt(curMinute);
			
			countdown = (TextView) findViewById(R.id.countdown);
			MyCount counter = new MyCount(hourRemaining * 3600000 + minuteRemaining * 60000, 1000);
			counter.start();      
		} else {
			// no more for the day
		}
    }
    
    public class MyCount extends CountDownTimer {
    	public MyCount(long millisInFuture, long countDownInterval) {
    		super(millisInFuture, countDownInterval);
    	}

		@Override
		public void onFinish() {
            countdown.setText("done!");
		}

		@Override
		public void onTick(long millisUntilFinished) {
            countdown.setText("seconds remaining: " + millisUntilFinished / 1000);
		}
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