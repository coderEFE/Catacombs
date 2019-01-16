package com.efe.gamedev.catacombs.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by coder on 11/12/2018.
 * This class saves progress in game for furthest level, max diamonds, and items collected
 */

public class Prefs {

    private Preferences pref;

    private Preferences getPref() {
        if (pref == null) {
            pref = Gdx.app.getPreferences("My Preferences");
        }
        return pref;
    }

    //get furthest level player has visited
    public Integer getFurthestLevel(){
        return getPref().getInteger("furthestLevel", 0);
    }
    //should be called when furthest level is changed
    public void setFurthestLevel(Integer setLevel){
        getPref().putInteger("furthestLevel", setLevel);
        getPref().flush();
    }

    //get max diamonds for levels
    public Integer getMaxDiamonds(int level){
        return getPref().getInteger("maxDiamonds" + level, 0);
    }
    //should be called when the values max diamonds are changed
    public void setMaxDiamonds(Integer setMaxD, int level){
        getPref().putInteger("maxDiamonds" + level, setMaxD);
        getPref().flush();
    }

    //set whether items have been collected before
    public boolean getLevelCollected(int itemIndex) {
        return getPref().getBoolean("itemCollected" + itemIndex, false);
    }
    //should be called when a new item is collected
    public void setItemCollected(boolean setCollected, int itemIndex) {
        getPref().putBoolean("itemCollected" + itemIndex, setCollected);
        getPref().flush();
    }

    //get whether sound effects are on
    public boolean getSoundEffectsOn() {
        return getPref().getBoolean("soundEffectsOn", true);
    }
    //should be called when player modifies settings in options menu
    public void setSoundEffectsOn(boolean setSoundEffects) {
        getPref().putBoolean("soundEffectsOn", setSoundEffects);
        getPref().flush();
    }

    //get whether music is on
    public boolean getMusicOn() {
        return getPref().getBoolean("musicOn", true);
    }
    //should be called when player modifies settings in options menu
    public void setMusicOn(boolean setMusic) {
        getPref().putBoolean("musicOn", setMusic);
        getPref().flush();
    }

    //set whether vibration is on
    public boolean getVibrationOn() {
        return getPref().getBoolean("vibrationOn", true);
    }
    //should be called when player modifies settings in options menu
    public void setVibrationOn(boolean setVibration) {
        getPref().putBoolean("vibrationOn", setVibration);
        getPref().flush();
    }
}
