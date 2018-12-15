package com.efe.gamedev.catacombs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.efe.gamedev.catacombs.entities.Boss;
import com.efe.gamedev.catacombs.entities.Catacomb;
import com.efe.gamedev.catacombs.entities.Chest;
import com.efe.gamedev.catacombs.entities.Door;
import com.efe.gamedev.catacombs.entities.Electricity;
import com.efe.gamedev.catacombs.entities.Exit;
import com.efe.gamedev.catacombs.entities.Guard;
import com.efe.gamedev.catacombs.entities.Inventory;
import com.efe.gamedev.catacombs.entities.Item;
import com.efe.gamedev.catacombs.entities.Lever;
import com.efe.gamedev.catacombs.entities.Prisoner;
import com.efe.gamedev.catacombs.entities.Puzzle;
import com.efe.gamedev.catacombs.entities.Sign;
import com.efe.gamedev.catacombs.entities.Target;
import com.efe.gamedev.catacombs.entities.Word;
import com.efe.gamedev.catacombs.entities.levelVerdict;
import com.efe.gamedev.catacombs.items.Diamond;
import com.efe.gamedev.catacombs.entities.Player;
import com.efe.gamedev.catacombs.entities.SpeechBubble;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 11/17/2017.
 * A Level is a big function that has a TouchDown and TouchUp function which use an InputAdapter to tell if someone is touching the screen or not and sends the results to various Java classes in the game, such as Player, Item, ect.
 * A Level has an Update function, Render function, RenderHUD function (pause menu, torch light background, alarm effect), RenderText (Words on screen), and RenderWords (renders words for a type of puzzle called Words). These functions render words and shapes and update variables for functions in the game.
 */

public class Level extends InputAdapter {

    private static final String TAG = Level.class.getName();

    //gameplay screen
    public GameplayScreen gameplayScreen;
    //all levels
    public Levels superior;
    //viewport
    public Viewport viewport;
    //hudViewport
    private Viewport hudViewport;
    //viewport touch position
    public Vector2 viewportPosition;
    //seperate touch position for buttons and things
    public Vector2 touchPosition;
    public Vector2 lookPosition;
    public boolean pressDown;
    public boolean pressUp;
    //allows player to touch stuff or not
    public boolean touchLocked;

    //has player beaten the level or lost it
    public boolean victory;
    public boolean defeat;
    public levelVerdict verdict;

    //player
    public Player player;
    //Exit door
    public Exit exitDoor;
    //speechBubbles
    public Array<SpeechBubble> speechBubbles;
    public int currentBubble;
    //save last bubble seen
    public int lastSeenBubble;
    //variables controlling sight of speechBubbles
    public boolean continueBubbles = true;
    public boolean show = true;
    //Catacomb Chambers
    public Array<Catacomb> catacombs;
    public int currentCatacomb;
    //items
    public DelayedRemovalArray<Item> items;
    //A boolean array containing data that stores which items have been collected. True means that the player has collected that item before and false means that the player has never seen the item before.
    public boolean[] collectedItems = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    public Array<String> collectedItemTypes;
    //player's Inventory
    public Inventory inventory;
    //Chests
    public Array<Chest> chests;
    //Words
    public Array<Word> words;
    //Puzzles
    public Array<Puzzle> puzzles;
    //Target box
    public Target targetBox;
    //guards
    public Array<Guard> guards;
    //Levers
    public Array<Lever> levers;
    //Doors
    public Array<Door> doors;
    public boolean usedDoor;
    //Signs
    public Array<Sign> signs;
    //Electrical wire puzzles
    public Array<Electricity> wires;
    //prisoners
    public Array<Prisoner> prisoners;
    //bosses
    public Array<Boss> bosses;

    //torch light
    public long startTime;
    public float torchLight;
    private static final float TORCH_MOVEMENT_DISTANCE = 5;
    private static final float TORCH_PERIOD = 2f;
    public boolean levelStarted = false;
    public boolean torchFade;
    public boolean torchUp;
    //alarm
    public boolean alarm;
    private float alarmLight;
    public boolean shake;
    //camera move
    public Vector2 cameraPosition;

