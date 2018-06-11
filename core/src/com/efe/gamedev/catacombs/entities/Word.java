package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.efe.gamedev.catacombs.Level;

/**
 * Created by coder on 1/12/2018.
 * This Word Function is used in certain levels as letters that you can collect to form words to make items
 */

public class Word {
    private String[] letters;
    private Vector2[] letterPositions;
    private String collectedString;
    private Array<Boolean> collected;
    private float countdown;
    private boolean isAWord;
    //private boolean notAWord;
    private Level level;
    public boolean solved;

    public Word (String[] letters, Vector2[] letterPositions, Level level) {
        this.letters = letters;
        this.letterPositions = letterPositions;
        collectedString = "";
        collected = new Array<Boolean>();
        for (int i = 0; i < letters.length; i++) {
            collected.add(false);
        }
        countdown = 0;
        isAWord = (collectedString.equals("KEY") || collectedString.equals("2KEYS") || collectedString.equals("GOLD") || collectedString.equals("EXIT"));
        //notAWord = (!collectedString.equals("KEY") && !collectedString.equals("2KEYS") && !collectedString.equals("GOLD"));
        this.level = level;
        solved = false;
    }

    public void renderText (SpriteBatch batch, BitmapFont font) {
        //update word standards
        isAWord = (collectedString.equals("KEY") || collectedString.equals("2KEYS") || collectedString.equals("GOLD") || collectedString.equals("EXIT"));
        //notAWord = (!collectedString.equals("KEY") && !collectedString.equals("2KEYS") && !collectedString.equals("GOLD"));
        //draw letters and check if they have been collected
        for (int i = 0; i < letters.length; i++) {
            if (level.touchPosition.dst(letterPositions[i]) < 12 && !collected.get(i)) {
                collectedString = collectedString + letters[i];
                collected.set(i, true);
            }
            if (!collected.get(i)) {
                font.draw(batch, letters[i], letterPositions[i].x, letterPositions[i].y);
            }
        }
        //check if the letters collected make a word together
        if ((!isAWord && collectedString.length() == letters.length)) {
            if (countdown < 51) {
                countdown++;
            } else {
                countdown = 0;
                collectedString = "";
                collected = new Array<Boolean>();
                for (int i = 0; i < letters.length; i++) {
                    collected.add(false);
                }
                level.touchPosition = new Vector2();
            }
        }
        if (isAWord) {
            if (countdown < 51) {
                countdown++;
            } else {
                countdown = 0;
                collectedString = "";
            }
            if (countdown > 50) {
                if (collectedString.equals("KEY")) {
                    level.inventory.inventoryItems.add(new Item(new Vector2(), level.viewportPosition, "key"));
                }
                if (collectedString.equals("2KEYS")) {
                    level.inventory.inventoryItems.add(new Item(new Vector2(), level.viewportPosition, "key"));
                    level.inventory.inventoryItems.add(new Item(new Vector2(), level.viewportPosition, "key"));
                }
                if (collectedString.equals("GOLD")) {
                    level.inventory.inventoryItems.add(new Item(new Vector2(), level.viewportPosition, "gold"));
                }
                if (collectedString.equals("EXIT")) {
                    level.exitDoor.show = true;
                }
            }
        }
    }
    public void renderCollectedText (SpriteBatch batch, BitmapFont font, Viewport hudViewport) {
        if (countdown > 1) {
            if ((!isAWord && collectedString.length() == letters.length)) {
                font.setColor(Color.RED);
            }
            if (isAWord) {
                font.setColor(Color.GOLD);
                solved = true;
            }
        }
        else {
            font.setColor(Color.WHITE);
        }
        font.draw(batch, collectedString, hudViewport.getWorldWidth() / 2f - (level.words.indexOf(this, true) * (collectedString.length() * 3f)), hudViewport.getWorldHeight() / 1.6f);
    }
}
