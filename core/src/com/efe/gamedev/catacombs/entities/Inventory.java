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
import com.efe.gamedev.catacombs.util.Prefs;

import java.util.Locale;

/**
 * Created by coder on 11/24/2017.
 * This is the Player's Inventory where the Player holds his/her items.
 */

public class Inventory {

    private static final String TAG = Inventory.class.getName();

    private Level level;
    public Vector2 position;
    public float width;
    public float height;
    public float inventorySlots = 5;
    public DelayedRemovalArray<Item> inventoryItems;
    public int selectedItem = -1;
    //is game paused
    public boolean paused;
    private String stage;
    private Button resetButton;
    private Button homeButton;
    private Button playButton;
    public Button speechButton;
    private Button guideButton;
    private Button mapButton;
    private Button backButton;
    private Button yesButton;
    private Button noButton;

    //keep track of diamonds
    public DelayedRemovalArray<Diamond> scoreDiamonds;

    //new items
    public boolean newItem;
    public String newItemType;
    private Item newCollectedItem;
    private float newItemTimer;
    private float newItemOpacity;
    public boolean dragItem;
    public String message;
    private float messageTimer;

    public Inventory (final Level level) {
        this.level = level;
        position = new Vector2();
        width = 0;
        height = 0;
        inventoryItems = new DelayedRemovalArray<Item>();
        scoreDiamonds = new DelayedRemovalArray<Diamond>();
        paused = false;
        newItem = false;
        newItemType = "";
        newCollectedItem = new Item(new Vector2(), level.viewportPosition, newItemType);
        newItemTimer = 0;
        newItemOpacity = 1f;
        dragItem = false;
        message = "";
        messageTimer = 100;
        stage = "Main";
        resetButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Reset", 30, 30, Color.BLUE,
                () -> stage="ValidateReset",
                level);
        homeButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Home", 30, 30, Color.BLUE,
                () -> stage="ValidateHome",
                level);
        speechButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Speech", 30, 30, Color.BLUE,
                () -> stage="ValidateSpeech",
                level);
        guideButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Guide", 30, 30, Color.BLUE,
                () -> stage="Instructions",
                level);
        mapButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Map", 30, 30, Color.BLUE,
                () -> stage = "Map",
                level);
        backButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Back", 20, 20, Color.BLUE,
                () -> { stage = "Main"; playButton.playMove = 0; homeButton.doorMove = 0; resetButton.arrowMove = -200; },
                level);
        playButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Play", 50, 30, Color.BLUE,
                () -> {
                    paused = false;
                    playButton.playMove = 0; homeButton.doorMove = 0; resetButton.arrowMove = -200;
                    level.getPlayer().armRotate = 0;
                    level.getPlayer().legRotate = 170;
                    level.getPlayer().startTime = TimeUtils.nanoTime();
                },
                level);
        yesButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "Yes", 30, 30, Color.BLUE,
                () -> {
                    playButton.playMove = 0; homeButton.doorMove = 0; resetButton.arrowMove = -200;
                    if (stage.equals("ValidateReset")) {
                        level.superior.configureLevel(level); level.getPlayer().spawnTimer = 0;
                    } else if (stage.equals("ValidateHome")) {
                        //save progress
                        level.gameplayScreen.game.setFurthestLevel(level.superior.furthestLevel);
                        level.gameplayScreen.showMenuScreen(level.superior.furthestLevel);
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
                },
                level);
        noButton = new Button(new Vector2(level.viewport.getWorldWidth() / 2f, level.viewport.getWorldHeight() / 2f), "No", 30, 30, Color.BLUE,
                () -> { stage = "Main"; playButton.playMove = 0; homeButton.doorMove = 0; resetButton.arrowMove = -200; },
                level);
    }

    public void update () {
        //set up position, width, height
        position = new Vector2(level.getPlayer().getPosition().x - (level.viewport.getWorldWidth() / 2.7f), level.getPlayer().getPosition().y - (level.viewport.getWorldHeight() / 3));
        width = level.viewport.getWorldWidth() / 1.2f;
        height = level.viewport.getWorldHeight() / 7;
        //see if player is trying to select an inventory item
        trySelecting();
        //see if player is pressing pause button
        if (level.pressDown && level.touchPosition.dst(new Vector2((level.getPlayer().getPosition().x - level.viewport.getWorldWidth() / 2) + 2, level.getPlayer().getPosition().y - level.viewport.getWorldHeight() / 2)) < 18 && !level.touchPosition.equals(new Vector2()) && level.getPlayer().jumpState == Enums.JumpState.GROUNDED && !level.torchFade) {
            paused = true;
            stage = "Main";
        }
    }

    public void updatePause () {
        //pause buttons
        if (paused) {
            resetButton.update();
            homeButton.update();
            playButton.update();
        }
        //click on new Item panel
        if (newItem && level.touchPosition.x > (level.getPlayer().getPosition().x - 86) && level.touchPosition.x < (level.getPlayer().getPosition().x - 86) + 164 && level.touchPosition.y > (level.getPlayer().getPosition().y + 50) && level.touchPosition.y < (level.getPlayer().getPosition().y + 50) + (level.viewport.getWorldHeight() / 8f)) {
            level.touchPosition = new Vector2();
            //play confirm sound
            level.gameplayScreen.sound5.play();
            newItem = false;
            //save that you have already seen this item in preferences
            level.gameplayScreen.game.setItemCollected(true, level.collectedItemTypes.indexOf(newItemType, true));
        }
        //flashing triangle for new Item panel
        if (newItem) {
            newItemTimer ++;
            if (newItemTimer > 25) {
                newItemOpacity = 0;
            }
            if (newItemTimer > 50) {
                newItemOpacity = 1f;
                newItemTimer = 0;
            }
        } else {
            newItemTimer = 0;
            newItemOpacity = 1f;
        }
    }

    private void trySelecting () {
        //clicking on a box selects it
        if (level.touchPosition.y < position.y && level.touchPosition.y > position.y - height && !newItem) {
            for (int i = 0; i < inventoryItems.size; i++) {
                Item item = inventoryItems.get(i);
                if (level.touchPosition.x > position.x + (i * (width / inventorySlots)) && level.touchPosition.x < position.x + (i * (width / inventorySlots)) + (width / inventorySlots) && !dragItem) {
                    selectedItem = inventoryItems.indexOf(item, true);
                }
            }
        }
        //reset selection box
        if (inventoryItems.size == 0) {
            selectedItem = -1;
        }
    }

    public int touchItem (String touchItemType) {
        //boolean for output
        int touchedItemIndex = -1;
        //loop through items
        if (level.touchPosition.y < position.y && level.touchPosition.y > position.y - height) {
            for (int i = 0; i < inventoryItems.size; i++) {
                Item item = inventoryItems.get(i);
                if ((level.touchPosition.x > position.x + (i * (width / inventorySlots))) && (level.touchPosition.x < position.x + (i * (width / inventorySlots)) + (width / inventorySlots)) && item.itemType.equals(touchItemType)) {
                    touchedItemIndex = i;
                }
            }
        }
        return touchedItemIndex;
    }

    public void render (ShapeRenderer renderer) {
        //if player is not battling boss in final level
        if (level.superior.currentLevel != 14 || (level.currentBubble <= 50 || level.currentBubble >= 52)) {
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
        }
            //add selected bars if bar is tapped
            if (selectedItem != -1) {
                //draw red lines around selectedItem
                if (level.superior.currentLevel != 14 || (level.currentBubble <= 50 || level.currentBubble >= 52)) {
                    renderer.setColor(Color.RED);
                    renderer.rectLine(position.x + (selectedItem * (width / inventorySlots)), position.y, position.x + (selectedItem * (width / inventorySlots)) + (width / inventorySlots), position.y, 2);
                    renderer.rectLine(position.x + (selectedItem * (width / inventorySlots)), position.y - height, position.x + (selectedItem * (width / inventorySlots)) + (width / inventorySlots), position.y - height, 2);
                }
                //make player hold the item selected
                if (inventoryItems.size != 0) {
                    if (!level.getPlayer().fighting) {
                        level.getPlayer().holdItem(inventoryItems.get(selectedItem).itemType);
                        //if item has not been collected before, pop up a screen telling what item is.
                        if (!level.collectedItems[level.collectedItemTypes.indexOf(inventoryItems.get(selectedItem).itemType, true)] && !level.gameplayScreen.game.getItemCollected(level.collectedItemTypes.indexOf(newItemType, true))) {
                            level.collectedItems[level.collectedItemTypes.indexOf(inventoryItems.get(selectedItem).itemType, true)] = true;
                            //reset stuff
                            level.touchPosition = new Vector2();
                            newItemType = inventoryItems.get(selectedItem).itemType;
                            newItem = true;
                            //play sound
                            level.gameplayScreen.sound4.play();
                            //Gdx.app.log(TAG, "" + level.gameplayScreen.game.getItemCollected(level.collectedItemTypes.indexOf(inventoryItems.get(selectedItem).itemType, true)));
                        }
                    }
                } else {
                    level.getPlayer().holdItem("");
                }
            } else {
                level.getPlayer().holdItem("");
            }
        if (level.superior.currentLevel != 14 || (level.currentBubble <= 50 || level.currentBubble >= 52)) {
            //delete first item when inventory gets too big
            if (inventoryItems.size > inventorySlots) {
                //display message
                message = "Inventory is full!";
                messageTimer = 100;
                level.items.add(new Item(new Vector2(level.getPlayer().getPosition().x > level.catacombs.get(level.currentCatacomb).position.x + level.catacombs.get(level.currentCatacomb).width / 2f ? level.getPlayer().getPosition().x - 40 : level.getPlayer().getPosition().x + 40, level.catacombs.get(level.currentCatacomb).position.y + 30), level.viewportPosition, inventoryItems.get(0).itemType));
                level.items.get(level.items.size - 1).potion.full = inventoryItems.get(0).potion.full;
                inventoryItems.removeIndex(0);
            }
            //set item position's inside the inventory when they are collected
            for (Item item : inventoryItems) {
                if (!dragItem) {
                    item.position.set(new Vector2(position.x + (width / 15) + (inventoryItems.indexOf(item, true) * (width / inventorySlots)), (position.y - (height / 2)) + (level.getPlayer().getVelocity().y / 60)));
                }
                item.collected = true;
                item.render(renderer, level);
            }

            //draw collected diamonds: MAX DIAMONDS PER LEVEL IS 3.
            for (Diamond diamond : scoreDiamonds) {
                diamond.position.set(new Vector2(position.x + (level.viewport.getWorldWidth() / 1.25f) - (scoreDiamonds.indexOf(diamond, true) * diamond.diamondWidth * 1.2f), position.y + (level.viewport.getWorldHeight() / 1.3f)));
                diamond.render(renderer);
            }
            //remove a diamond if diamond count is over the MAX_DIAMONDS count of 3
            if (scoreDiamonds.size > Constants.MAX_DIAMONDS) {
                scoreDiamonds.removeIndex(0);
            }
            //render information of an item when a new item has been collected.
            if (newItem) {
                //box
                level.getPlayer().resetLegs();
                renderer.set(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(new Color(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, 0.8f));
                renderer.rect((level.getPlayer().getPosition().x - 86), level.getPlayer().getPosition().y + 50, 164, level.viewport.getWorldHeight() / 8f);
                renderer.setColor(new Color(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, 1f));
                renderer.rect((level.getPlayer().getPosition().x - 86) + 2, level.getPlayer().getPosition().y + 52, (level.viewport.getWorldHeight() / 8f), (level.viewport.getWorldHeight() / 8f) - 4);
                renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, newItemOpacity));
                renderer.triangle(level.getPlayer().getPosition().x - 2, level.getPlayer().getPosition().y + 51, level.getPlayer().getPosition().x + 2, level.getPlayer().getPosition().y + 51, level.getPlayer().getPosition().x, level.getPlayer().getPosition().y + 47);
                //new item
                newCollectedItem.position.set(new Vector2((level.getPlayer().getPosition().x - 86) + 6, level.getPlayer().getPosition().y + 60));
                newCollectedItem.collected = true;
                newCollectedItem.itemType = newItemType;
                newCollectedItem.render(renderer, level);
            }
        } else {
            //target button which player uses to shoot stun gun lasers
            renderer.set(ShapeRenderer.ShapeType.Filled);
            //outer hexagon
            renderer.setColor(Color.DARK_GRAY);
            renderer.ellipse(level.getPlayer().getPosition().x - Constants.HEAD_SIZE * 1.9f, level.getPlayer().getPosition().y - level.viewport.getWorldHeight() / 2f, 30, 30, 6);
            //red outer target circle
            renderer.setColor(level.getPlayer().energy < 19 ? new Color(0.3f, 0.3f, 0.3f, 1f) : Color.RED);
            renderer.ellipse(level.getPlayer().getPosition().x + 1 - (Constants.HEAD_SIZE * 1.25f), level.getPlayer().getPosition().y + 1 - (level.viewport.getWorldHeight() / 2.125f), 18, 18, 30);
            //inner gray circle
            renderer.setColor(Color.DARK_GRAY);
            renderer.ellipse(level.getPlayer().getPosition().x + 2.5f - (Constants.HEAD_SIZE * 1.25f), level.getPlayer().getPosition().y + 2.5f - (level.viewport.getWorldHeight() / 2.125f), 15, 15, 30);
            //target icon lines
            renderer.setColor(level.getPlayer().energy < 19 ? new Color(0.3f, 0.3f, 0.3f, 1f) : Color.RED);
            //bottom and top
            renderer.rectLine(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y - 2 - (level.viewport.getWorldHeight() / 2.125f), level.getPlayer().getPosition().x, level.getPlayer().getPosition().y + 6 - (level.viewport.getWorldHeight() / 2.125f), 2);
            renderer.rectLine(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y + 14 - (level.viewport.getWorldHeight() / 2.125f), level.getPlayer().getPosition().x, level.getPlayer().getPosition().y + 22 - (level.viewport.getWorldHeight() / 2.125f), 2);
            //left and right
            renderer.rectLine(level.getPlayer().getPosition().x - 12, level.getPlayer().getPosition().y + 10f - (level.viewport.getWorldHeight() / 2.125f), level.getPlayer().getPosition().x - 4, level.getPlayer().getPosition().y + 10f - (level.viewport.getWorldHeight() / 2.125f), 2);
            renderer.rectLine(level.getPlayer().getPosition().x + 4, level.getPlayer().getPosition().y + 10f - (level.viewport.getWorldHeight() / 2.125f), level.getPlayer().getPosition().x + 12, level.getPlayer().getPosition().y + 10f - (level.viewport.getWorldHeight() / 2.125f), 2);
        }
    }

    public void deleteCurrentItem () {
        if (selectedItem != -1) {
            inventoryItems.removeIndex(selectedItem);
            selectedItem = -1;
        }
    }

    public void renderPause (ShapeRenderer renderer) {
        //pause button
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(new Color(0.3f, 0.3f, 1.0f, 1));
        renderer.circle((level.cameraPosition.x - level.viewport.getWorldWidth() / 2) + 2, level.cameraPosition.y - level.viewport.getWorldHeight() / 2, 20, 6);
        renderer.setColor(Color.BLUE);
        renderer.rect((level.cameraPosition.x - level.viewport.getWorldWidth() / 2) + 3, (level.cameraPosition.y - level.viewport.getWorldHeight() / 2) + 3, 3, 10);
        renderer.rect((level.cameraPosition.x - level.viewport.getWorldWidth() / 2) + 8, (level.cameraPosition.y - level.viewport.getWorldHeight() / 2) + 3, 3, 10);
        //pause menu
        if (paused) {
            renderer.setColor(new Color(0.3f, 0.3f, 1.0f, 1));
            renderer.rect((level.getPlayer().getPosition().x - level.viewport.getWorldWidth() / 2.5f), level.getPlayer().getPosition().y - level.viewport.getWorldHeight() / 2.5f, level.viewport.getWorldWidth() / 1.3f, level.viewport.getWorldHeight() / 1.2f);
            //set pause menu button positions
            if (level.viewport.getWorldWidth() < 250) {
                resetButton.position = new Vector2(level.getPlayer().getPosition().x - 40, level.getPlayer().getPosition().y - 20);
                homeButton.position = new Vector2(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y - 20);
                playButton.position = new Vector2(level.getPlayer().getPosition().x - 30, level.getPlayer().getPosition().y - 55);
                speechButton.position = new Vector2(level.getPlayer().getPosition().x - 70, level.getPlayer().getPosition().y - 55);
                guideButton.position = new Vector2(level.getPlayer().getPosition().x - 70, level.getPlayer().getPosition().y - 55);
                mapButton.position = new Vector2(level.getPlayer().getPosition().x + 30, level.getPlayer().getPosition().y - 55);
            }  else {
                resetButton.position = new Vector2(level.getPlayer().getPosition().x - 80, level.getPlayer().getPosition().y - 55);
                homeButton.position = new Vector2(level.getPlayer().getPosition().x + 40, level.getPlayer().getPosition().y - 55);
                playButton.position = new Vector2(level.getPlayer().getPosition().x - 30, level.getPlayer().getPosition().y - 55);
                speechButton.position = new Vector2(level.getPlayer().getPosition().x - 60, level.getPlayer().getPosition().y - 20);
                guideButton.position = new Vector2(level.getPlayer().getPosition().x - 60, level.getPlayer().getPosition().y - 20);
                mapButton.position = new Vector2(level.getPlayer().getPosition().x + 20, level.getPlayer().getPosition().y - 20);
            }
            backButton.position = new Vector2(level.getPlayer().getPosition().x - 80, level.getPlayer().getPosition().y - 60);
            yesButton.position = new Vector2(level.getPlayer().getPosition().x + 5, level.getPlayer().getPosition().y - 50);
            noButton.position = new Vector2(level.getPlayer().getPosition().x - 45, level.getPlayer().getPosition().y - 50);
            if (stage.equals("Main")) {
                resetButton.render(renderer);
                homeButton.render(renderer);
                playButton.render(renderer);
                //if level is less than 5, show speech button, else, show the instruction button instead
                if (level.superior.currentLevel < 4) {
                    speechButton.render(renderer);
                } else {
                    guideButton.render(renderer);
                }
                mapButton.render(renderer);
            } else if (stage.equals("Map")) {
                //map of catacombs
                for(int i = 0; i < level.catacombs.size; i++) {
                    renderer.setColor(Color.BLUE);
                    if (level.catacombs.get(i).width == 200) {
                        renderer.ellipse(level.getPlayer().getPosition().x + ((level.catacombs.get(i).position.x + 15) / 16f), level.getPlayer().getPosition().y + ((level.catacombs.get(i).position.y) / 20f), 11, 9, 6);
                    } else {
                        renderer.ellipse(level.getPlayer().getPosition().x + ((level.catacombs.get(i).position.x + 15) / 16f), level.getPlayer().getPosition().y + ((level.catacombs.get(i).position.y) / 20f), 11, 9, 6);
                        renderer.ellipse(level.getPlayer().getPosition().x + ((level.catacombs.get(i).position.x + 15) / 16f) + 3.125f, level.getPlayer().getPosition().y + ((level.catacombs.get(i).position.y) / 20f), 11, 9, 6);
                        renderer.ellipse(level.getPlayer().getPosition().x + ((level.catacombs.get(i).position.x + 15) / 16f) + 6.25f, level.getPlayer().getPosition().y + ((level.catacombs.get(i).position.y) / 20f), 11, 9, 6);
                    }
                }
                //player symbol on map
                renderer.setColor(new Color(Color.BLUE.r / 3f, Color.BLUE.g / 3f, Color.BLUE.b / 3f, 1));
                renderer.circle(level.getPlayer().getPosition().x + (level.getPlayer().getPosition().x / 16f), level.getPlayer().getPosition().y + (level.getPlayer().getPosition().y / 20f), 1.5f + (playButton.playMove / 40f), 4);
                //back button
                backButton.render(renderer);
            } else if (stage.equals("Instructions")) {
                //back button
                backButton.render(renderer);
            } else if (stage.equals("ValidateReset") || stage.equals("ValidateSpeech") || stage.equals("ValidateHome")) {
                //verification stages have a "yes" and a "no" button
                yesButton.render(renderer);
                noButton.render(renderer);
            }
        }
    }

    public void renderText (SpriteBatch batch, BitmapFont font, BitmapFont font2, Viewport hudViewport) {
        if (paused) {
            font.setColor(Color.BLACK);
            font.draw(batch, "PAUSED", hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 150f), hudViewport.getWorldHeight() / 1.2f);
            font2.setColor(Color.WHITE);
            if (stage.equals("ValidateReset")) {
                font2.draw(batch, "Would you like to restart the level?", hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 200f), hudViewport.getWorldHeight() / 1.5f);
            } else if (stage.equals("ValidateHome")) {
                font2.draw(batch, "Would you like to exit to home menu?", hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 210f), hudViewport.getWorldHeight() / 1.5f);
            } else if (stage.equals("ValidateSpeech")) {
                font2.draw(batch, speechButton.speech ? " Would you like to play the\n level without instructions?" : "   Would you like to replay\n the level with instructions?", hudViewport.getWorldWidth() / 2f - ((Math.min(hudViewport.getWorldWidth(), hudViewport.getWorldHeight()) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 170f), hudViewport.getWorldHeight() / 1.5f);
            } else if (stage.equals("Instructions")) {
                font2.draw(batch, "You, the Player, are imprisoned in the Catacombs and are attempting to escape to the surface. To move around, tap on the area which you would like to walk to. To jump, tap twice in either upper corner. To unlock a door, tap on a key in your inventory and tap on the button of the door you want. To drink a potion, tap on the potion in the Player's hand.", hudViewport.getWorldWidth() / 4.5f , hudViewport.getWorldHeight() / 1.5f, hudViewport.getWorldWidth() / 1.7f, 5, true);
            }
        } else if (newItem) {
            font2.setColor(Color.GOLD);
            font2.draw(batch, newItemType.equals("invisibility") || newItemType.equals("ghost") || newItemType.equals("shock") ? "?????" : (newItemType.replaceFirst(newItemType.charAt(0)+"", (newItemType.charAt(0)+"").toUpperCase()) + ":"), (hudViewport.getWorldWidth() / 4.4f) + ((480 - hudViewport.getWorldHeight()) / 3f) + ((hudViewport.getWorldWidth() - 640) / 4f), hudViewport.getWorldHeight() / 1.1f);
            font2.setColor(Color.BLACK);
            font2.draw(batch, itemText(newItemType), (hudViewport.getWorldWidth() / 4.4f) + (18 + ((newItemType.equals("invisibility") || newItemType.equals("ghost") || newItemType.equals("shock") ? 5 : newItemType.length()) * 13f)) + ((480 - hudViewport.getWorldHeight()) / 5f) + ((hudViewport.getWorldWidth() - 640) / 4f), hudViewport.getWorldHeight() / 1.1f);
        }
        //update messages
        if (!message.equals("")) {
            messageTimer--;
        }
        if (messageTimer <= 0) {
            message = "";
            messageTimer = 100;
        }
        //draw messages
        font2.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b, messageTimer / 100f));
        font2.draw(batch, message, (hudViewport.getWorldWidth() / 2f) - (message.length() * 4f), hudViewport.getWorldHeight() * 0.75f);
    }
    //When player collects a new Item, this function shows a summary of the Item based on its name.
    private String itemText (String newItemType) {
        if (newItemType.equals("key")) {
            return "Used to lock and unlock doors.";
        } else if (newItemType.equals("dagger")) {
            return "A small weapon used for battle.";
        } else if (newItemType.equals("gold")) {
            return "This precious item is highly\nprized throughout the Catacombs.";
        } else if (newItemType.equals("phone")) {
            return "A small device carried by Guards.";
        } else if (newItemType.equals("diamond")) {
            return "This jewel is used as currency.";
        } else if (newItemType.equals("stungun")) {
            return "A blaster capable of knocking\nout even the best of Guards.";
        } else if (newItemType.equals("sapphire")) {
            return "This blue gem is sometimes\nfound in the Catacombs.";
        } else if (newItemType.equals("ruby")) {
            return "This red gem is sometimes\nfound inside the Catacombs.";
        } else if (newItemType.equals("emerald")) {
            return "This green gem is sometimes\nfound in the Catacombs.";
        } else if (newItemType.equals("invisibility")) {
            return "A strange bubbling potion.";
        } else if (newItemType.equals("ghost")) {
            return "A bubbling potion that\neerily glows white.";
        } else if (newItemType.equals("shock")) {
            return "A potion which sizzles\nwith electric sparks.";
        } else if (newItemType.equals("doubleKey")) {
            return "Can only be used to\nunlock double-locks.";
        } else if (newItemType.equals("shield")) {
            return "This item bolsters your defence\nagainst various obstacles.";
        } else if (newItemType.equals("bomb")) {
            return "This explosive device can\nbe used to destroy walls.";
        } else if (newItemType.equals("fire")) {
            return "Fire can be used to light torches.";
        } else if (newItemType.equals("disguise")) {
            return "A disguise can be\nused to fool Guards.";
        } else if (newItemType.equals("spear")) {
            return "The weapon carried\nby the final Boss.";
        } else {
            return "Unknown item...";
        }
    }

}