    public Level (Viewport hudViewport, Levels superior, GameplayScreen gameplayScreen) {
        this.superior = superior;
        this.gameplayScreen = gameplayScreen;
        //viewport parameters
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        viewportPosition = new Vector2();
        touchPosition = new Vector2();
        lookPosition = new Vector2();
        pressDown = false;
        pressUp = false;
        touchLocked = true;
        //initiate some stuff
        this.hudViewport = hudViewport;
        alarm = false;
        shake = false;
        cameraPosition = new Vector2();
        //all items
        collectedItemTypes = new Array<String>();
        collectedItemTypes.add("key"); collectedItemTypes.add("dagger"); collectedItemTypes.add("gold");
        collectedItemTypes.add("phone"); collectedItemTypes.add("diamond"); collectedItemTypes.add("stungun");
        collectedItemTypes.add("sapphire"); collectedItemTypes.add("ruby"); collectedItemTypes.add("emerald");
        collectedItemTypes.add("invisibility"); collectedItemTypes.add("ghost"); collectedItemTypes.add("doubleKey");
        collectedItemTypes.add("shield"); collectedItemTypes.add("bomb"); collectedItemTypes.add("fire");
        collectedItemTypes.add("shock"); collectedItemTypes.add("disguise"); collectedItemTypes.add("spear");
        if (gameplayScreen.game.getFurthestLevel() > superior.currentLevel) {
            lastSeenBubble = 60;
        } else {
            lastSeenBubble = 0;
        }

        victory = false;
        defeat = false;
        verdict = new levelVerdict(this);

        //make inventory
        inventory = new Inventory(this);

        targetBox = new Target();
        //get player
        player = new Player(new Vector2(100, 150), viewportPosition, this);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        //save touch position when player touches screen
        if (!touchLocked && player.jumpState == Enums.JumpState.GROUNDED) {
            viewportPosition = viewport.unproject(new Vector2(screenX, screenY));
        }
        touchPosition = viewport.unproject(new Vector2(screenX, screenY));
        pressDown = true;
        pressUp = false;
        //give objects and player the touch position when player touches screen
        player.viewportPosition = viewportPosition;
        for (Item item : items) {
            item.viewportPosition = viewportPosition;
        }

        //change speechBubble
        //make sure you are not touching on the pause button
        if (touchPosition.dst(new Vector2((player.getPosition().x - viewport.getWorldWidth() / 2) + 2, player.getPosition().y - viewport.getWorldHeight() / 2)) > 18 && !speechBubbles.get(currentBubble).textLoaded) {
            speechBubbles.get(currentBubble).typeSpeed = 2;
        }
        if (speechBubbles.get(currentBubble).Options == 0 && continueBubbles && currentBubble < speechBubbles.size - 1 && speechBubbles.get(currentBubble).textLoaded && !inventory.paused && touchPosition.dst(new Vector2((player.getPosition().x - viewport.getWorldWidth() / 2) + 2, player.getPosition().y - viewport.getWorldHeight() / 2)) > 18 && (speechBubbles.get(currentBubble).speechBubbleSkip == -1 || touchPosition.dst(new Vector2(speechBubbles.get(currentBubble).position.x + speechBubbles.get(currentBubble).width + 59.5f + speechBubbles.get(currentBubble).targetOffset, (speechBubbles.get(currentBubble).height == 20 ? ((speechBubbles.get(currentBubble).position.y + 7.5f) + 10) : (speechBubbles.get(currentBubble).position.y + 7.5f)))) > 30)) {
            speechBubbles.get(currentBubble).typeSpeed = 3;
            currentBubble += 1;
            //only update lastSeenBubble if currentBubble is more or equal to it.
            if (currentBubble >= lastSeenBubble) {
                lastSeenBubble = currentBubble;
            }
        }
        //check to see if you want the player to jump when you tap on the screen.
        if (!touchLocked && !inventory.paused) {
            player.tryJumping();
        }
        if (!inventory.paused) {
            speechBubbles.get(currentBubble).trySelecting();
        }

        //fight with guard
        for (int i = 0; i < guards.size; i++) {
            if (guards.get(i).guardState == Enums.GuardState.FIGHT && touchPosition.x > (guards.get(i).position.x - Constants.HEAD_SIZE) && touchPosition.x < (guards.get(i).position.x - Constants.HEAD_SIZE) + (Constants.PLAYER_WIDTH * 2f) && touchPosition.y > (guards.get(i).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f) && touchPosition.y < (guards.get(i).position.y + Constants.HEAD_SIZE) && guards.get(i).guardEnergy > 0) {
                guards.get(i).guardEnergy -= 1;
            }
        }
        //duck lasers
        if (superior.currentLevel == 5 && currentBubble == 35) {
            if (touchPosition.x > this.getPlayer().getPosition().x - Constants.HEAD_SIZE && touchPosition.x < (this.getPlayer().getPosition().x - Constants.HEAD_SIZE) + Constants.PLAYER_WIDTH * 2f && touchPosition.y > this.getPlayer().getPosition().y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f && touchPosition.y < this.getPlayer().getPosition().y + Constants.HEAD_SIZE && player.energy > 0 && !player.recoverE) {
                player.duck = true;
            }
        }
        //shoot lasers back at guard
        if (superior.currentLevel == 8 && currentBubble == 9 && player.heldItem.itemType.equals("stungun") && player.energy >= 20 && !defeat && speechBubbles.get(currentBubble).textLoaded) {
            player.heldItem.stungun.shoot = true;
            player.energy = 0;
        }
        //shoot lasers at boss during boss battle
        if (touchPosition.dst(new Vector2(player.getPosition().x - Constants.HEAD_SIZE * 1.9f, player.getPosition().y - viewport.getWorldHeight() / 2f)) < 30 && superior.currentLevel == 14 && currentBubble > 50 && player.heldItem.itemType.equals("stungun") && player.energy >= 20 && !defeat && speechBubbles.get(currentBubble).textLoaded) {
            player.heldItem.stungun.shoot = true;
            player.energy = 0;
        }

        //returns the position at which your finger touches device screen
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //make player duck in boss fight
        if (superior.currentLevel == 14 && currentBubble > 17 && currentBubble < 20) {
            player.duck = (viewport.unproject(new Vector2(screenX, screenY)).y < touchPosition.y - 10) && (player.position.y < catacombs.get(currentCatacomb).position.y + catacombs.get(currentCatacomb).wallThickness + 64);
            if ((player.position.y < catacombs.get(currentCatacomb).position.y + catacombs.get(currentCatacomb).wallThickness + 64) && !player.duck && ((viewport.unproject(new Vector2(screenX, screenY)).y > touchPosition.y + 10))) {
                player.setVelocity(new Vector2(0, 200f));
            }
        }
        //check if player is dragging bomb
        if (inventory.touchItem("bomb") != -1 && !inventory.newItem) {
            inventory.dragItem = true;
        } else {
            inventory.dragItem = false;
        }
        //set bomb to player's touch position when bomb is being dragged
        if (inventory.dragItem) {
            inventory.selectedItem = inventory.touchItem("bomb");
            inventory.inventoryItems.get(inventory.touchItem("bomb")).position = viewport.unproject(new Vector2(screenX, screenY));
        }
        //keeps returning the position at which your finger is touching the device screen when you are dragging your finger
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {

        lookPosition = viewport.unproject(new Vector2(screenX, screenY));
        //boss fight stop dragging
        if (superior.currentLevel == 14) {
            player.duck = false;
        }
        //stop dragging
        if (inventory.dragItem) {
            //check if item is out of inventory and if item is in currentCatacomb's bounds
            Catacomb catacomb = catacombs.get(currentCatacomb);
            if ((inventory.inventoryItems.get(inventory.touchItem("bomb")).position.x < inventory.position.x || inventory.inventoryItems.get(inventory.touchItem("bomb")).position.x > inventory.position.x + inventory.width || inventory.inventoryItems.get(inventory.touchItem("bomb")).position.y > inventory.position.y + inventory.height) && inventory.inventoryItems.get(inventory.touchItem("bomb")).position.x > catacomb.position.x + 50 && inventory.inventoryItems.get(inventory.touchItem("bomb")).position.x < (catacomb.position.x + catacomb.width) - 50 && inventory.inventoryItems.get(inventory.touchItem("bomb")).position.y > catacomb.position.y + 35 && inventory.inventoryItems.get(inventory.touchItem("bomb")).position.y < catacomb.position.y + catacomb.height - 35) {
                items.add(new Item(new Vector2(lookPosition), viewportPosition, "bomb"));
                items.get(items.size - 1).bomb.triggered = true;
                items.get(items.size - 1).bomb.currentCatacomb = currentCatacomb;
                inventory.deleteCurrentItem();
            }
            inventory.dragItem = false;
        }
        //stop pressing
        pressDown = false;
        pressUp = true;
        //makes it so that in level 6, at a certain spot in the level, when you stop touching the player, it stops ducking lasers.
        if (superior.currentLevel == 5 && currentBubble == 35) {
            player.duck = false;
        }
        //returns the position at which your finger stops touching device screen
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            inventory.paused = true;
        }
        return false;
    }

