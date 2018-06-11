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
    private Vector2 position;
    private float width;
    private float height;
    private String text;
    public String target;
    private float targetOffset;
    //make speechBubbleAlpha
    public float speechBubbleAlpha = 0f;
    public float option1Alpha;
    public float option2Alpha;
    public float option3Alpha;
    public float option4Alpha;
    //text
    public int typeTimer;
    public int typeSpeed = 4;
    public String typedUpText;
    public boolean textLoaded;

    private float viewportOffset;

    //Options
    public int Options;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

    //function for options
    private Runnable function1;
    private Runnable function2;
    private Runnable function3;
    public Runnable function4;

    //trigger boolean
    public boolean triggered;

    public SpeechBubble (String text, String target, int Options, String option1, String option2, String option3, String option4, final Runnable function1, final Runnable function2, final Runnable function3, final Runnable function4, Level level) {
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
        if (text.toLowerCase().indexOf(search.toLowerCase()) != -1) {
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
        if (Options >= 1 && option1Alpha > 0.95 && level.touchPosition.x > position.x - 15 && level.touchPosition.x < (position.x - 15) + (level.viewport.getWorldWidth() / 2.5f) && level.touchPosition.y > (position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10) && level.touchPosition.y < ((position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10)) + (level.viewport.getWorldHeight() / 5f)) {
            function1.run();
        }
        if (Options >= 2 && option2Alpha > 0.95 && level.touchPosition.x > (position.x - 15) + (level.viewport.getWorldWidth() / 2.2f) && level.touchPosition.x < (position.x - 15) + (level.viewport.getWorldWidth() / 2.2f) + (level.viewport.getWorldWidth() / 2.5f) && level.touchPosition.y > (position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10) && level.touchPosition.y < ((position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10)) + (level.viewport.getWorldHeight() / 5f)) {
            function2.run();
        }
        if (Options >= 3 && option3Alpha > 0.95 && level.touchPosition.x > position.x - 15 && level.touchPosition.x < (position.x - 15) + (level.viewport.getWorldWidth() / 2.5f) && level.touchPosition.y > (position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10) && level.touchPosition.y < ((position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10)) + (level.viewport.getWorldHeight() / 5f)) {
            function3.run();
        }
        if (Options >= 4 && option4Alpha > 0.95 && level.touchPosition.x > (position.x - 15) + (level.viewport.getWorldWidth() / 2.2f) && level.touchPosition.x < (position.x - 15) + (level.viewport.getWorldWidth() / 2.2f) + (level.viewport.getWorldWidth() / 2.5f) && level.touchPosition.y > (position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10) && level.touchPosition.y < ((position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10)) + (level.viewport.getWorldHeight() / 5f)) {
            function4.run();
        }
    }

    //draw backdrop of bubble
    public void renderBubble (ShapeRenderer renderer) {
        //if position is loaded, draw speechBubble
        if (position.equals(new Vector2(Constants.HUD_SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().x - viewportOffset, level.viewport.getWorldHeight() - height - Constants.SPEECH_BUBBLE_MARGIN + level.getPlayer().getPosition().y - 80))) {
            //first draw bubble with no alpha, then increase alpha quickly
            renderer.setColor(target.equals("c7-x:") ? new Color(0.9f, 0.9f, 1.0f, speechBubbleAlpha) : (target.equals("Security:") ? new Color(1.0f, 0.8f, 0.8f, speechBubbleAlpha) : new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, speechBubbleAlpha)));
            renderer.rect(position.x - 10 - 14, position.y + 3, width + 60 + targetOffset, height);
            renderer.rect(position.x - 5 - 14, position.y - 2, width + 50 + targetOffset, height + 10);
            renderer.arc(position.x - 5 - 14, position.y + 3 + height, 5, 90, 90, 10);
            renderer.arc(position.x + width + 31 + targetOffset, position.y + 3 + height, 5, 360, 91, 10);
            renderer.arc(position.x - 5 - 14, position.y + 3, 5, 180, 90, 10);
            renderer.arc(position.x + width + 31 + targetOffset, position.y + 3, 5, 270, 90, 10);
            //fade in speechBubble's alpha
            if (speechBubbleAlpha < 1) {
                speechBubbleAlpha += 0.05f;
            }
        }

        //draw how many options there are for player to choose
        //first draw bubbles with no alpha, then increase alpha quickly
        if (Options >= 1) {
            renderer.setColor(new Color(0.9f, 0.9f, 0.6f, option1Alpha));
            renderer.rect(position.x - 15, (position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10), level.viewport.getWorldWidth() / 2.5f, level.viewport.getWorldHeight() / 5f);
        }
        if (Options >= 2) {
            renderer.setColor(new Color(0.9f, 0.9f, 0.6f, option2Alpha));
            renderer.rect((position.x - 15) + (level.viewport.getWorldWidth() / 2.2f), (position.y - level.viewport.getWorldHeight() / 4f) - (height == 20 ? 0 : 10), level.viewport.getWorldWidth() / 2.5f, level.viewport.getWorldHeight() / 5f);
        }
        if (Options >= 3) {
            renderer.setColor(new Color(0.9f, 0.9f, 0.6f, option3Alpha));
            renderer.rect(position.x - 15, (position.y - level.viewport.getWorldHeight() / 2f) - (height == 20 ? 0 : 10), level.viewport.getWorldWidth() / 2.5f, level.viewport.getWorldHeight() / 5f);
        }
        if (Options >= 4) {
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
    }

}
