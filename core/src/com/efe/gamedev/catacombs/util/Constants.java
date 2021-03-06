package com.efe.gamedev.catacombs.util;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by coder on 11/13/2017.
 * This class holds many variables that stay the same and can be referenced by any class
 */

public class Constants {

    //World and general Constants
    public static final Color BACKGROUND_COLOR = Color.BROWN;
    public static final float WORLD_SIZE = 160;
    //menu
    public static final float MENU_WORLD_SIZE = 480.0f;
    public static final Color MENU_BACKGROUND_COLOR = Color.BLUE;
    public static String CURRENT_SONG = "black_vortex";
    //public static
    //default gravity is 10
    public static final float GRAVITY = 10;

    //Screen reference size for scaling HUD
    public static final float HUD_FONT_REFERENCE_SCREEN_SIZE = 560.0f;
    public static final String FONT_FILE = "font/header.fnt";

    //Player Constants
    public static final Color SKIN_COLOR = Color.TAN;
    public static final Color PRISONER_SKIN_COLOR = new Color((1 / 255f) * 133, (1 / 255f) * 88, (1 / 255f) * 30, 1f);
    public static final Color BOSS_SKIN_COLOR = new Color((1 / 255f) * 163, (1 / 255f) * 166, (1 / 255f) * 148, 1f);
    public static final Color GUARD_CLOTHES_COLOR = new Color(0.3f, 0.3f, 0.4f, 1);
    public static final int HEAD_SEGMENTS = 40;
    public static final float HEAD_SIZE = 8;
    public static final float HAND_SIZE = 2;
    public static final float EYE_OFFSET = HEAD_SIZE / 4;
    public static final float MOUTH_THICKNESS = 0.5f;
    public static final float MOUTH_TALKING_SPEED = 5;
    public static final float PLAYER_WIDTH = 8;
    public static final float PLAYER_HEIGHT = 22;

    //SpeechBubble
    public static final float SPEECH_BUBBLE_MARGIN = 15;
    public static final float HUD_SPEECH_BUBBLE_MARGIN = 30;

    //items
    public static final Color TARGET_COLOR = Color.MAROON;
    public static final float MAX_DIAMONDS = 3;
    public static final Color SHADOW_COLOR = new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 0.5f);
}