    public void update (float delta) {
        //update most things when you have not won or lost and the game is not paused
        if (!victory && !defeat && !inventory.paused) {
            //if you are not looking at info about a new item, update stuff
            if (!inventory.newItem) {
                //player, guards, exit, targetBox (tutorial stuff), speechbubbles, chests, ect...
                player.update(delta);
                for (Guard guard : guards) {
                    guard.update(delta);
                }
                for (Prisoner prisoner : prisoners) {
                    prisoner.update(delta);
                }
                for (Boss boss : bosses) {
                    boss.update(delta);
                }
                for (Lever lever : levers) {
                    lever.update();
                }
                exitDoor.update(delta);
                if (targetBox.target) {
                    targetBox.update();
                }
                if (!targetBox.target) {
                    targetBox.startTime = TimeUtils.nanoTime();
                }
                if (show) {
                    speechBubbles.get(currentBubble).update();
                }
                for (Chest chest : chests) {
                    chest.update();
                }
                //set some stuff and check if speechBubbles are triggered
                if (speechBubbles.get(currentBubble).triggered) {
                    speechBubbles.get(currentBubble).function4.run();
                }

                for (int i = 0; i < items.size; i++) {
                    Item item = items.get(i);
                    //set item shadow positions
                    item.shadowOffset.set(new Vector2((player.getPosition().x - item.position.x) / -10, -2));
                    //if collected, let score or inventory pick it up
                    if (item.collected) {
                        gameplayScreen.sound3.play();
                        //if diamond, add to ScoreDiamonds (records how many diamonds you have collected in the level), else, add to inventory items
                        if (item.itemType.equals("diamond")) {
                            inventory.scoreDiamonds.add(new Diamond(new Vector2()));
                        } else {
                            inventory.inventoryItems.add(new Item(new Vector2(item.position.x, item.position.y), viewportPosition, item.itemType));
                            inventory.inventoryItems.get(inventory.inventoryItems.size - 1).potion.full = item.potion.full;
                        }
                        items.removeIndex(i);
                    }
                }
                for (Catacomb catacomb : catacombs) {
                    catacomb.update(delta);
                }
            }
            inventory.update();
            //update torch light
            if (!torchFade) {
                if (torchLight < 60f && !levelStarted) {
                    torchLight += 0.5f;
                    startTime = TimeUtils.nanoTime();
                } else {
                    levelStarted = true;
                    updateTorch();
                }
            } else {
                //used for opening doors
                //fade torchlight
                if (torchLight > 0f && !torchUp) {
                    torchLight -= 1f;
                }
                //when torchlight is very low, make it become larger again and move player to the location of the exitDoorIndex.
                if (torchLight <= 0f && !torchUp) {
                    torchUp = true;
                }
                //move torchlight up again
                if (torchLight <= 60f && torchUp) {
                    torchLight += 1f;
                }
                //reset torchlight
                if (torchLight > 59 && torchUp) {
                    torchFade = false;
                    torchUp = false;
                }
            }
        }
        //update alarm light
        if (alarm) {
            updateAlarm();
        }
        inventory.updatePause();
        if (victory || defeat) {
            verdict.update(delta);
        }
    }

