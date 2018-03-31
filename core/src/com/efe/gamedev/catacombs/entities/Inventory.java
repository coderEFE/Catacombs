package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.items.Diamond;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 11/24/2017.
 * This is the Player's Inventory where the Player holds his/her items.
 */

public class Inventory {

    private static final String TAG = Inventory.class.getName();

    private Level level;
    public Vector2 position;
    private float width;
    private float height;
    private float inventorySlots = 5;
    public DelayedRemovalArray<Item> inventoryItems;
    public int selectedItem = -1;
    private Vector2 viewportPosition;
    //is game paused
    public boolean paused;
    private String stage;
    private Button resetButton;
    private Button homeButton;
    private Button playButton;
    public Button speechButton;
    private Button mapButton;
    private Button backButton;
    private Button yesButton;
    private Button noButton;

    //keep track of diamonds
    public DelayedRemovalArray<Diamond> scoreDiamonds;

    public Inventory (final Level level) {
        this.level = level;
        position = new Vector2();
        width = 0;
        height = 0;
        inventoryItems = new DelayedRemovalArray<Item>();
        viewportPosition = level.viewportPosition;
        scoreDiamonds = new DelayedRemovalArray<Diamond>();
        paused = false;
        stage = "Main";
        resetButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Reset", 30, 30, Color.BLUE,
                new Runnable () {public void run() { stage="ValidateReset"; }},
                level);
        homeButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Home", 30, 30, Color.BLUE,
                new Runnable () {public void run() { stage="ValidateHome"; }},
                level);
        speechButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Speech", 30, 30, Color.BLUE,
                new Runnable () {public void run() { stage="ValidateSpeech"; }},
                level);
        mapButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Map", 30, 30, Color.BLUE,
                new Runnable () {public void run() { stage = "Map"; }},
                level);
        backButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Back", 20, 20, Color.BLUE,
                new Runnable () {public void run() { stage = "Main"; playButton.playMove = 0; homeButton.doorMove = 0; resetButton.arrowMove = -200; }},
                level);
        playButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Play", 50, 30, Color.BLUE,
                new Runnable () {public void run() {
                    paused = false;
                    playButton.playMove = 0; homeButton.doorMove = 0; resetButton.arrowMove = -200;
                    level.getPlayer().armRotate = 0;
                    level.getPlayer().legRotate = 170;
                    level.getPlayer().startTime = TimeUtils.nanoTime();
                }},
                level);
        yesButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Yes", 30, 30, Color.BLUE,
                new Runnable () {public void run() {
                    playButton.playMove = 0; homeButton.doorMove = 0; resetButton.arrowMove = -200;
                    if (stage.equals("ValidateReset")) {
                        level.superior.configureLevel(level); level.getPlayer().spawnTimer = 0;
                    } else if (stage.equals("ValidateHome")) {
                        /*TODO: Make a home menu!*/
                    } else if (stage.equals("ValidateSpeech")) {
                        speechButton.speech = !speechButton.speech;
                        if (!speechButton.speech) {
                            paused = false;
                            level.getPlayer().armRotate = 0;
                            level.getPlayer().legRotate = 170;
                            level.getPlayer().startTime = TimeUtils.nanoTime();
                            level.touchLocked = false;
                            level.show = false;
                            level.continueBubbles = false;
                        } else {
                            level.superior.configureLevel(level); level.getPlayer().spawnTimer = 0;
                        }
                    }
                }},
                level);
        noButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "No", 30, 30, Color.BLUE,
                new Runnable () {public void run() { stage = "Main"; playButton.playMove = 0; homeButton.doorMove = 0; resetButton.arrowMove = -200; }},
                level);
    }

    public void update () {
        //set up position, width, height and the touch position (viewportPosition)
        position = new Vector2(level.getPlayer().getPosition().x - (level.viewport.getWorldWidth() / 2.7f), level.getPlayer().getPosition().y - (level.viewport.getWorldHeight() / 3));
        width = level.viewport.getWorldWidth() / 1.2f;
        height = level.viewport.getWorldHeight() / 7;
        viewportPosition.set(level.viewportPosition);
        //see if player is trying to select an inventory item
        trySelecting();
        //see if player is pressing pause button
        if (level.touchPosition.dst(new Vector2((level.getPlayer().getPosition().x - level.viewport.getWorldWidth() / 2) + 2, level.getPlayer().getPosition().y - level.viewport.getWorldHeight() / 2)) < 18 && !level.touchPosition.equals(new Vector2()) && level.getPlayer().jumpState == Enums.JumpState.GROUNDED) {
            paused = true;
            stage = "Main";
        }
    }

    public void updatePause () {
        if (paused) {
            resetButton.update();
            homeButton.update();
            playButton.update();
        }
    }

    private void trySelecting () {
        if (viewportPosition.y < position.y && viewportPosition.y > position.y - height) {
            for (int i = 0; i < inventoryItems.size; i++) {
                Item item = inventoryItems.get(i);
                if (viewportPosition.x > position.x + (i * (width / inventorySlots)) && viewportPosition.x < position.x + (i * (width / inventorySlots)) + (width / inventorySlots)) {
                    selectedItem = inventoryItems.indexOf(item, true);
                }
            }
        }
    }

    public void render (ShapeRenderer renderer) {
        //draw inventory
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.DARK_GRAY);
        renderer.rectLine(position.x, position.y, position.x + width, position.y, 2);
        renderer.rectLine(position.x, position.y - height, position.x + width, position.y - height, 2);
        //make inventory bars
        for (int i = 0; i < inventorySlots + 1; i++) {
            if ((selectedItem == i || selectedItem + 1 == i) && selectedItem != -1) {
                renderer.setColor(Color.RED);
            } else {
                renderer.setColor(Color.DARK_GRAY);
            }
            renderer.rectLine(position.x + (i * (width / inventorySlots)), position.y, position.x + (i * (width / inventorySlots)), position.y - height, 2);
        }
        //add selected bars if bar is tapped
        if (selectedItem != -1) {
            renderer.setColor(Color.RED);
            renderer.rectLine(position.x + (selectedItem * (width / inventorySlots)), position.y, position.x + (selectedItem * (width / inventorySlots)) + (width / inventorySlots), position.y, 2);
            renderer.rectLine(position.x + (selectedItem * (width / inventorySlots)), position.y - height, position.x + (selectedItem * (width / inventorySlots)) + (width / inventorySlots), position.y - height, 2);
            //make player hold the item selected
            if (inventoryItems.size != 0) {
                level.getPlayer().holdItem(inventoryItems.get(selectedItem).itemType);
            } else {
                level.getPlayer().holdItem("");
            }
        } else {
            level.getPlayer().holdItem("");
        }
        //delete first item when inventory gets too big
        if (inventoryItems.size > inventorySlots) {
            inventoryItems.removeIndex(0);
        }
        //set item position's inside the inventory when they are collected
        for (Item item : inventoryItems) {
            item.position.set(new Vector2(position.x + (width / 15) + (inventoryItems.indexOf(item, true) * (width / inventorySlots)), (position.y - (height / 2)) + (level.getPlayer().getVelocity().y / 60)));
            item.collected = true;
            item.render(renderer);
        }

        //draw collected diamonds: MAX DIAMONDS PER LEVEL IS 3.
        for (Diamond diamond : scoreDiamonds) {
            diamond.render(renderer);
            diamond.position.set(new Vector2(position.x + (level.viewport.getWorldWidth() / 1.25f) - (scoreDiamonds.indexOf(diamond, true) * diamond.diamondWidth * 1.2f), position.y + (level.viewport.getWorldHeight() / 1.3f)));
        }
        //remove a diamond if diamond count is over the MAX_DIAMONDS count of 3
        if (scoreDiamonds.size > Constants.MAX_DIAMONDS) {
            scoreDiamonds.removeIndex(0);
        }
    }

    public void renderPause (ShapeRenderer renderer) {
        //pause button
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(new Color(0.3f, 0.3f, 1.0f, 1));
        renderer.circle((level.getPlayer().getPosition().x - level.viewport.getWorldWidth() / 2) + 2, level.getPlayer().getPosition().y - level.viewport.getWorldHeight() / 2, 20, 6);
        renderer.setColor(Color.BLUE);
        renderer.rect((level.getPlayer().getPosition().x - level.viewport.getWorldWidth() / 2) + 3, (level.getPlayer().getPosition().y - level.viewport.getWorldHeight() / 2) + 3, 3, 10);
        renderer.rect((level.getPlayer().getPosition().x - level.viewport.getWorldWidth() / 2) + 8, (level.getPlayer().getPosition().y - level.viewport.getWorldHeight() / 2) + 3, 3, 10);
        //pause menu
        if (paused) {
            renderer.setColor(new Color(0.3f, 0.3f, 1.0f, 1));
            renderer.rect((level.getPlayer().getPosition().x - level.viewport.getWorldWidth() / 2.5f), level.getPlayer().getPosition().y - level.viewport.getWorldHeight() / 2.5f, level.viewport.getWorldWidth() / 1.3f, level.viewport.getWorldHeight() / 1.2f);
            if (level.viewport.getWorldWidth() < 250) {
                resetButton.position = new Vector2(level.getPlayer().getPosition().x - 40, level.getPlayer().getPosition().y - 20);
                homeButton.position = new Vector2(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y - 20);
                playButton.position = new Vector2(level.getPlayer().getPosition().x - 30, level.getPlayer().getPosition().y - 55);
                speechButton.position = new Vector2(level.getPlayer().getPosition().x - 70, level.getPlayer().getPosition().y - 55);
                mapButton.position = new Vector2(level.getPlayer().getPosition().x + 30, level.getPlayer().getPosition().y - 55);
            }  else {
                resetButton.position = new Vector2(level.getPlayer().getPosition().x - 80, level.getPlayer().getPosition().y - 55);
                homeButton.position = new Vector2(level.getPlayer().getPosition().x + 40, level.getPlayer().getPosition().y - 55);
                playButton.position = new Vector2(level.getPlayer().getPosition().x - 30, level.getPlayer().getPosition().y - 55);
                speechButton.position = new Vector2(level.getPlayer().getPosition().x - 60, level.getPlayer().getPosition().y - 20);
                mapButton.position = new Vector2(level.getPlayer().getPosition().x + 20, level.getPlayer().getPosition().y - 20);
            }
            backButton.position = new Vector2(level.getPlayer().getPosition().x - 80, level.getPlayer().getPosition().y - 60);
            yesButton.position = new Vector2(level.getPlayer().getPosition().x + 5, level.getPlayer().getPosition().y - 50);
            noButton.position = new Vector2(level.getPlayer().getPosition().x - 45, level.getPlayer().getPosition().y - 50);
            if (stage.equals("Main")) {
                resetButton.render(renderer);
                homeButton.render(renderer);
                playButton.render(renderer);
                speechButton.render(renderer);
                mapButton.render(renderer);
            } else if (stage.equals("Map")) {
                //map of catacombs
                for(int i = 0; i < level.catacombs.size; i++) {
                    renderer.setColor(Color.BLUE);
                    //renderer.circle(level.getPlayer().getPosition().x + ((level.catacombs.get(i).position.x + 15) / 16f), level.getPlayer().getPosition().y + ((level.catacombs.get(i).position.y) / 20f), 5, 6);
                    renderer.ellipse(level.getPlayer().getPosition().x + ((level.catacombs.get(i).position.x + 15) / 16f), level.getPlayer().getPosition().y + ((level.catacombs.get(i).position.y) / 20f), 11, 9, 6);
                }
                renderer.setColor(new Color(Color.BLUE.r / 3f, Color.BLUE.g / 3f, Color.BLUE.b / 3f, 1));
                renderer.circle(level.getPlayer().getPosition().x + (level.getPlayer().getPosition().x / 16f), level.getPlayer().getPosition().y + (level.getPlayer().getPosition().y / 20f), 1.5f + (playButton.playMove / 40f), 4);
                backButton.render(renderer);
            } else if (stage.equals("ValidateReset") || stage.equals("ValidateSpeech") || stage.equals("ValidateHome")) {
                yesButton.render(renderer);
                noButton.render(renderer);
            }
        }
    }

    public void renderText (SpriteBatch batch, BitmapFont font, BitmapFont font2, Viewport hudViewport) {
        font.setColor(Color.BLACK);
        font.draw(batch, "PAUSED", hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 150f), hudViewport.getWorldHeight() / 1.2f);
        if (stage.equals("ValidateReset")) {
            font2.draw(batch, "Would you like to restart the level?", hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 200f), hudViewport.getWorldHeight() / 1.5f);
        } else if (stage.equals("ValidateHome")) {
            font2.draw(batch, "Would you like to exit to home menu?", hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 210f), hudViewport.getWorldHeight() / 1.5f);
        } else if (stage.equals("ValidateSpeech")) {
            font2.draw(batch, speechButton.speech ? " Would you like to play the\n level without instructions?" : "   Would you like to replay\n the level with instructions?", hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 170f), hudViewport.getWorldHeight() / 1.5f);
        }
    }

}
