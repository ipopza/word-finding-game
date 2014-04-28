/**
 * Squong, free Android/AndEngine game
 * Copyright (C) 2013 Martin Varga <android@kul.is>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.example.wordmatching;

import org.andengine.util.debug.Debug;

import com.example.wordmatching.resources.ResourceManager;
import com.example.wordmatching.scene.AbstractScene;
import com.example.wordmatching.scene.MainMenuScene;
import com.example.wordmatching.scene.SplashScene;

public class SceneManager {

	private static final SceneManager INSTANCE = new SceneManager();

	private ResourceManager res = ResourceManager.getInstance();
	private AbstractScene currentScene;

	private SceneManager() {
	}

	/**
	 * Shows splash screen and loads resources on background
	 */
	public void showSplash() {
		Debug.i("Scene: Splash");
		final SplashScene splash = new SplashScene();
		setCurrentScene(splash);
		splash.loadResources();
		splash.create();
		res.engine.setScene(splash);

		res.activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				long timestamp = System.currentTimeMillis();
				MainMenuScene menu = new MainMenuScene();
				menu.loadResources();
				menu.create();

				long elapsed = System.currentTimeMillis() - timestamp;
				if (elapsed < GameActivity.SPLASH_DURATION) {
					try {
						Thread.sleep(GameActivity.SPLASH_DURATION - elapsed);
					} catch (InterruptedException e) {
						Debug.w("This should not happen");
					}
				}
				setCurrentScene(menu);
				res.engine.setScene(menu);
				splash.destroy();
				splash.unloadResources();
			}
		});
	}

	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public AbstractScene getCurrentScene() {
		return currentScene;
	}

	private void setCurrentScene(AbstractScene currentScene) {
		this.currentScene = currentScene;
	}

	public void showScene(Class<? extends AbstractScene> sceneClazz) {
		try {
			final AbstractScene scene = sceneClazz.newInstance();
			Debug.i("Showing scene " + scene.getClass().getName());
			res.activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					AbstractScene oldScene = getCurrentScene();

					if (oldScene != null) {
						oldScene.destroy();
						oldScene.unloadResources();
					}
					scene.loadResources();
					scene.create();
					setCurrentScene(scene);
					res.engine.setScene(scene);
				}
			});
		} catch (Exception e) {
			String message = "Error while changing scene";
			Debug.e(message, e);
			throw new RuntimeException(message, e);
		}
	}

}