    ///make torch light update
    private void updateTorch () {
        //Figure out how long it's been since the animation started using TimeUtils.nanoTime()
        long elapsedNanos = TimeUtils.nanoTime() - startTime;
        //Use MathUtils.nanoToSec to figure out how many seconds the animation has been running
        float elapsedSeconds = MathUtils.nanoToSec * elapsedNanos;
        //Figure out how many cycles have elapsed since the animation started running
        float cycles = elapsedSeconds / TORCH_PERIOD;
        //Figure out where in the cycle we are
        float cyclePosition = cycles % 1;
        //make torch light flicker
        torchLight = 60f + TORCH_MOVEMENT_DISTANCE * MathUtils.sin(MathUtils.PI2 * cyclePosition);
    }
    //very similar function for alarm
    private void updateAlarm () {
        //one line version of the previous function
        alarmLight = 150f + 50 * MathUtils.sin(MathUtils.PI2 * (((MathUtils.nanoToSec * (TimeUtils.nanoTime() - startTime)) / 1f) % 1));
    }

    public void render (ShapeRenderer renderer) {
        //apply viewport
        viewport.apply();

        //draw stuff with special blends to enable color alpha changes
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        //catacombs
        for (Catacomb catacomb : catacombs) {
            catacomb.render(renderer);
        }
        //render exit
        exitDoor.render(renderer);
        if (!exitDoor.unlocked) {
            exitDoor.renderDoor(renderer);
        }
        //puzzles
        for (Puzzle puzzle : puzzles) {
            puzzle.render(renderer);
            puzzle.renderHint(renderer);
        }
        //Doors
        for (Door door : doors) {
            door.render(renderer);
        }
        //Signs
        for (Sign sign : signs) {
            sign.render(renderer);
        }
        //Electricity
        for (Electricity wire : wires) {
            wire.render(renderer, this);
        }
        //prisoners
        for (Prisoner prisoner : prisoners) {
            prisoner.render(renderer);
        }
        //player
        player.render(renderer);
        //boss
        for (Boss boss : bosses) {
            boss.render(renderer);
        }
        //guards
        for (Guard guard : guards) {
            guard.render(renderer);
        }
        //render catacomb walls and buttons
        for (Catacomb catacomb : catacombs) {
            catacomb.renderWalls(renderer);
        }
        for (Catacomb catacomb : catacombs) {
            catacomb.renderButton(renderer);
        }
        //chests
        for (Chest chest : chests) {
            chest.render(renderer);
        }
        //Levers
        for (Lever lever : levers) {
            lever.render(renderer);
        }
        //render exit
        if (exitDoor.unlocked) {
            exitDoor.renderDoor(renderer);
        }
        //render exit button
        exitDoor.renderButton(renderer);
        //items
        for (Item item : items) {
            item.render(renderer, this);
        }
        //red covering over catacombs
        for (Catacomb catacomb : catacombs) {
            catacomb.renderRedCatacomb(renderer);
        }
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void renderHUD (ShapeRenderer renderer) {
        //apply viewport
        viewport.apply();
        //draw speech bubbles with special blends to enable color alpha changes
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        //torch light
        renderer.set(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < viewport.getWorldWidth() / 4f; i+=1) {
            renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, i / (torchLight)));
            rectBorder(renderer, new Vector2(cameraPosition.x - (i * 2) - Constants.HEAD_SIZE, cameraPosition.y - (i * 2) - Constants.HEAD_SIZE), 20 + (i * 4), 10 + (i * 4));
        }

