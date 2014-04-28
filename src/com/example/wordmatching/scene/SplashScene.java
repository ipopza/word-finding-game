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
package com.example.wordmatching.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.example.wordmatching.GameActivity;

public class SplashScene extends AbstractScene {

	private Sprite splash;

	@Override
	public void loadResources() {
		res.loadSplashResources();
	}

	@Override
	public void create() {
		splash = new Sprite(GameActivity.CAMERA_WIDTH / 2 - res.splashRegion.getWidth() / 2, GameActivity.CAMERA_HEIGHT / 2
				- res.splashRegion.getHeight() / 2, res.splashRegion, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		attachChild(splash);
	}

	@Override
	public void unloadResources() {
		res.unloadSplashResources();
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
