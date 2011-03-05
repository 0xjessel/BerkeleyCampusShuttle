package net.jessechen.berkeleycampusshuttle;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Stop extends Activity {
	private static Bundle b;
	private static CharSequence busStop, routeName;
	private static Calendar calendar;
	private static TextView title, countdown1, countdown2, countdown3;
	private static int[][] result;
	private static short hourRemaining, minuteRemaining, curHour, curMinute,
			dayOfWeek;
	private MyCount counter;
	private static Button refreshButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.stop);

		b = getIntent().getExtras();
		busStop = b.getCharSequence("stop");
		routeName = b.getCharSequence("route");

		title = (TextView) findViewById(R.id.stop_title);
		title.setText(routeName + ": Predictions for " + busStop);

		refresh();

		refreshButton = (Button) findViewById(R.id.refresh);
		refreshButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				refresh();
			}
		});
	}

	public void refresh() {
		calendar = Calendar.getInstance();
		curHour = (short) calendar.get(Calendar.HOUR_OF_DAY);
		curMinute = (short) calendar.get(Calendar.MINUTE);
		dayOfWeek = (short) calendar.get(Calendar.DAY_OF_WEEK);
		countdown1 = (TextView) findViewById(R.id.countdown1);
		countdown2 = (TextView) findViewById(R.id.countdown2);
		countdown3 = (TextView) findViewById(R.id.countdown3);

		if (dayOfWeek != Calendar.SUNDAY && dayOfWeek != Calendar.SATURDAY) {
			try {
				result = getEventsFromAnXML(this, b.getInt("xml"), busStop);
			} catch (XmlPullParserException e) {
				Toast.makeText(getApplicationContext(),
						"A fatal error occured.  Report it!",
						Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(),
						"A fatal error occured.  Report it!",
						Toast.LENGTH_SHORT).show();
			}

			if (result[0][0] != -1) { // first hour result is -1, no more
								      // predictions for the day
				setCounter(countdown1, 1);
				setCounter(countdown2, 2);
				setCounter(countdown3, 3);
			} else {
				countdown1.setText("No more predictions for the day");
			}
		} else {
			countdown1
					.setText("Campus shuttle does not run during the weekends");
		}
	}

	private void setCounter(TextView tv, int i) {
		if (result[i][0] != -1) {
			hourRemaining = (short) (result[i][0] - curHour);
			minuteRemaining = (short) (result[i][1] - curMinute);

			counter = new MyCount(tv, hourRemaining * 3600000
					+ minuteRemaining * 60000, 1000);
			counter.start();
		}
	}

	public class MyCount extends CountDownTimer {
		TextView tv;

		public MyCount(TextView t, long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			tv = t;
		}

		@Override
		public void onFinish() {
			Stop.this.refresh();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv.setText(millisUntilFinished / 60000 + " minutes remaining (at "
					+ result[0] + ":" + result[1] + ")");
		}
	}

	private int[][] getEventsFromAnXML(Activity activity, int xml,
			CharSequence stop) throws XmlPullParserException, IOException {
		InputStream istream = null;
		int[][] result = null;
		try {
			istream = activity.getResources().openRawResource(xml);

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

	public void onDestroy() {
		super.onDestroy();
		if (counter != null) { counter.cancel();}
	}

	public static CharSequence getBusStop() {
		return busStop;
	}

	public static CharSequence getRouteName() {
		return routeName;
	}

	public static short getCurHour() {
		return curHour;
	}

	public static short getCurMinute() {
		return curMinute;
	}
}