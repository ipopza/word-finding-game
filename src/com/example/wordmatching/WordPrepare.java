package com.example.wordmatching;

import java.util.ArrayList;

import com.example.wordmatching.model.ENUM;
import com.example.wordmatching.model.Word;

public class WordPrepare {
	public static ArrayList<Word> getWords(int id) {

		ArrayList<Word> wordList = new ArrayList<Word>();
		Word word;
		switch (id) {
		case 0:
			wordList = new ArrayList<Word>();
			word = new Word();
			word.value = "dog";
			word.x = 2;
			word.y = 0;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "duck";
			word.x = 0;
			word.y = 1;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);

			word = new Word();
			word.value = "cat";
			word.x = 2;
			word.y = 2;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "ant";
			word.x = 3;
			word.y = 2;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);

			word = new Word();
			word.value = "girl";
			word.x = 5;
			word.y = 4;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);

			word = new Word();
			word.value = "animal";
			word.x = 0;
			word.y = 7;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "man";
			word.x = 3;
			word.y = 7;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);
			break;
		case 1:
			wordList = new ArrayList<Word>();
			word = new Word();
			word.value = "cute";
			word.x = 0;
			word.y = 1;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "tool";
			word.x = 2;
			word.y = 1;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);

			word = new Word();
			word.value = "love";
			word.x = 2;
			word.y = 4;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "shirt";
			word.x = 1;
			word.y = 6;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "home";
			word.x = 2;
			word.y = 6;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);

			word = new Word();
			word.value = "rat";
			word.x = 4;
			word.y = 6;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);
			break;
		case 2:
			wordList = new ArrayList<Word>();
			word = new Word();
			word.value = "park";
			word.x = 1;
			word.y = 1;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "egg";
			word.x = 2;
			word.y = 2;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "pencil";
			word.x = 5;
			word.y = 1;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);

			word = new Word();
			word.value = "word";
			word.x = 0;
			word.y = 4;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "time";
			word.x = 0;
			word.y = 6;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "move";
			word.x = 2;
			word.y = 6;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);
			break;
		case 3:
			wordList = new ArrayList<Word>();
			word = new Word();
			word.value = "computer";
			word.x = 2;
			word.y = 0;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);

			word = new Word();
			word.value = "day";
			word.x = 0;
			word.y = 1;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);

			word = new Word();
			word.value = "monk";
			word.x = 2;
			word.y = 2;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "study";
			word.x = 0;
			word.y = 4;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "year";
			word.x = 4;
			word.y = 4;
			word.orientation = ENUM.VERTICAL;
			wordList.add(word);

			word = new Word();
			word.value = "car";
			word.x = 0;
			word.y = 7;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);

			word = new Word();
			word.value = "apple";
			word.x = 1;
			word.y = 9;
			word.orientation = ENUM.HORIZONTAL;
			wordList.add(word);
			break;
		}
		return wordList;
	}
}
