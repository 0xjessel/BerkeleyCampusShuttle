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
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class FileHandler extends Activity {

	private static ArrayList<String> myFavorites = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
	}

	// This is used prior to writing to the file to check if
	// the specified stop is already in the file.
	public static String[] readFileWrapper(Context context) {
		String tempFavorites = null;
		String[] favorites = null;
		String[] trimFavorites = null;
		int toCopy;
		try {
			tempFavorites = readFile(context);
			favorites = tempFavorites.split("\n");
			// trim junk off
			toCopy = (favorites.length > 1) ? favorites.length - 1 : 0;
			trimFavorites = new String[toCopy];
			System.arraycopy(favorites, 0, trimFavorites, 0, toCopy);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return trimFavorites;
	}

	public static void writeToFile(Context context, String data) {
		FileOutputStream fOut = null;
		BufferedWriter writer = null;

		try {
			fOut = context.openFileOutput("favorites.dat", MODE_APPEND);
			writer = new BufferedWriter(new OutputStreamWriter(fOut));

			data.trim();

			String[] favorites = readFileWrapper(context);

			for (String favorite : favorites) {
				myFavorites.add(favorite);
			}
			
			if (!myFavorites.contains(data)) {
				myFavorites.add(data);
				writer.write(data);
				writer.newLine();
				writer.flush();
				String toToast = data.replace(",", ": ");
				Toast.makeText(context,
						toToast + " has been added to your Favorites!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "Already in your Favorites!",
						Toast.LENGTH_SHORT).show();
			}
		} catch (FileNotFoundException f) {
			f.printStackTrace();
			Toast.makeText(context, "File not found", Toast.LENGTH_SHORT);
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

	// deleteLine copies the file to a temp file except for the specified
	// string, and at the end, it renames the temp file to the original.
	protected static boolean deleteLine(Context context, String toDelete)
			throws IOException {

		File inputFile = new File(
				"/data/data/net.jessechen.berkeleycampusshuttle/files/favorites.dat");
		File tempFile = new File(
				"/data/data/net.jessechen.berkeleycampusshuttle/files/favorites.tmp");

		FileOutputStream fOut = context.openFileOutput("favorites.tmp",
				MODE_APPEND);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				context.openFileInput("favorites.dat")));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fOut));

		String currentLine;

		while ((currentLine = reader.readLine()) != null) {
			String trimmedLine = currentLine.trim();
			if (trimmedLine.equals(toDelete)) {
				myFavorites.remove(currentLine);
				continue;
			}
			writer.write(currentLine);
			writer.newLine();
			writer.flush();
		}

		writer.close();
		reader.close();

		return tempFile.renameTo(inputFile);
	}

	protected static String readFile(Context context)
			throws FileNotFoundException {
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
			Toast.makeText(context, "Something went wrong..",
					Toast.LENGTH_SHORT).show();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (fIn != null)
					fIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return data;
	}
}