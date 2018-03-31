package com.efe.gamedev.catacombs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//Currently under development
public class CatacombsGame extends Game {

	//there will be other screens like menus and stuff
	@Override
	public void create() {
		setScreen(new GameplayScreen());
	}
}
