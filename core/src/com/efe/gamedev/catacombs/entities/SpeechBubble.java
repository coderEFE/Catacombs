package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 11/16/2017.
 * This is the SpeechBubble from which Characters talk from.
 * It is made up of a rounded speech bubble and several options you can choose if the text is a question
 * It is always located in the top left corner.
 */

public class SpeechBubble {

    private static final String TAG = SpeechBubble.class.getName();
    //get level
    private Level level;
    //get parameter variables
    public Vector2 position;
    public float width;
    public float height;
    private String text;
    public String target;
    public float targetOffset;
    //make speechBubbleAlpha
    public float speechBubbleAlpha = 0f;
    public float option1Alpha;
    public float option2Alpha;
    public float option3Alpha;
    public float option4Alpha;
    //text
    public int typeTimer;
    public int typeSpeed = 3;
    public String typedUpText;
    public boolean textLoaded;

    private float viewportOffset;

    //Options
    public int Options;
    public String option1;
    public String option2;
    public String option3;
    public String option4;

    //function for options
    private Runnable function1;
    private Runnable function2;
    private Runnable function3;
    public Runnable function4;

    //skip button used to skip parts of speech
    public int speechBubbleSkip;

    //trigger boolean
    public boolean triggered;

    public SpeechBubble (String text, String target, int Options, String option1, String option2, String option3, String option4, final Runnable function1, final Runnable function2, final Runnable function3, final Runnable function4, Level level, int speechBubbleSkip) {
        position = new Vector2(Constants.HUD_SPEECH_BUBBLE_MARGIN, level.viewport.getWorldHeight() - height - Constants.SPEECH_BUBBLE_MARGIN);
        this.text = text;
        this.target = target;
        this.level = level;
        this.Options = Options;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.function1 = function1;
        this.function2 = function2;
        this.function3 = function3;
        this.function4 = function4;
        triggered = false;
        viewportOffset = level.viewport.getWorldWidth() / 2f;
        width = text.length();
        height = 10;
        typeTimer = 0;
        targetOffset = ((target.length() - 6) * 4f);
        textLoaded = false;
        //default speechBubble skip location is -1, meaning that it does not have a skip button.
        this.speechBubbleSkip = speechBubbleSkip;
    }

    public void update () {
        viewportOffset = level.viewport.getWorldWidth() / 2f;
        position = new Vector2(Constants.HUD_SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().x - viewportOffset, level.viewport.getWorldHeight() - height - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80);
        //make it look like text types itself out
        typeTimer += 1;
        typedUpText = text.substring(0, ((typeTimer / typeSpeed) < text.length()) ? typeTimer / typeSpeed : text.length());
        //Gdx.app.log(TAG, "" + (typedUpText.length() == 0 ? "" : typedUpText.charAt(typedUpText.length() - 1)));
        //checks to see if the text is all typed out and gives a boolean value
        if (!((typeTimer / typeSpeed) < text.length())) {
            textLoaded = true;
        }
        String search = "\n";
        //checks if text has \n in it
        if (text.toLowerCase().contains(search.toLowerCase())) {
            //assigns widths and heights
            if (text.toLowerCase().indexOf(search.toLowerCase()) >= ((text.length() - text.toLowerCase().indexOf(search.toLowerCase())))) {
                width = (text.toLowerCase().indexOf(search.toLowerCase()) * 3.5f);
            } else if (text.length() - text.toLowerCase().indexOf(search.toLowerCase()) > text.toLowerCase().indexOf(search.toLowerCase())) {
                width = ((text.length() - text.toLowerCase().indexOf(search.toLowerCase())) * 3.5f) - 8;
            }
            height = 20;
        } else {
            //otherwise go with normal
            width = (text.length() * 3.5f);
            height = 10;
        }
    }

