package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.MenuScreen;

/**
 * Created by coder on 12/29/2017.
 * This is a rectangle button that is used in pause menus, it is different than HexagonButtons
 */

public class Button {

    public Vector2 position;
    private String type;
    private float width;
    private float height;
    public Color color;
    private Runnable pressFunction;
    private Level level;
    private MenuScreen menu;
    //move icons
    float arrowMove;
    float doorMove;
    public boolean speech;
    //play icon
    private long startTime;
    float playMove;
    private static final float PLAY_MOVEMENT_DISTANCE = 8;
    private static final float PLAY_PERIOD = 1.5f;
    //decide if button is shown or not
    boolean shown;


    public Button (Vector2 position, String type, float width, float height, Color color, final Runnable pressFunction, Level level, MenuScreen menu) {
        this.position = position;
        this.type = type;
        this.width = width;
        this.height = height;
        this.color = color;
        this.pressFunction = pressFunction;
        this.level = level;
        this.menu = menu;
        arrowMove = -200;
        doorMove = 0;
        speech = true;
        playMove = 0;
        startTime = TimeUtils.nanoTime();
        shown = true;
    }

    public void update () {
        //animate icons
        if(type.equals("Reset") && arrowMove < 0) {
            arrowMove += 8;
        }
        if (type.equals("Home") && doorMove < width / 6f) {
            doorMove ++;
        }
        if (type.equals("Play")) {
            movePlay();
        }
    }

    private void movePlay () {
        //Figure out how long it's been since the animation started using TimeUtils.nanoTime()
        long elapsedNanos = TimeUtils.nanoTime() - startTime;
        //Use MathUtils.nanoToSec to figure out how many seconds the animation has been running
        float elapsedSeconds = MathUtils.nanoToSec * elapsedNanos;
        //Figure out how many cycles have elapsed since the animation started running
        float cycles = elapsedSeconds / PLAY_PERIOD;
        //Figure out where in the cycle we are
        float cyclePosition = cycles % 1;
        //move play icon in a reciprocating motion
        playMove = PLAY_MOVEMENT_DISTANCE * MathUtils.sin(MathUtils.PI2 * cyclePosition);
    }

