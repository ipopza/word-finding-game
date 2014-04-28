package com.example.wordmatching.scene;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;

import com.example.wordmatching.SceneManager;
import com.example.wordmatching.WordPrepare;
import com.example.wordmatching.model.ENUM;
import com.example.wordmatching.model.Word;
import com.example.wordmatching.model.WordSprite;

public class GameScene extends AbstractScene {
	private static String TAG = "POP";
	private static int CAMERA_WIDTH = 480;
	private static int CAMERA_HEIGHT = 800;
	private static int dimenX = 6;
	private static int dimenY = 10;
	private static int alphaWidth = 60, startPositionX = 65, startPositionY = 30;
	private int TIME_REMAINING = 60 * 2; // in second
	private int widthX;
	private int widthY;

	private Font mFont;
	private Line line;
	private Text timerText;

	private String[][] table;
	private WordSprite[][] spriteTable;
	private ArrayList<Word> wordList;
	private ArrayList<int[]> correctXYList;
	private ArrayList<Sprite> hoverList;
	private StringBuilder wordStack;
	private int[] lastestTouch;
	private boolean canSwipe;

	private HashMap<String, ITexture> alphabetBitmapList;
	private HashMap<String, ITextureRegion> textureRegionList;
	private ITextureRegion backgroundTextureRegion, tableBackgroundTextureRegion, hoverTextureRegion, hoverVTextureRegion, timerTextureRegion,
			helpTimerTextureRegion;

	/*
	 * Initial Function
	 */
	private void initTable() {
		int x = 0;
		int y = 0;
		Random r = new Random();
		String w;
		for (y = 0; y < dimenY; y++) {
			for (x = 0; x < dimenX; x++) {
				w = String.valueOf((char) (r.nextInt(26) + 'a'));
				table[x][y] = w;
				loadAlphabet(w);
			}
		}
	}

	private void initWordList() {
		Random r = new Random();
		int id = r.nextInt(4) + 0;
		wordList = WordPrepare.getWords(id);
		for (Word word : wordList) {
			fillWord2Table(word);
		}
	}

	/*
	 * Helper Function
	 */
	private void loadAllAlphabet() {
		for (String key : alphabetBitmapList.keySet()) {
			ITexture texture = alphabetBitmapList.get(key);
			texture.load();
			textureRegionList.put(key, TextureRegionFactory.extractFromTexture(texture));
		}
	}

