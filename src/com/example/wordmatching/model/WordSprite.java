package com.example.wordmatching.model;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class WordSprite extends Sprite {
	public int x;
	public int y;

	public WordSprite(int x, int y, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.x = x;
		this.y = y;
	}
}