    //see if player is pressing inside options, then activate that option's function
    public void trySelecting () {
        //run option functions if option is pressed
        //play confirm sound when option is selected
        if (Options >= 1 && !option1.equals("") && level.show && option1Alpha > 0.95 && level.touchPosition.x > position.x - 15 && level.touchPosition.x < (position.x - 15) + (level.viewport.getWorldWidth() / 2.5f) && level.touchPosition.y > (position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10) && level.touchPosition.y < ((position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10)) + (level.viewport.getWorldHeight() / 5f)) {
            level.gameplayScreen.sound5.play();
            function1.run();
        }
        if (Options >= 2 && !option2.equals("") && level.show && option2Alpha > 0.95 && level.touchPosition.x > (position.x - 15) + (level.viewport.getWorldWidth() / 2.2f) && level.touchPosition.x < (position.x - 15) + (level.viewport.getWorldWidth() / 2.2f) + (level.viewport.getWorldWidth() / 2.5f) && level.touchPosition.y > (position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10) && level.touchPosition.y < ((position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10)) + (level.viewport.getWorldHeight() / 5f)) {
            level.gameplayScreen.sound5.play();
            function2.run();
        }
        if (Options >= 3 && !option3.equals("") && level.show && option3Alpha > 0.95 && level.touchPosition.x > position.x - 15 && level.touchPosition.x < (position.x - 15) + (level.viewport.getWorldWidth() / 2.5f) && level.touchPosition.y > (position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10) && level.touchPosition.y < ((position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10)) + (level.viewport.getWorldHeight() / 5f)) {
            level.gameplayScreen.sound5.play();
            function3.run();
        }
        if (Options >= 4 && !option4.equals("") && level.show && option4Alpha > 0.95 && level.touchPosition.x > (position.x - 15) + (level.viewport.getWorldWidth() / 2.2f) && level.touchPosition.x < (position.x - 15) + (level.viewport.getWorldWidth() / 2.2f) + (level.viewport.getWorldWidth() / 2.5f) && level.touchPosition.y > (position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10) && level.touchPosition.y < ((position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10)) + (level.viewport.getWorldHeight() / 5f)) {
            level.gameplayScreen.sound5.play();
            function4.run();
        }
        //press speechBubble skip button
        if (level.touchPosition.x > (position.x + width + 42 + targetOffset) && level.touchPosition.x < (position.x + width + 42 + targetOffset) + (35) && level.touchPosition.y > (level.viewport.getWorldHeight() - 10 - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80) + 0.5f && level.touchPosition.y < (level.viewport.getWorldHeight() - 10 - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80) + 14.5f && !level.touchPosition.equals(new Vector2()) && speechBubbleSkip != -1 && level.lastSeenBubble >= speechBubbleSkip) {
            //reset mouths so that there are no talking glitches
            for (Guard guard : level.guards) {
                guard.mouthState = Enums.MouthState.NORMAL;
            }
            level.getPlayer().mouthState = Enums.MouthState.NORMAL;
            //change current speechBubble to the one in speechBubbleSkip
            level.currentBubble = speechBubbleSkip;
            level.touchPosition.set(new Vector2());
            level.gameplayScreen.sound1.play();
        }
    }

    //draw backdrop of bubble
    public void renderBubble (ShapeRenderer renderer) {
        //if position is loaded, draw speechBubble
        if (position.equals(new Vector2(Constants.HUD_SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().x - viewportOffset, level.viewport.getWorldHeight() - height - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80))) {
            //first draw bubble with no alpha, then increase alpha quickly
            renderer.setColor(target.equals("c7-x:") ? new Color(0.9f, 0.9f, 1.0f, speechBubbleAlpha) : (target.equals("Security:") ? new Color(1.0f, 0.8f, 0.8f, speechBubbleAlpha) : new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, speechBubbleAlpha)));
            //draw bubble depending on target type, if it is a sign, draw a rectangle, else, draw a rounded rectangle.
            if (!target.equals("Sign:")) {
                renderer.rect(position.x - 10 - 14, position.y + 3, width + 60 + targetOffset, height);
                renderer.rect(position.x - 5 - 14, position.y - 2, width + 50 + targetOffset, height + 10);
                renderer.arc(position.x - 5 - 14, position.y + 3 + height, 5, 90, 90, 10);
                renderer.arc(position.x + width + 31 + targetOffset, position.y + 3 + height, 5, 360, 91, 10);
                renderer.arc(position.x - 5 - 14, position.y + 3, 5, 180, 90, 10);
                renderer.arc(position.x + width + 31 + targetOffset, position.y + 3, 5, 270, 90, 10);
            } else {
                renderer.rect(position.x - 10 - 14, position.y - 2, width + 60 + targetOffset, height + 10);
            }
            //fade in speechBubble's alpha
            if (speechBubbleAlpha < 1) {
                speechBubbleAlpha += 0.05f;
            }
            //skip button used to skip parts of speech
            //you can only skip speechBubbles if you haven't seen the whole level yet
            if (speechBubbleSkip != -1 && level.lastSeenBubble >= speechBubbleSkip) {
                renderer.setColor(new Color(0.9f, 0.9f, 0.6f, 1f));
                renderer.rect(position.x + width + 42 + targetOffset, (level.viewport.getWorldHeight() - 10 - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80) + 0.5f, 35, 14);
                //draw fast forward symbol
                renderer.setColor(Color.DARK_GRAY);
                //first triangle
                renderer.triangle(position.x + width + 47 + targetOffset, (level.viewport.getWorldHeight() - 10 - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80) + 2.5f, position.x + width + 47 + targetOffset, (level.viewport.getWorldHeight() - 10 - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80) + 12.5f, position.x + width + 57 + targetOffset, (level.viewport.getWorldHeight() - 10 - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80) + 7.5f);
                //second triangle
                renderer.triangle(position.x + width + 57 + targetOffset, (level.viewport.getWorldHeight() - 10 - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80) + 2.5f, position.x + width + 57 + targetOffset, (level.viewport.getWorldHeight() - 10 - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80) + 12.5f, position.x + width + 67 + targetOffset, (level.viewport.getWorldHeight() - 10 - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80) + 7.5f);
                //final rectangle
                renderer.rect(position.x + width + 67 + targetOffset, (level.viewport.getWorldHeight() - 10 - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80) + 2.5f, 5f, 10f);
            }
        }

