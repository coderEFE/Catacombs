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
    public Vector2[] letterPositions;
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
        isAWord = (collectedString.equals("KEY") || collectedString.equals("2KEYS") || collectedString.equals("GOLD") || collectedString.equals("EXIT") || collectedString.equals("RUBY") || collectedString.equals("POTION") || collectedString.equals("DOOR"));
        //notAWord = (!collectedString.equals("KEY") && !collectedString.equals("2KEYS") && !collectedString.equals("GOLD"));
        this.level = level;
        solved = false;
    }

    public void renderText (SpriteBatch batch, BitmapFont font) {
        //update word standards
        isAWord = (collectedString.equals("KEY") || collectedString.equals("2KEYS") || collectedString.equals("GOLD") || collectedString.equals("EXIT") || collectedString.equals("RUBY") || collectedString.equals("POTION") || collectedString.equals("DOOR"));
        //notAWord = (!collectedString.equals("KEY") && !collectedString.equals("2KEYS") && !collectedString.equals("GOLD"));
        //draw letters and check if they have been collected
        for (int i = 0; i < letters.length; i++) {
            //if you tap on letter and it is not already collected, it gets added to the word above your head and becomes collected.
            if (level.touchPosition.dst(letterPositions[i]) < 12 && !collected.get(i) && !level.touchPosition.equals(new Vector2())) {
                collectedString = collectedString + letters[i];
                collected.set(i, true);
            }
            //draw letter if it has not been collected
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
        if (isAWord && (collectedString.length() == letters.length)) {
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
                if (collectedString.equals("RUBY")) {
                    level.inventory.inventoryItems.add(new Item(new Vector2(), level.viewportPosition, "ruby"));
                }
                if (collectedString.equals("POTION")) {
                    level.inventory.inventoryItems.add(new Item(new Vector2(), level.viewportPosition, "ghost"));
                }
                if (collectedString.equals("DOOR")) {
                    level.doors.get(0).show = true;
                }
                //remove word
                level.words.removeIndex(level.words.indexOf(this, true));
            }
        }
    }
    //render collected text over player's head: if a real word is made, the letters flash gold before turning into an item or something. If a real word is not made but all letters have been used, the letters flash red before resetting to their original positions.
    public void renderCollectedText (SpriteBatch batch, BitmapFont font, Viewport hudViewport) {
        if (countdown > 1) {
            if ((!isAWord && collectedString.length() == letters.length)) {
                font.setColor(Color.RED);
            }
            if (isAWord && (collectedString.length() == letters.length)) {
                font.setColor(Color.GOLD);
                solved = true;
            }
        }
        else {
            font.setColor(Color.WHITE);
        }// - (collectedString.length() * 3.5f)
        font.draw(batch, collectedString, ((hudViewport.getWorldWidth() / 2f) - (level.words.indexOf(this, true) * ((collectedString.length() + 5) * 5f))), hudViewport.getWorldHeight() / 1.6f);
    }
}
