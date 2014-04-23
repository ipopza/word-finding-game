package com.example.test;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends ActionBarActivity {

	private final static String TAG = "POP";
	private final static int dimenX = 5;
	private final static int dimenY = 5;

	private Random rand;
	private String[][] table;

	private ArrayList<Word> wordList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		rand = new Random();
		table = new String[dimenX][dimenY];

		initApp();
	}

	
	/*
	 * Initial Function
	 */
	private void initApp() {
		initTable();
		initWordList();
		displayTable();
	}

	private void initWordList() {
		wordList = new ArrayList<Word>();
		Word word = new Word();
		word.value = "cat";
		word.position = getStartPosition(dimenX - word.value.length() + 1, dimenY, word.value.length());
		fillWord2Table(word.value, word.position);
		wordList.add(word);

		word = new Word();
		word.value = "dog";
		word.position = getStartPosition(dimenX - word.value.length() + 1, dimenY, word.value.length());
		fillWord2Table(word.value, word.position);
		wordList.add(word);

		word = new Word();
		word.value = "duck";
		word.position = getStartPosition(dimenX - word.value.length() + 1, dimenY, word.value.length());
		fillWord2Table(word.value, word.position);
		wordList.add(word);
	}

	private void initTable() {
		int x = 0;
		int y = 0;
		for (y = 0; y < dimenY; y++) {
			for (x = 0; x < dimenX; x++) {
				table[x][y] = " ";
			}
		}
	}

	/*
	 * Display Function
	 */
	private void displayTable() {
		int x = 0;
		int y = 0;
		String display = "";
		for (y = 0; y < dimenY; y++) {
			for (x = 0; x < dimenX; x++) {
				display += table[x][y] + "|";
			}
			display += "\n";
		}
		Log.i(TAG, display);
	}

	/*
	 * Helper Function
	 */
	private int[] getStartPosition(int maxX, int maxY, int len) {
		int[] xy = new int[2];
		int min = 0;
		do {
			xy[0] = rand.nextInt(maxX - min) + min;
			xy[1] = rand.nextInt(maxY - min) + min;
		} while (isDuplicateStartPosition(xy, len));
		Log.i(TAG, "XY : " + xy[0] + "," + xy[1]);
		return xy;
	}

	private boolean isDuplicateStartPosition(int[] xy, int wLen) {
		int x = 0;
		int y = 0;
		for (Word w : wordList) {
			x = xy[0];
			y = xy[1];
			if (y == w.position[1] && (x >= w.position[0] || x <= (w.position[0] + w.value.length() - 1)) && (x + wLen - 1) >= w.position[0] ) {
				Log.i(TAG, "XY in other word : " + xy[0] + "," + xy[1]);
				return true;
			}
		}
		return false;
	}

	private void fillWord2Table(String word, int[] position) {
		int len = word.length();
		int start = 0;
		int X = position[0];
		int Y = position[1];
		for (start = 0; start < len; start++) {
			table[X + start][Y] = String.valueOf(word.charAt(start));
		}
	}

	public class Word {
		public String value;
		public int[] position;
	}

	/*
	 * Event Handler
	 */
	public void refreshApp(View v) {
		initApp();
	}
}