	private ITexture loadTexture(final String path) {
		try {
			ITexture texture = new BitmapTexture(res.activity.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return res.activity.getAssets().open(path);
				}
			});
			return texture;
		} catch (IOException e) {
			Debug.e(e);
		}
		return null;
	}

	private void loadAlphabet(String key) {
		if (!alphabetBitmapList.containsKey(key))
			alphabetBitmapList.put(key, loadTexture("alphabets/" + key + ".png"));
	}

	private void fillWord2Table(Word word) {
		int len = word.value.length();
		int start = 0;
		String w;
		if (word.orientation == ENUM.HORIZONTAL) {
			for (start = 0; start < len; start++) {
				w = String.valueOf(word.value.charAt(start));
				table[word.x + start][word.y] = w;
				loadAlphabet(w);
			}
		} else {
			for (start = 0; start < len; start++) {
				w = String.valueOf(word.value.charAt(start));
				table[word.x][word.y + start] = w;
				loadAlphabet(w);
			}
		}
	}

	private int[] touchAt(float posX, float posY) {
		int[] touch = new int[2];
		int x = (int) Math.floor((posX * dimenX) / (alphaWidth * dimenX));
		int y = (int) Math.floor((posY * dimenY) / (alphaWidth * dimenY));
		touch[0] = (x >= dimenX ? dimenX - 1 : x);
		touch[1] = (y >= dimenY ? dimenY - 1 : y);
		return touch;
	}

	private boolean isSwipeToNextAlphabet(int[] touch) {
		if (touch[0] == lastestTouch[0] && touch[1] == lastestTouch[1])
			return false;
		return true;
	}

	private Word isCorrectWord(String word) {
		for (Word w : wordList) {
			if (w.value.equalsIgnoreCase(word))
				return w;
		}
		return null;
	}

	private String getTime() {
		int minutes = TIME_REMAINING / 60;
		int seconds = TIME_REMAINING % 60;
		return String.format("%d:%02d", minutes, seconds);
	}

	private void showDialog(final String title, final String msg) {
		res.activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				AlertDialog.Builder alert = new AlertDialog.Builder(res.activity);
				alert.setTitle(title);
				alert.setMessage(msg);
				alert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SceneManager.getInstance().showScene(GameScene.class);
					}
				});
				alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SceneManager.getInstance().showScene(MainMenuScene.class);
					}
				});

				alert.show();
			}
		});
	}

	/*
	 * Handler
	 */
	TimerHandler mTimerHandler = new TimerHandler(1f, true, new ITimerCallback() {

		@Override
		public void onTimePassed(TimerHandler pTimerHandler) {

			TIME_REMAINING--;
			timerText.setText(getTime());
			if (TIME_REMAINING == 0) {
				showDialog("TIME'S UP", "You lose, Please try again!!");
			}
		}
	});

	@Override
	public void loadResources() {
		widthX = dimenX * alphaWidth;
		widthY = dimenY * alphaWidth;

		lastestTouch = new int[2];

		correctXYList = new ArrayList<int[]>();
		table = new String[dimenX][dimenY];
		spriteTable = new WordSprite[dimenX][dimenY];
		alphabetBitmapList = new HashMap<String, ITexture>();
		textureRegionList = new HashMap<String, ITextureRegion>();

		// 1 - Set up bitmap textures
		ITexture backgroundTexture = loadTexture("gfx/background2.png");
		ITexture tableBackgroundTexture = loadTexture("gfx/table.png");
		ITexture hoverBackgroundTexture = loadTexture("gfx/hover2.png");
		//ITexture hoverVBackgroundTexture = loadTexture("gfx/hoverV.png");
		ITexture timerTexture = loadTexture("gfx/timer.png");
		ITexture helpTimerTexture = loadTexture("gfx/helpTime.png");
		initTable();
		initWordList();

		// 2 - Load bitmap textures into VRAM
		backgroundTexture.load();
		tableBackgroundTexture.load();
		hoverBackgroundTexture.load();
		//hoverVBackgroundTexture.load();
		timerTexture.load();
		helpTimerTexture.load();
		loadAllAlphabet();

		// 3 - Set up texture regions
		backgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		tableBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(tableBackgroundTexture);
		hoverTextureRegion = TextureRegionFactory.extractFromTexture(hoverBackgroundTexture);
		//hoverVTextureRegion = TextureRegionFactory.extractFromTexture(hoverVBackgroundTexture);
		timerTextureRegion = TextureRegionFactory.extractFromTexture(timerTexture);
		helpTimerTextureRegion = TextureRegionFactory.extractFromTexture(helpTimerTexture);

		// 4 - Set up Font
		mFont = FontFactory.create(res.activity.getFontManager(), res.activity.getTextureManager(), 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 24, Color.WHITE.hashCode());
		mFont.load();
	}

	@Override
	public void create() {
		// 1 - Create new scene & add background
		final Scene scene = new Scene();
		Sprite sprite = new Sprite(0, 0, backgroundTextureRegion, res.activity.getVertexBufferObjectManager());
		scene.attachChild(sprite);

		// 2 - Add Table Background
		Sprite tableBackground = new Sprite(startPositionX, startPositionY, widthX, widthY, tableBackgroundTextureRegion,
				res.activity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				try {
					int posX, posY;
					Sprite hover;
					if (pSceneTouchEvent.isActionDown()) {
						hoverList = new ArrayList<Sprite>();
						wordStack = new StringBuilder();
						lastestTouch = touchAt(pTouchAreaLocalX, pTouchAreaLocalY);
						canSwipe = true;
						for (int[] xy : correctXYList) {
							if (xy[0] == lastestTouch[0] && xy[1] == lastestTouch[1]) {
								canSwipe = false;
								return false;
							}
						}

						if (canSwipe) {
							posX = (lastestTouch[0] * alphaWidth) + startPositionX;
							posY = (lastestTouch[1] * alphaWidth) + startPositionY;
							hover = new Sprite(posX, posY, alphaWidth, alphaWidth, hoverTextureRegion, getVertexBufferObjectManager());
							scene.attachChild(hover);

							hoverList.add(hover);
							wordStack.append(table[lastestTouch[0]][lastestTouch[1]]);
						}
					}

					if (pSceneTouchEvent.isActionUp()) {
						if (canSwipe) {
							Word w = isCorrectWord(wordStack.toString());
							if (w == null) {
								Log.i(TAG, "Not found!! : " + wordStack.toString());
								for (Sprite h : hoverList) {
									scene.detachChild(h);
								}
							} else {
								Log.i(TAG, "Found!! : " + wordStack.toString());

								float x = w.text.getX();
								float y = w.text.getY() + (w.text.getHeight() / 2);
								line = new Line(x, y, x + w.text.getWidth(), y, 3.0f, getVertexBufferObjectManager());
								line.setColor(Color.RED);
								scene.attachChild(line);

								int[] xy = new int[2];
								xy[0] = w.x;
								xy[1] = w.y;
								correctXYList.add(xy);

								wordList.remove(w);
								if (wordList.size() == 0) {
									showDialog("YOU WIN", "Your score : " + TIME_REMAINING);
								}
							}
						}
						canSwipe = false;
						Log.i(TAG, correctXYList.toString());
					}

					if (canSwipe && pTouchAreaLocalX >= 0 && pTouchAreaLocalX <= widthX && pTouchAreaLocalY >= 0 && pTouchAreaLocalY <= widthY) {
						int[] touch = touchAt(pTouchAreaLocalX, pTouchAreaLocalY);
						if (isSwipeToNextAlphabet(touch)) {
							wordStack.append(table[touch[0]][touch[1]]);
							lastestTouch = touch;

							posX = (touch[0] * alphaWidth) + startPositionX;
							posY = (touch[1] * alphaWidth) + startPositionY;
							hover = new Sprite(posX, posY, alphaWidth, alphaWidth, hoverTextureRegion, getVertexBufferObjectManager());
							scene.attachChild(hover);

							hoverList.add(hover);
						}
					}
				} catch (Exception e) {
					for (Sprite h : hoverList) {
						scene.detachChild(h);
					}
					e.printStackTrace();
				}
				return true;
			}
		};
		scene.attachChild(tableBackground);

		// 3 - Add Alphabets
		int x, y, posX, posY;
		for (y = 0; y < dimenY; y++) {
			posY = (y * alphaWidth) + startPositionY;
			for (x = 0; x < dimenX; x++) {
				posX = (x * alphaWidth) + startPositionX;
				spriteTable[x][y] = new WordSprite(x, y, posX, posY, textureRegionList.get(table[x][y]), res.activity.getVertexBufferObjectManager());
				scene.attachChild(spriteTable[x][y]);
			}
		}

		// 4 - Add Font
		posX = startPositionX;
		posY = startPositionY + (dimenY * alphaWidth) + 10;
		for (Word w : wordList) {
			w.text = new Text(posX, posY, this.mFont, w.value.toUpperCase(), new TextOptions(HorizontalAlign.LEFT),
					res.activity.getVertexBufferObjectManager());
			scene.attachChild(w.text);

			posX += w.text.getWidth() + 30;
			if (posX > (dimenX * alphaWidth)) {
				posX = startPositionX;
				posY += w.text.getHeight();
			}
		}

		// 6 - Timer & Helper
		posX = CAMERA_WIDTH - 80;
		posY = CAMERA_HEIGHT - 55;
		timerText = new Text(posX, posY, this.mFont, getTime(), new TextOptions(HorizontalAlign.LEFT), res.activity.getVertexBufferObjectManager());
		scene.attachChild(timerText);

		posX = CAMERA_WIDTH - 145;
		posY = CAMERA_HEIGHT - 70;
		sprite = new Sprite(posX, posY, timerTextureRegion, res.activity.getVertexBufferObjectManager());
		scene.attachChild(sprite);

		posX = CAMERA_WIDTH - 250;
		posY = CAMERA_HEIGHT - 70;
		ButtonSprite helper = new ButtonSprite(posX, posY, helpTimerTextureRegion, res.activity.getVertexBufferObjectManager(),
				new OnClickListener() {

					@Override
					public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
						pButtonSprite.setEnabled(false);
						pButtonSprite.setColor(Color.BLACK);
						TIME_REMAINING += 60;
					}
				});
		scene.attachChild(helper);

		// 7 - Register Event
		scene.registerTouchArea(helper);
		scene.registerTouchArea(tableBackground);
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		scene.registerUpdateHandler(mTimerHandler);
		setChildScene(scene);
	}

	@Override
	public void unloadResources() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}
}