        //alarm
        if (alarm) {
            for (int i = 47; i < viewport.getWorldWidth() / 4f; i+=1) {
                renderer.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b, (i / alarmLight) - (30 / i)));
                rectBorder(renderer, new Vector2((player.getPosition().x - (i * 2) - Constants.HEAD_SIZE - 2) - ((viewport.getWorldWidth() - 213) * 0.5f), (player.getPosition().y - (i * 2) - Constants.HEAD_SIZE) + 28), (viewport.getWorldWidth() - 193) + (i * 4), -40 + (i * 4));
            }
        }
        //target boxes
        if (targetBox.target && !inventory.paused) {
            targetBox.render(renderer);
        }
        //boss Spike Pillar warnings
        for (Boss boss : bosses) {
            boss.renderSpikeWarning(renderer);
        }
        if (!victory && !defeat) {
            //Inventory for player
            inventory.render(renderer);
            //pause
            inventory.renderPause(renderer);
            //speechBubbles
            if (show && !inventory.paused && !inventory.newItem) {
                speechBubbles.get(currentBubble).renderBubble(renderer);
            }
        }
        if (victory || defeat) {
            verdict.renderOutline(renderer);
        }
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    //draws an unfilled rectangle but with a thick translucent border
    private void rectBorder (ShapeRenderer renderer, Vector2 shapePosition, float shapeWidth, float shapeHeight) {
        renderer.rectLine(shapePosition.x, shapePosition.y, shapePosition.x + shapeWidth, shapePosition.y, 4);
        renderer.rectLine(shapePosition.x + shapeWidth, shapePosition.y, shapePosition.x + shapeWidth, shapePosition.y + shapeHeight, 4);
        renderer.rectLine(shapePosition.x + shapeWidth, shapePosition.y + shapeHeight, shapePosition.x, shapePosition.y + shapeHeight, 4);
        renderer.rectLine(shapePosition.x, shapePosition.y + shapeHeight, shapePosition.x, shapePosition.y, 4);
    }

    public void renderText (SpriteBatch batch, BitmapFont font, BitmapFont font2) {
        if (!victory && !defeat) {
            //speechBubble text
            if (show && !inventory.paused && !inventory.newItem) {
                speechBubbles.get(currentBubble).renderText(batch, font, hudViewport);
            }
            if (speechBubbles.get(currentBubble).target.equals("Player:")) {
                if (speechBubbles.get(currentBubble).textLoaded) {
                    player.mouthState = Enums.MouthState.NORMAL;
                } else {
                    if (inventory.speechButton.speech && show && !inventory.newItem) {
                        player.mouthState = Enums.MouthState.TALKING;
                    } else {
                        player.mouthState = Enums.MouthState.NORMAL;
                    }
                }
            }
            //guards' talking states
            for (int i = 0; i < guards.size; i++) {
                if (show && speechBubbles.get(currentBubble).target.equals("Guard " + (i+1) + ":")) {
                    if (speechBubbles.get(currentBubble).textLoaded) {
                        guards.get(i).mouthState = Enums.MouthState.NORMAL;
                    } else {
                        guards.get(i).mouthState = Enums.MouthState.TALKING;
                    }
                }
            }
            //prisoners' talking states
            if (show && (speechBubbles.get(currentBubble).target.equals("Prisoner 48H:") || speechBubbles.get(currentBubble).target.equals("Prisoner 12E:"))) {
                if (speechBubbles.get(currentBubble).textLoaded) {
                    prisoners.get(0).mouthState = Enums.MouthState.NORMAL;
                } else {
                    prisoners.get(0).mouthState = Enums.MouthState.TALKING;
                }
            }
            //boss's talking states
            if (show && (speechBubbles.get(currentBubble).target.equals("Boss:"))) {
                if (speechBubbles.get(currentBubble).textLoaded) {
                    bosses.get(0).mouthState = Enums.MouthState.NORMAL;
                } else {
                    bosses.get(0).mouthState = Enums.MouthState.TALKING;
                }
            }
        }
        if (inventory.paused || inventory.newItem || !inventory.message.equals("")) {
            inventory.renderText(batch, font2, font, hudViewport);
        }
        if (victory || defeat) {
            verdict.renderText(batch, font, font2, hudViewport);
        }
    }

    public void renderWords (SpriteBatch batch, BitmapFont font) {
        //words
        if (!inventory.paused) {
            for (Word word : words) {
                word.renderText(batch, font);
            }
        }
    }

    public Player getPlayer () {
        return player;
    }

    public DelayedRemovalArray<Item> getItems () {
        return items;
    };

    public Array<SpeechBubble> getSpeechBubbles () { return speechBubbles; }

}
