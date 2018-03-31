package com.efe.gamedev.catacombs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.efe.gamedev.catacombs.entities.Catacomb;
import com.efe.gamedev.catacombs.entities.Chest;
import com.efe.gamedev.catacombs.entities.Exit;
import com.efe.gamedev.catacombs.entities.Guard;
import com.efe.gamedev.catacombs.entities.Inventory;
import com.efe.gamedev.catacombs.entities.Item;
import com.efe.gamedev.catacombs.entities.Puzzle;
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
 */

public class Level extends InputAdapter {

    private static final String TAG = Level.class.getName();

    //all levels
    public Levels superior;

    //viewport
    public Viewport viewport;
    //hudViewport
    public Viewport hudViewport;
    //viewport touch position
    public Vector2 viewportPosition;
    //seperate touch position for buttons and things
    public Vector2 touchPosition;
    public Vector2 lookPosition;
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
    public boolean continueBubbles = true;
    public boolean show = true;
    //Catacomb Chambers
    public Array<Catacomb> catacombs;
    public int currentCatacomb;
    //items
    public DelayedRemovalArray<Item> items;
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

    //torch light
    public long startTime;
    public float torchLight;
    private static final float TORCH_MOVEMENT_DISTANCE = 5;
    private static final float TORCH_PERIOD = 2f;
    public boolean levelStarted = false;

    public Level (Viewport hudViewport, Levels superior) {
        this.superior = superior;
        //viewport parameters
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        viewportPosition = new Vector2();
        touchPosition = new Vector2();
        lookPosition = new Vector2();
        touchLocked = true;

        this.hudViewport = hudViewport;

        victory = false;
        defeat = false;
        verdict = new levelVerdict(this);

        //make inventory
        inventory = new Inventory(this);

        targetBox = new Target();
        //get player
        player = new Player(new Vector2(100, 150), viewportPosition, this);
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        //save touch position when player touches screen
        if (!touchLocked && player.jumpState == Enums.JumpState.GROUNDED) {
            viewportPosition = viewport.unproject(new Vector2(screenX, screenY));
        }
            touchPosition = viewport.unproject(new Vector2(screenX, screenY));
        //give objects and player the touch position when player touches screen
        player.viewportPosition = viewportPosition;
        for (Item item : items) {
            item.viewportPosition = viewportPosition;
        }

        //change speechBubble
        if (touchPosition.dst(new Vector2((player.getPosition().x - viewport.getWorldWidth() / 2) + 2, player.getPosition().y - viewport.getWorldHeight() / 2)) > 18 && !speechBubbles.get(currentBubble).textLoaded) {
            speechBubbles.get(currentBubble).typeSpeed = 2;
        }
        if (speechBubbles.get(currentBubble).Options == 0 && continueBubbles && currentBubble < speechBubbles.size - 1 && speechBubbles.get(currentBubble).textLoaded && !inventory.paused && touchPosition.dst(new Vector2((player.getPosition().x - viewport.getWorldWidth() / 2) + 2, player.getPosition().y - viewport.getWorldHeight() / 2)) > 18) {
            speechBubbles.get(currentBubble).typeSpeed = 4;
            currentBubble += 1;
        }
        //check to see if you want the player to jump when you tap on the screen.
        if (!touchLocked && !inventory.paused) {player.tryJumping();}
        if (!inventory.paused) {speechBubbles.get(currentBubble).trySelecting();}

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {

        lookPosition = viewport.unproject(new Vector2(screenX, screenY));

        return super.touchUp(screenX, screenY, pointer, button);
    }

    public void update (float delta) {
        if (!victory && !defeat && !inventory.paused) {
            player.update(delta);
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
                item.shadowOffset.set(new Vector2((player.getPosition().x - item.position.x) / -10, -2));
                //if collected, let score or inventory pick it up
                if (item.collected) {
                    if (item.itemType.equals("diamond")) {
                        inventory.scoreDiamonds.add(new Diamond(new Vector2()));
                    } else {
                        inventory.inventoryItems.add(new Item(new Vector2(item.position.x, item.position.y), viewportPosition, item.itemType));
                    }
                    items.removeIndex(i);
                }
            }
            for (Catacomb catacomb : catacombs) {
                catacomb.update(delta);
            }
            inventory.update();
            //update torch light
            if (torchLight < 60f && !levelStarted) {
                torchLight += 0.5f;
                startTime = TimeUtils.nanoTime();
            } else {
                levelStarted = true;
                updateTorch();
            }
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

    public void render (ShapeRenderer renderer) {
        //apply viewport
        viewport.apply();
        //draw player, catacombs, others
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        //catacombs
        for (Catacomb catacomb : catacombs) {
            catacomb.render(renderer);
        }
        renderer.end();

        //draw stuff with special blends to enable color alpha changes
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
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
        //player
        player.render(renderer);
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
        //items
        for (Item item : items) {
            item.render(renderer);
        }
        //chests
        for (Chest chest : chests) {
            chest.render(renderer);
        }
        //render exit
        if (exitDoor.unlocked) {
            exitDoor.renderDoor(renderer);
        }
        //render exit button
        exitDoor.renderButton(renderer);
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
            rectBorder(renderer, new Vector2(player.getPosition().x - (i * 2) - Constants.HEAD_SIZE, player.getPosition().y - (i * 2) - Constants.HEAD_SIZE), 20 + (i * 4), 10 + (i * 4), i / (torchLight));
        }
        if (targetBox.target && !inventory.paused) {
            targetBox.render(renderer);
        }
        if (!victory && !defeat) {
            //Inventory for player
            inventory.render(renderer);
            //pause
            inventory.renderPause(renderer);
            //speechBubbles
            if (show && !inventory.paused) {
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
    private void rectBorder (ShapeRenderer renderer, Vector2 shapePosition, float shapeWidth, float shapeHeight, float colorIncrement) {
        renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, colorIncrement));
        renderer.rectLine(shapePosition.x, shapePosition.y, shapePosition.x + shapeWidth, shapePosition.y, 4);
        renderer.rectLine(shapePosition.x + shapeWidth, shapePosition.y, shapePosition.x + shapeWidth, shapePosition.y + shapeHeight, 4);
        renderer.rectLine(shapePosition.x + shapeWidth, shapePosition.y + shapeHeight, shapePosition.x, shapePosition.y + shapeHeight, 4);
        renderer.rectLine(shapePosition.x, shapePosition.y + shapeHeight, shapePosition.x, shapePosition.y, 4);
    }

    public void renderText (SpriteBatch batch, BitmapFont font, BitmapFont font2) {
        if (!victory && !defeat) {
            //speechBubble text
            if (show && !inventory.paused) {
                speechBubbles.get(currentBubble).renderText(batch, font, hudViewport);
            }
            //TODO: Make it so the player can make a custom name for the character
            if (speechBubbles.get(currentBubble).target.equals("Player:")) {
                if (speechBubbles.get(currentBubble).textLoaded) {
                    player.mouthState = Enums.MouthState.NORMAL;
                } else {
                    if (inventory.speechButton.speech) {
                        player.mouthState = Enums.MouthState.TALKING;
                    } else {
                        player.mouthState = Enums.MouthState.NORMAL;
                    }
                }
            }
        }
        if (inventory.paused) {
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