        //draw how many options there are for player to choose
        //first draw bubbles with no alpha, then increase alpha quickly
        if (Options >= 1 && !option1.equals("")) {
            renderer.setColor(new Color(0.9f, 0.9f, 0.6f, option1Alpha));
            renderer.rect(position.x - 15, (position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10), level.viewport.getWorldWidth() / 2.5f, level.viewport.getWorldHeight() / 5f);
        }
        if (Options >= 2 && !option2.equals("")) {
            renderer.setColor(new Color(0.9f, 0.9f, 0.6f, option2Alpha));
            renderer.rect((position.x - 15) + (level.viewport.getWorldWidth() / 2.2f), (position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10), level.viewport.getWorldWidth() / 2.5f, level.viewport.getWorldHeight() / 5f);
        }
        if (Options >= 3 && !option3.equals("")) {
            renderer.setColor(new Color(0.9f, 0.9f, 0.6f, option3Alpha));
            renderer.rect(position.x - 15, (position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10), level.viewport.getWorldWidth() / 2.5f, level.viewport.getWorldHeight() / 5f);
        }
        if (Options >= 4 && !option4.equals("")) {
            renderer.setColor(new Color(0.9f, 0.9f, 0.6f, option4Alpha));
            renderer.rect((position.x - 15) + (level.viewport.getWorldWidth() / 2.2f), (position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10), level.viewport.getWorldWidth() / 2.5f, level.viewport.getWorldHeight() / 5f);
        }
        //update options' alpha when text is done typing
        if (option1Alpha < 1 && textLoaded) {
            option1Alpha += 0.02;
        }
        if (option1Alpha > 0.95 && option2Alpha < 1) {
            option2Alpha += 0.02;
        }
        if (option2Alpha > 0.95 && option3Alpha < 1) {
            option3Alpha += 0.02;
        }
        if (option3Alpha > 0.95 && option4Alpha < 1) {
            option4Alpha += 0.02;
        }
    }

    //write text
    public void renderText (SpriteBatch batch, BitmapFont font, Viewport hudViewport) {
        //speech text
        font.setColor(Color.DARK_GRAY);
        font.draw(batch, "" + typedUpText, hudViewport.getWorldHeight() / 3.5f + ((target.length() - 6) * 8f), hudViewport.getWorldHeight() - (height == 20 ? hudViewport.getWorldHeight() / 11 : hudViewport.getWorldHeight() / 11));
        //options text
        if (Options >= 1 && option1Alpha > 0.95) {
            font.draw(batch, option1, hudViewport.getWorldWidth() / 10f, hudViewport.getWorldHeight() / 1.45f, hudViewport.getWorldWidth() / 3f, Align.center, true);
        }
        if (Options >= 2 && option2Alpha > 0.95) {
            font.draw(batch, option2, hudViewport.getWorldWidth() / 1.8f, hudViewport.getWorldHeight() / 1.45f, hudViewport.getWorldWidth() / 3f, Align.center, true);
        }
        if (Options >= 3 && option3Alpha > 0.95) {
            font.draw(batch, option3, hudViewport.getWorldWidth() / 10f, hudViewport.getWorldHeight() / 2.3f, hudViewport.getWorldWidth() / 3f, Align.center, true);
        }
        if (Options >= 4 && option4Alpha > 0.95) {
            font.draw(batch, option4, hudViewport.getWorldWidth() / 1.8f, hudViewport.getWorldHeight() / 2.3f, hudViewport.getWorldWidth() / 3f, Align.center, true);
        }
        //target text
        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, target, hudViewport.getWorldHeight() / 10, hudViewport.getWorldHeight() - (height == 20 ? hudViewport.getWorldHeight() / 11 : hudViewport.getWorldHeight() / 11));
        //skip button text
        /*if (speechBubbleSkip != -1 && level.lastSeenBubble >= speechBubbleSkip) {
            font.setColor(Color.DARK_GRAY);
            //a series of if statements determine the location of the "SKIP" button text.
            //if the speechBubble has two lines of text in it, change the x position
            if (text.toLowerCase().contains(("\n").toLowerCase())) {
                //if the index of the new line is more than or equal to the length of the whole text minus that index, set x position to that index, else, set it to the text length minus that index.
                if (text.toLowerCase().indexOf(("\n").toLowerCase()) >= ((text.length() - text.toLowerCase().indexOf(("\n").toLowerCase())))) {
                    //font.draw(batch, "SKIP", ((hudViewport.getWorldHeight() / 1.94f) + ((target.length() - 6) * 8f) + ((text.toLowerCase().indexOf(("\n").toLowerCase())) * 9.5f)) - ((480 - hudViewport.getWorldHeight()) * (text.toLowerCase().indexOf(("\n").toLowerCase()) / 52f)), hudViewport.getWorldHeight() - (height == 20 ? hudViewport.getWorldHeight() / 11 : hudViewport.getWorldHeight() / 11));
                    font.draw(batch, "SKIP", ((hudViewport.getWorldHeight() / 1.94f) + ((target.length() - 6) * 8f) + (text.toLowerCase().indexOf(("\n").toLowerCase()) * 10.5f)) - ((480 - hudViewport.getWorldHeight()) * (text.toLowerCase().indexOf(("\n").toLowerCase()) / 45f)), hudViewport.getWorldHeight() - (height == 20 ? hudViewport.getWorldHeight() / 11 : hudViewport.getWorldHeight() / 11));
                } else if (text.length() - text.toLowerCase().indexOf(("\n").toLowerCase()) > text.toLowerCase().indexOf(("\n").toLowerCase())) {
                    //font.draw(batch, "SKIP", ((hudViewport.getWorldHeight() / 1.94f) + ((target.length() - 6) * 8f) + ((text.length() - text.toLowerCase().indexOf(("\n").toLowerCase())) * 10f)) - ((480 - hudViewport.getWorldHeight()) * ((text.length() - text.toLowerCase().indexOf(("\n").toLowerCase())) / 52f)), hudViewport.getWorldHeight() - (height == 20 ? hudViewport.getWorldHeight() / 11 : hudViewport.getWorldHeight() / 11));
                    font.draw(batch, "SKIP", ((hudViewport.getWorldHeight() / 1.94f) + ((target.length() - 6) * 8f) + ((text.length() - text.toLowerCase().indexOf(("\n").toLowerCase())) * 9.5f)) - ((480 - hudViewport.getWorldHeight()) * ((text.length() - text.toLowerCase().indexOf(("\n").toLowerCase())) / 55f)), hudViewport.getWorldHeight() - (height == 20 ? hudViewport.getWorldHeight() / 11 : hudViewport.getWorldHeight() / 11));
                }
            } else {
                //if the speechBubble only has one line of text, draw x position a certain way
                font.draw(batch, "SKIP", ((hudViewport.getWorldHeight() / 1.94f) + ((target.length() - 6) * 8f) + (text.length() * 10.5f)) - ((480 - hudViewport.getWorldHeight()) * (text.length() / 45f)), hudViewport.getWorldHeight() - (height == 20 ? hudViewport.getWorldHeight() / 11 : hudViewport.getWorldHeight() / 11));
            }
            //TODO: maybe have a fast-forward icon instead of the word "SKIP"
        }*/
    }

}
