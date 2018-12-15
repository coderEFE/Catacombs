package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Constants;

/**
 * Created by coder on 12/8/2017.
 * This is the pop-up that tells you if you won or lost and gives you a little quote
 */

public class levelVerdict {
    public boolean verdict;
    public String verdictPhrase;
    private Level level;
    private float zoomIn;
    private float rotateIn;
    private int timer;
    private String typedUpText;

    private Button resetButton;
    private Button homeButton;
    private Button playButton;

    public levelVerdict (final Level level) {
        verdict = false;
        verdictPhrase = "";
        typedUpText = "";
        this.level = level;
        zoomIn = 0;
        rotateIn = 0;
        timer = 0;
        resetButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Reset", 30, 30, Color.RED,
                new Runnable () {public void run() {
                    //save progress
                    if (level.inventory.scoreDiamonds.size >= level.gameplayScreen.game.getMaxDiamonds(level.superior.currentLevel)) {
                        level.gameplayScreen.game.setMaxDiamonds(level.inventory.scoreDiamonds.size, level.superior.currentLevel);
                    }
                    //save progress
                    level.gameplayScreen.game.setFurthestLevel(verdict ? (level.superior.currentLevel == level.superior.furthestLevel ? level.superior.furthestLevel + 1 : level.superior.furthestLevel) : level.superior.furthestLevel);
                    level.superior.configureLevel(level); level.getPlayer().spawnTimer = 0;
                }},
                level);
        homeButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Home", 30, 30, Color.RED,
                new Runnable () {public void run() {
                    //save progress
                    if (level.inventory.scoreDiamonds.size >= level.gameplayScreen.game.getMaxDiamonds(level.superior.currentLevel)) {
                        level.gameplayScreen.game.setMaxDiamonds(level.inventory.scoreDiamonds.size, level.superior.currentLevel);
                    }
                    //save progress
                    level.gameplayScreen.game.setFurthestLevel(verdict ? (level.superior.currentLevel == level.superior.furthestLevel ? level.superior.furthestLevel + 1 : level.superior.furthestLevel) : level.superior.furthestLevel);
                    level.gameplayScreen.showMenuScreen(verdict ? (level.superior.currentLevel == level.superior.furthestLevel ? level.superior.furthestLevel + 1 : level.superior.furthestLevel) : level.superior.furthestLevel);
                }},
                level);
        playButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Play", 50, 30, Color.RED,
                new Runnable () {public void run() {
                    //save progress
                    if (level.inventory.scoreDiamonds.size >= level.gameplayScreen.game.getMaxDiamonds(level.superior.currentLevel)) {
                        level.gameplayScreen.game.setMaxDiamonds(level.inventory.scoreDiamonds.size, level.superior.currentLevel);
                    }
                    if (level.superior.currentLevel != 14) {
                        //save progress
                        level.gameplayScreen.game.setFurthestLevel((level.superior.currentLevel == level.superior.furthestLevel ? level.superior.furthestLevel + 1 : level.superior.furthestLevel));

                        level.lastSeenBubble = 0;
                        level.superior.currentLevel++;
                        level.superior.configureLevel(level);
                        level.getPlayer().spawnTimer = 0;
                    } else {
                        //if last level, go back to menu screen
                        level.gameplayScreen.showMenuScreen(level.superior.furthestLevel);
                    }
                }},
                level);
    }

    public void update (float delta) {
        //make hexagon zoom in and rotate
        if (zoomIn < (level.viewport.getWorldHeight() * 1f)) {
            zoomIn += delta * 100;
            level.touchPosition = new Vector2();
        } else {
            zoomIn = (level.viewport.getWorldHeight() * 1f);
        }
        if (rotateIn < 179) {
            rotateIn += delta * 150;
        } else if (rotateIn > 180) {
            rotateIn = 180;
        }
        //type text up
        if (rotateIn > 179) {
            timer += 1;
            typedUpText = verdictPhrase.substring(0, ((timer / 4) < verdictPhrase.length()) ? timer / 4 : verdictPhrase.length());
        }
        if (zoomIn > (level.viewport.getWorldHeight() * 0.8f)) {
            resetButton.update();
            homeButton.update();
            playButton.update();
        }
    }

    public void renderOutline (ShapeRenderer renderer) {
        //draw general shape of hexagon shaped circle
        renderer.set(ShapeRenderer.ShapeType.Filled);
        if (verdict) {
            renderer.setColor(new Color(0.1f, 0.5f, 0.1f, 1));
        } else {
            renderer.setColor(new Color(0.7f, 0.1f, 0.1f, 1));
        }
        renderer.ellipse(level.getPlayer().getPosition().x - level.viewport.getWorldHeight() / 2 - 4, level.getPlayer().getPosition().y - level.viewport.getWorldHeight() / 2, zoomIn, zoomIn, rotateIn, 6);
        //render buttons
        resetButton.color = new Color(verdict ? Color.FOREST : Color.RED);
        homeButton.color = new Color(verdict ? Color.FOREST : Color.RED);
        playButton.color = new Color(verdict ? Color.FOREST : Color.RED);
        resetButton.shown = true;
        homeButton.shown = true;
        //if verdict is false, don't include play button in pop-up menu
        if (!verdict) {
            //resetButton.position = new Vector2(level.getPlayer().getPosition().x - 20, level.getPlayer().getPosition().y - 30);
            resetButton.position = new Vector2(level.getPlayer().getPosition().x - 40, level.getPlayer().getPosition().y - 30);
            homeButton.position = new Vector2(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y - 30);
            playButton.shown = false;
        } else {
            resetButton.position = new Vector2(level.getPlayer().getPosition().x - 40, level.getPlayer().getPosition().y - 30);
            homeButton.position = new Vector2(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y - 30);
            playButton.position = new Vector2(level.getPlayer().getPosition().x - 30, level.getPlayer().getPosition().y - 65);
            playButton.shown = true;
        }
        if (zoomIn > (level.viewport.getWorldHeight() * 0.8f)) {
            //render each button
            resetButton.render(renderer);
            homeButton.render(renderer);
            playButton.render(renderer);
        }
    }

    public void renderText (SpriteBatch batch, BitmapFont font, BitmapFont font2, Viewport hudViewport) {
        //render text for ending
        if (rotateIn > 179) {
            font2.setColor(Color.DARK_GRAY);
            font2.draw(batch, verdict ? (level.superior.currentLevel == 14 ? "The End!" : "Victory!") : "You Lost", hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 150f) - 3, (hudViewport.getWorldHeight() / 1.2f) + 3);
            font2.setColor(Color.BLACK);
            font2.draw(batch, verdict ? (level.superior.currentLevel == 14 ? "The End!" : "Victory!") : "You Lost", hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 150f), hudViewport.getWorldHeight() / 1.2f);
            font.setColor(verdict ? Color.DARK_GRAY : Color.GRAY);
            font.draw(batch, typedUpText, (hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 170f)) + ((28 - verdictPhrase.length()) * 5f), hudViewport.getWorldHeight() / 1.5f);
        }
    }
}
