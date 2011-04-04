package net.jessechen.berkeleycampusshuttle.myfavorites;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class FileHandler extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

	}

	public static void writeToFile(Context context, String data) {
		FileOutputStream fOut = null;
		BufferedWriter fbw = null;

		try {
			fOut = context.openFileOutput("favorites.dat", MODE_APPEND);
			fbw = new BufferedWriter(new OutputStreamWriter(fOut));

			fbw.write(data + "\r");
			fbw.flush();
			Toast.makeText(context, data + " added to your favorites",
					Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException f) {
			f.printStackTrace();
			Toast.makeText(context, "file not found",
					Toast.LENGTH_SHORT);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Error: did not save", Toast.LENGTH_SHORT)
					.show();
		} finally {
			try {
				fbw.close();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected static String readFile(Context context) {
		FileInputStream fIn = null;
		InputStreamReader isr = null;
		char[] inputBuffer = new char[255];
		String data = null;
		try {
			fIn = context.openFileInput("favorites.dat");
			isr = new InputStreamReader(fIn);
			isr.read(inputBuffer);
			data = new String(inputBuffer);
			Toast.makeText(context, "Favorites read", Toast.LENGTH_SHORT)
					.show();
		} catch (FileNotFoundException f) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Something went wrong..", Toast.LENGTH_SHORT)
			.show();
		} finally {
			try {
				isr.close();
				fIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return data;
	}
}