package com.efe.gamedev.catacombs;

//This is my first published game called "Catacombs". Enjoy!!!
//Catacombs is created by Ethan F. Erickson

import com.badlogic.gdx.Game;
import com.efe.gamedev.catacombs.util.Prefs;

//Catacombs, the video game class
public class CatacombsGame extends Game {

    //preferences
    private Prefs prefs;

	//These are the menu and game-play screens
	@Override
	public void create() {
		showMenuScreen();
        prefs = new Prefs();
	}

	//show different screens in game
	void showMenuScreen() {
		setScreen(new MenuScreen(this));
	}

	void showGamePlayScreen(int levelNum) {
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

    //set and return number of diamonds collected per level
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

    boolean getItemCollected(int itemIndex){
        try {
            return prefs.getLevelCollected(itemIndex);
        } catch (NullPointerException e ) {
            return false;
        }
    }
}
