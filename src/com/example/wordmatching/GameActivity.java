package com.example.wordmatching;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import android.view.KeyEvent;

import com.example.wordmatching.resources.ResourceManager;
import com.example.wordmatching.scene.AbstractScene;
import com.example.wordmatching.scene.MainMenuScene;

public class GameActivity extends BaseGameActivity {
	/** Screen width, height */
	public static final int CAMERA_WIDTH = 480;
	public static final int CAMERA_HEIGHT = 800;

	/** FPS limit of the engine */
	public static final int FPS_LIMIT = 60;
	public static final long SPLASH_DURATION = 2000;

	@Override
	public synchronized void onResumeGame() {
		super.onResumeGame();
	}

	@Override
	public synchronized void onPauseGame() {
		super.onPauseGame();
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		// create simple camera
		Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		// use the easiest resolution policy
		IResolutionPolicy resolutionPolicy = new FillResolutionPolicy();
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, resolutionPolicy, camera);
		return engineOptions;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		Engine engine = new LimitedFPSEngine(pEngineOptions, FPS_LIMIT);
		return engine;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
		ResourceManager.getInstance().init(this);
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
		pOnCreateSceneCallback.onCreateSceneFinished(null);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
		SceneManager.getInstance().showSplash();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				AbstractScene scene = SceneManager.getInstance().getCurrentScene();
				if (scene instanceof MainMenuScene) {
					finish();
				} else {
					scene.onBackKeyPressed();
				}
			} catch (NullPointerException ne) {
				Debug.e("The current scene is null", ne);
			}
		}
		return false;
	}

}
