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

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.jessechen.berkeleycampusshuttle.myfavorites.FileHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Stop is the activity that shows the user the next three predictions for the
 * specified route and stop. This is the final destination for the user. From
 * here, the user can add the specified stop to their favorites for quicker
 * access.
 * 
 * @author Jesse Chen
 * 
 */
public class Stop extends Activity {
	private Bundle b;
	private static CharSequence busStop, routeName;
	private static Calendar cal;
	private TextView title;
	private static TextView countdown1;
	private static TextView countdown2;
	private static TextView countdown3;
	private static int[][] result;
	private static int hourRemaining, minuteRemaining, curHour, curMinute,
			dayOfWeek, busXml;
	private static MyCount counter1;
	private static MyCount counter2;
	private static MyCount counter3;
	private Button favButton;
	private boolean inFavorites;
	private String toFavorite;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.stop);

		b = getIntent().getExtras();
		busXml = b.getInt("xml");
		busStop = b.getCharSequence("stop");
		routeName = b.getCharSequence("route");

		title = (TextView) findViewById(R.id.t_stop);
		title.setText(routeName + ": Predictions for " + busStop);

		countdown1 = (TextView) findViewById(R.id.countdown1);
		countdown2 = (TextView) findViewById(R.id.countdown2);
		countdown3 = (TextView) findViewById(R.id.countdown3);

		cal = Calendar.getInstance();
		calculate(countdown1, cal, this, 0, counter1, busXml);
		calculate(countdown2, cal, this, 1, counter2, busXml);
		calculate(countdown3, cal, this, 2, counter3, busXml);

		toFavorite = routeName + FileHandler.TOKEN + busStop;

		favButton = (Button) findViewById(R.id.b_addtofav);

		if (inFavorites = FileHandler.inFavorites(getApplicationContext(),
				toFavorite)) {
			favButton.setText("Remove from Favorites");
		} else {
			favButton.setText("Add to Favorites");
		}

		favButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (inFavorites) {
					setAlertDialog(); // prompt to remove from fav
				} else { // add to fav
					if (FileHandler.writeToFile(getApplicationContext(),
							toFavorite) == true) {
						favButton.setText("Remove from Favorites");
						inFavorites = true;
					}
				}
			}
		});
	}

	private void setAlertDialog() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					FileHandler.deleteLine(getApplicationContext(), toFavorite);
					favButton.setText("Add to Favorites");
					inFavorites = false;
				case DialogInterface.BUTTON_NEGATIVE:
					dialog.cancel();
				}
			}
		};

		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setMessage(
				"Are you sure you want remove this stop from your Favorites?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	/**
	 * This method takes care of the three CountDownTimers by pulling data from
	 * the XML file and initializing them. Also takes care of cases when there
	 * are no more predictions for the day , and when user is viewing this on a
	 * weekend (campus shuttle does not run on weekends).
	 * 
	 */
	public static void calculate(TextView t, Calendar c, Context cx, int id,
			CountDownTimer counter, int xml) {
		t.invalidate(); // do i need this

		curHour = c.get(Calendar.HOUR_OF_DAY);
		curMinute = c.get(Calendar.MINUTE);
		dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeek != Calendar.SUNDAY && dayOfWeek != Calendar.SATURDAY) {
			try {
				result = getEventsFromAnXML(cx, xml, busStop);
			} catch (XmlPullParserException e) {
				Toast.makeText(cx, "A fatal error occured.  Report it!",
						Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Toast.makeText(cx, "A fatal error occured.  Report it!",
						Toast.LENGTH_SHORT).show();
			}

			// if first hour result is -1 then no more predictions for the day
			if (result[0][0] != -1) {
				if (id == 0) {
					if (result[0][0] != -1) {
						hourRemaining = (result[0][0] - curHour);
						minuteRemaining = (result[0][1] - curMinute);
						counter = new Stop.MyCount(t, cx, 0, hourRemaining * 3600000
								+ minuteRemaining * 60000, 1000);
						counter.start();
					} else {
						t.setText("");
					}
				} else if (id == 1) {
					if (result[1][0] != -1) {
						hourRemaining = (result[1][0] - curHour);
						minuteRemaining = (result[1][1] - curMinute);
						counter = new MyCount(t, cx, 1, hourRemaining * 3600000
								+ minuteRemaining * 60000, 1000);
						counter.start();
					} else {
						t.setText("");
					}
				} else if (id == 2) {
					if (result[2][0] != -1) {
						hourRemaining = (result[2][0] - curHour);
						minuteRemaining = (result[2][1] - curMinute);
						counter = new MyCount(t, cx, 2, hourRemaining * 3600000
								+ minuteRemaining * 60000, 1000);
						counter.start();
					} else {
						t.setText("");
					}
				}
			} else {
				t.setText("No more predictions for the day");
			}
		} else {
			t.setText("Campus shuttle does not run during the weekends");
		}
	}

	/**
	 * Custom CountDownTimer that displays how many minutes remaining along with
	 * the estimated arrival time. Switches the text to "Arriving" when it is
	 * under a minute from prediction.
	 * 
	 * When the CountDownTimer finishes, it cancels all 3 CountDownTimers and
	 * calls calculate() again to recalculate the next three predictions.
	 * 
	 * @author Jesse Chen
	 * 
	 */
	public static class MyCount extends CountDownTimer {
		private TextView tv;
		private Context c;
		private String subString, minute, hour, ampm;

		public MyCount(TextView cd, Context cx, int index, long millisInFuture,
				long countDownInterval) {
			super(millisInFuture, countDownInterval);
			tv = cd;
			c = cx;
			minute = Integer.toString(result[index][1]);
			if (minute.length() == 1) { // append extra 0 for formatting
				if (minute == "0") {
					minute = "00";
				} else {
					minute = "0" + minute;
				}
			}
			hour = Integer.toString(result[index][0] % 12); // add am or pm
			if (hour == "0") {
				hour = "12";
			}
			if (result[index][0] > 12) {
				ampm = "pm";
			} else {
				ampm = "am";
			}
			subString = " (at " + hour + ":" + minute + ampm + ")";
		}

		@Override
		public void onFinish() {
			countdown1.setText("");
			if (counter1 != null) {
				counter1.cancel();
			}
			countdown2.setText("");
			if (counter2 != null) {
				counter2.cancel();
			}
			countdown3.setText("");
			if (counter3 != null) {
				counter3.cancel();
			}
			cal = Calendar.getInstance();
			calculate(countdown1, cal, c, 0, counter1, busXml);
			calculate(countdown2, cal, c, 1, counter2, busXml);
			calculate(countdown3, cal, c, 2, counter3, busXml);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			int timeRemaining = (int) millisUntilFinished / 60000;
			if (timeRemaining == 0) {
				tv.setText("Arriving" + subString);
			} else {
				tv.setText(timeRemaining + " minutes remaining" + subString);
			}
		}
	}

	/**
	 * Handles the XML parsing and returns the next three predictions for a
	 * specified stop.
	 * 
	 * I used a SAXParser for my app because I read that SAXParsers are more
	 * efficient than DOMParsers since SAX only has to run through the XML file
	 * once. I aim to keep it fast (even if it is negligible) by design choice.
	 * 
	 * @param xml
	 * @param stop
	 *            name of the specified stop
	 * @return int[3][2] array that contains three predictions, with each
	 *         prediction containing the hour value, then the minute value in
	 *         that order.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static int[][] getEventsFromAnXML(Context cx, int xml,
			CharSequence stop) throws XmlPullParserException, IOException {
		InputStream istream = null;
		int[][] result = null;
		try {
			// taking out activity parameter and using 'this'.  can be disasterous
			istream = cx.getResources().openRawResource(xml);
			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			/* Create a new ContentHandler and apply it to the XML-Reader */
			SaxyParser myExampleHandler = new SaxyParser();
			xr.setContentHandler(myExampleHandler);
			/* Parse the xml-data from our URL. */
			xr.parse(new InputSource(istream));
			/* Parsing has finished. */
			result = SaxyParser.getResult();
		} catch (Exception FileNotFoundException) {
			FileNotFoundException.printStackTrace();
		}
		return result;
	}

	/*
	 * We want to make sure that when the activity calls onStop() it also stops
	 * the CountDownTimers running in the background.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		if (counter1 != null) {
			counter1.cancel();
		}
		if (counter2 != null) {
			counter2.cancel();
		}
		if (counter3 != null) {
			counter3.cancel();
		}
	}

	public static CharSequence getBusStop() {
		return busStop;
	}

	public static CharSequence getRouteName() {
		return routeName;
	}

	public static int getCurHour() {
		return curHour;
	}

	public static int getCurMinute() {
		return curMinute;
	}
}
