package com.efe.gamedev.catacombs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.efe.gamedev.catacombs.util.Prefs;

//Currently under development
public class CatacombsGame extends Game {

    //preferences
    private Prefs prefs;

	//These are the menu and game-play screens
	@Override
	public void create() {
		showMenuScreen(0);
        prefs = new Prefs();
	}

	//show different screens in game
	public void showMenuScreen(int currentLevel) {
		setScreen(new MenuScreen(this, currentLevel));
	}

	public void showGamePlayScreen(int levelNum) {
		setScreen(new GameplayScreen(this, levelNum));
	}

	//set and return the furthest level so far
    public void setFurthestLevel(Integer furthestLevel) {
        prefs.setFurthestLevel(furthestLevel);
    }

    public Integer getFurthestLevel(){
	    try {
            return prefs.getFurthestLevel();
        } catch (NullPointerException e ) {
	        return 0;
        }
    }

    //set and return number of dimaonds collected per level
    public void setMaxDiamonds(Integer newMaxDiamonds, int level) {
        prefs.setMaxDiamonds(newMaxDiamonds, level);
    }

    public Integer getMaxDiamonds(int level){
        try {
            return prefs.getMaxDiamonds(level);
        } catch (NullPointerException e ) {
            return 0;
        }
    }

    //set and return whether items have been collected
    public void setItemCollected(boolean setCollected, int itemIndex) {
        prefs.setItemCollected(setCollected, itemIndex);
    }

    public boolean getItemCollected(int itemIndex){
        try {
            return prefs.getLevelCollected(itemIndex);
        } catch (NullPointerException e ) {
            return false;
        }
    }
}
