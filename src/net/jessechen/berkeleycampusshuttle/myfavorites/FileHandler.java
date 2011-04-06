package net.jessechen.berkeleycampusshuttle.myfavorites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
		BufferedWriter writer = null;

		try {
			fOut = context.openFileOutput("favorites.dat", MODE_APPEND);
			writer = new BufferedWriter(new OutputStreamWriter(fOut));
			
			data.trim();
			writer.write(data);
			writer.newLine();
			writer.flush();
			data.replace(",", ": ");
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
				writer.close();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected static boolean deleteLine(Context context, String toDelete) throws IOException {

		File inputFile = new File("/data/data/net.jessechen.berkeleycampusshuttle/files/favorites.dat");
		File tempFile = new File("/data/data/net.jessechen.berkeleycampusshuttle/files/favorites.tmp");
		
		FileOutputStream fOut = context.openFileOutput("favorites.tmp", MODE_APPEND);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput("favorites.dat")));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fOut));

		String currentLine;
		
		while ((currentLine = reader.readLine()) != null) {
			String trimmedLine = currentLine.trim();
			if (trimmedLine.equals(toDelete)) continue;
			writer.write(currentLine);
			writer.newLine();
			writer.flush();
		}
		
		writer.close();
		reader.close();
		
		return tempFile.renameTo(inputFile);
	}
	
	protected static String readFile(Context context) throws FileNotFoundException {
		FileInputStream fIn = null;
		InputStreamReader isr = null;
		char[] inputBuffer = new char[255];
		String data = null;
		try {
			fIn = context.openFileInput("favorites.dat");
			isr = new InputStreamReader(fIn);
			isr.read(inputBuffer);
			data = new String(inputBuffer);
		} catch (FileNotFoundException f) {
			throw f;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Something went wrong..", Toast.LENGTH_SHORT)
			.show();
		} finally {
			try {
				if (isr != null) isr.close();
				if (fIn != null) fIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return data;
	}
}