    public void render (ShapeRenderer renderer) {
        //set speech variable to be false if someone is speaking
        if (level != null && level.show) {
            speech = true;
        }
        //render button if shown is true
        if (shown) {
            //set colors for button icons
            Color darkColor;
            Color lightColor;
            if (level != null && menu == null) {
                //run function when button is being touched
                if ((width < 30 || level.pressUp) && pressButton(level.lookPosition) && pressButton(level.touchPosition)) {
                    pressFunction.run();
                    //play button sound
                    if (level.gameplayScreen.game.getSoundEffectsOn()) {
                        if (type.equals("Play")) {
                            level.gameplayScreen.sound2.play();
                        } else {
                            level.gameplayScreen.sound1.play();
                        }
                    }
                    level.lookPosition = new Vector2();
                }
                if (pressButton(level.touchPosition) && level.pressDown) {
                    darkColor = new Color(color.r / 2, color.g / 2, color.b / 2, 1);
                    lightColor = new Color(color.r / 5, color.g / 5, color.b / 5, 1);
                } else {
                    darkColor = new Color(color.r / 5, color.g / 5, color.b / 5, 1);
                    lightColor = new Color(color.r / 2, color.g / 2, color.b / 2, 1);
                }
            } else {
                //run function when button is being touched
                if ((width < 30 || !menu.pressDown) && pressButton(menu.tapPosition) && pressButton(menu.releasePosition)) {
                    pressFunction.run();
                    //play button sound
                    if (menu.game.getSoundEffectsOn()) {
                        menu.sound5.play();
                    }
                    menu.tapPosition = new Vector2();
                    menu.releasePosition = new Vector2();
                }
                if (pressButton(menu.tapPosition) && menu.pressDown) {
                    darkColor = new Color(color.r / 2, color.g / 2, color.b / 2, 1);
                    lightColor = new Color(color.r / 5, color.g / 5, color.b / 5, 1);
                } else {
                    darkColor = new Color(color.r / 5, color.g / 5, color.b / 5, 1);
                    lightColor = new Color(color.r / 2, color.g / 2, color.b / 2, 1);
                }
            }
            //button
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(lightColor);
            renderer.rect(position.x, position.y, width, height);
            renderer.setColor(darkColor);
            renderer.rect(position.x + 2, position.y + 2, width - 4, height - 4);
            //different button icons
            switch (type) {
                case "Reset":
                    //reset icon
                    renderer.setColor(lightColor);
                    renderer.arc(position.x + width / 2, position.y + height / 2, height / 3f, 0, 300 + arrowMove, 20);
                    renderer.setColor(darkColor);
                    renderer.circle(position.x + width / 2, position.y + height / 2, height / 4f, 20);
                    renderer.setColor(lightColor);
                    renderer.triangle((position.x + width / 2f) + height / 5f, position.y + height / 2f, (position.x + width / 2f) + height / 2.6f, position.y + height / 2f, (position.x + width / 2f) + height / 3.5f, position.y + height / 3f);
                    break;
                case "Home":
                    //home icon
                    renderer.setColor(lightColor);
                    renderer.rect(position.x + width / 6f, position.y + height / 6f, width / 1.5f, height / 2.5f);
                    renderer.triangle(position.x + width / 6f, position.y + height / 1.7f, position.x + (width / 6f) + (width / 1.5f), position.y + height / 1.7f, position.x + width / 2f, position.y + height / 1.2f);
                    renderer.setColor(darkColor);
                    renderer.rect(position.x + width / 4f, position.y + height / 6f, doorMove, height / 4f);
                    break;
                case "Play":
                    //play icon
                    renderer.setColor(lightColor);
                    renderer.triangle((position.x + width / 4f) + playMove, position.y + height / 6f, (position.x + width / 4f) + playMove, position.y + height * 5f / 6f, (position.x + width * 3f / 4f) + playMove, position.y + height / 2f);
                    break;
                case "Speech":
                    //speech icon
                    renderer.setColor(lightColor);
                    renderer.rect(position.x + width / 6f, position.y + height / 2.28f, width / 1.5f, height / 4f);
                    renderer.rect(position.x + width / 4f, position.y + height / 2.75f, width / 2f, (height / 2.5f));
                    renderer.triangle(position.x + width / 2.25f, position.y + height / 2.75f, position.x + width / 3.25f, position.y + height / 2.75f, position.x + width / 2.25f, position.y + height / 5f);
                    renderer.arc(position.x + width / 4f, (position.y + height / 2.28f) + (height / 4f), width / 12f, 90, 90, 20);
                    renderer.arc((position.x + width / 4f) + (width / 2f), (position.y + height / 2.28f) + (height / 4f), width / 12f, 360, 91, 20);
                    renderer.arc(position.x + width / 4f, (position.y + height / 2.25f), width / 12f, 180, 91, 20);
                    renderer.arc((position.x + width / 4f) + (width / 2f), (position.y + height / 2.25f), width / 12f, 270, 90, 20);
                    if (speech) {
                        renderer.setColor(darkColor);
                        renderer.rectLine(position.x + width / 6f, position.y + height / 1.2f, position.x + width / 1.2f, position.y + height / 4f, width / 8f);
                    }
                    break;
                case "Guide":
                    //question mark icon
                    renderer.setColor(lightColor);
                    renderer.ellipse(position.x + width / 2f - ((width / 8f) / 2f), position.y + height / 6f - ((width / 8f) / 2f), width / 8f, height / 8f, 20);
                    renderer.rectLine(position.x + width / 2f, position.y + height / 4f, position.x + width / 2f, position.y + height / 1.9f, height / 8f);
                    renderer.arc(position.x + width / 2f, position.y + height / 1.5f, width / 4f, 260, 260, 40);
                    renderer.setColor(darkColor);
                    renderer.ellipse(position.x + width / 2f - ((width / 3.5f) / 2f), position.y + height / 1.5f - ((width / 3.5f) / 2f), width / 3.5f, width / 3.5f, 40);
                    break;
                case "Map":
                    //map icon
                    renderer.setColor(lightColor);
                    renderer.rect(position.x + width / 6f, position.y + height / 5f, width / 1.5f, height / 1.7f);
                    renderer.setColor(darkColor);
                    renderer.rectLine(position.x + width / 4f, (position.y + height / 1.7f) + (height / 8f), position.x + width / 3f, (position.y + height / 2f) + (height / 8f), 1);
                    renderer.rectLine(position.x + width / 3f, (position.y + height / 1.7f) + (height / 8f), position.x + width / 4f, (position.y + height / 2f) + (height / 8f), 1);
                    renderer.rectLine((position.x + width / 4f) + (width / 20f), (position.y + height / 1.7f), (position.x + width / 3.5f) + (width / 20f), (position.y + height / 2f), 1);
                    renderer.rectLine((position.x + width / 4f) + (width / 8f), (position.y + height / 2.1f), (position.x + width / 3.5f) + (width / 5f), (position.y + height / 2.3f), 1);
                    renderer.rectLine((position.x + width / 3.5f) + (width / 4f), (position.y + height / 2.4f), (position.x + width / 3.5f) + (width / 3f), (position.y + height / 3f), 1);
                    renderer.circle(position.x + width / 1.4f, position.y + height / 3.7f, 1.5f, 20);
                    break;
                case "Back":
                    //back icon
                    renderer.setColor(lightColor);
                    renderer.triangle((position.x + width * 3f / 4f), position.y + height / 6f, (position.x + width * 3f / 4f), position.y + height * 5f / 6f, (position.x + width / 4f), position.y + height / 2f);
                    break;
                case "Yes":
                    //confirmation icon
                    renderer.setColor(lightColor);
                    renderer.rectLine((position.x + width * 2f / 4f), position.y + height / 6.1f, (position.x + width * 3f / 4f), position.y + height * 5f / 6f, 3);
                    renderer.rectLine((position.x + width * 2.1f / 4f), position.y + height / 6f, (position.x + width / 4.7f), position.y + height * 3f / 6f, 3);
                    break;
                case "No":
                    //negation icon
                    renderer.setColor(lightColor);
                    renderer.rectLine((position.x + width / 5f), position.y + height / 6f, (position.x + width * 4f / 5f), position.y + height * 5f / 6f, 3);
                    renderer.rectLine((position.x + width * 4f / 5f), position.y + height / 6f, (position.x + width / 5f), position.y + height * 5f / 6f, 3);
                    break;
            }
        }
    }

    private boolean pressButton (Vector2 touchPosition) {
        return (touchPosition.x > position.x && touchPosition.x < position.x + width && touchPosition.y > position.y && touchPosition.y < position.y + height && !touchPosition.equals(new Vector2()));
    }
}
