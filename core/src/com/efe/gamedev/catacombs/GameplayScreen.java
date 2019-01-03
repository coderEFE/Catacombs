package com.efe.gamedev.catacombs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.efe.gamedev.catacombs.entities.Word;
import com.efe.gamedev.catacombs.util.ChaseCam;
import com.efe.gamedev.catacombs.util.Constants;

/**
 * Created by coder on 11/13/2017.
 * This is the main game-play screen that cares of all the levels, most of the music, and HUD elements.
 */

public class GameplayScreen extends ScreenAdapter {

    //Game
    public CatacombsGame game;
    //HUD
    private Viewport hudViewport;
    //Level
    private Levels levels;
    private Level level;
    private int levelNumber;
    //ShapeRenderer
    private ShapeRenderer renderer;
    //text setup
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont font2;
    private BitmapFont font3;

    private ChaseCam chaseCam;

    //add audio
    //background music
    private Music music_black_vortex;
    private boolean play_black_vortex;
    private Music music_enter_the_maze;
    private boolean play_enter_the_maze;
    private Music music_cave;
    private boolean play_cave;

    //default button sound
    public Sound sound1;
    //play buttons sound
    public Sound sound2;
    //get item sound
    public Sound sound3;
    //item pop-up sound
    public Sound sound4;
    //select speech bubble option
    public Sound sound5;
    //unlock and lock doors sound
    public Sound sound6;
    //enemy and play lasers sound
    public Sound sound7;
    //big thud sound for stalactites and spike pillars
    public Sound sound8;
    //explosion sound
    public Sound sound9;

    //set current level number
    GameplayScreen(CatacombsGame game, int levelNum) {
        this.game = game;
        levelNumber = levelNum;
    }

    @Override
    public void show () {
        //initialize hud
        hudViewport = new ScreenViewport();

        levels = new Levels();
        level = new Level(hudViewport, levels, this);
        levels.currentLevel = levelNumber;
        levels.configureLevel(level);

        //initialize renderer
        renderer = new ShapeRenderer();
        // Set autoShapeType(true) on the ShapeRenderer
        renderer.setAutoShapeType(true);

        //initialize text
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font2 = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font3 = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));

        //initialize texture
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font2.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font3.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //allow level to use external touch inputs
        Gdx.input.setInputProcessor(level);
        //initialize camera which follows player around the level
        chaseCam = new ChaseCam(level.viewport.getCamera(), level.getPlayer(), level.shake);
        //initialize sound files
        sound1 = Gdx.audio.newSound(Gdx.files.internal("sounds/nff_menu_05_a.wav"));
        sound2 = Gdx.audio.newSound(Gdx.files.internal("sounds/nff_menu_a.wav"));
        sound3 = Gdx.audio.newSound(Gdx.files.internal("sounds/nff_coin.wav"));
        sound3.setVolume(sound3.play(), 0.5f);
        sound4 = Gdx.audio.newSound(Gdx.files.internal("sounds/nff_prompt.wav"));
        sound5 = Gdx.audio.newSound(Gdx.files.internal("sounds/nff_confirm_02.wav"));
        sound6 = Gdx.audio.newSound(Gdx.files.internal("sounds/nff_click_switch.wav"));
        sound7 = Gdx.audio.newSound(Gdx.files.internal("sounds/nff_laser_gun.wav"));
        sound8 = Gdx.audio.newSound(Gdx.files.internal("sounds/nff_thud.wav"));
        sound9 = Gdx.audio.newSound(Gdx.files.internal("sounds/nff_explode.wav"));
        //initialize background music files
        music_black_vortex = Gdx.audio.newMusic(Gdx.files.internal("music/black_vortex.mp3"));
        play_black_vortex = Constants.CURRENT_SONG.equals("black_vortex");
        music_enter_the_maze = Gdx.audio.newMusic(Gdx.files.internal("music/enter_the_maze.mp3"));
        play_enter_the_maze = Constants.CURRENT_SONG.equals("enter_the_maze");
        music_cave = Gdx.audio.newMusic(Gdx.files.internal("music/chee_zee_cave.mp3"));
        play_cave = Constants.CURRENT_SONG.equals("cave");

        //play music
        music_black_vortex.setLooping(false);
        music_enter_the_maze.setLooping(false);
        music_cave.setLooping(false);
    }

    @Override
    public void resize (int width, int height) {
        //Ensure that the viewport updates correctly
        level.viewport.update(width, height, true);
        //Update HUD
        hudViewport.update(width, height, true);
        //set font scale
        font.getData().setScale(Math.min(width, height) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE);
        //make second font bigger
        font2.getData().setScale((Math.min(width, height) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE) * 3f);
        //set third font scale smaller
        font3.getData().setScale(Math.min(width, height) / (Constants.HUD_FONT_REFERENCE_SCREEN_SIZE * 2f));
    }

    @Override
    public void dispose () {
        //dispose of drawing tools when done
        renderer.dispose();
        batch.dispose();
        font.dispose();
        font2.dispose();
        font3.dispose();
        //dispose of sounds when app is closed
        sound1.dispose();
        sound2.dispose();
        sound3.dispose();
        sound4.dispose();
        sound5.dispose();
        sound6.dispose();
        sound7.dispose();
        sound8.dispose();
        sound9.dispose();
        //dispose of background music
        music_black_vortex.dispose();
        music_enter_the_maze.dispose();
        music_cave.dispose();
    }

    @Override
    public void render (float delta) {
        //first song
        if (play_black_vortex && !music_black_vortex.isPlaying()) {
            music_black_vortex.play();
            play_black_vortex = false;
        }
        //moves to next song when finished
        music_black_vortex.setOnCompletionListener(music -> {
            play_enter_the_maze = true;
            Constants.CURRENT_SONG = "enter_the_maze";
        }
        );
        //second song
        if (play_enter_the_maze && !music_enter_the_maze.isPlaying()) {
            music_enter_the_maze.play();
            play_enter_the_maze = false;
        }
        music_enter_the_maze.setOnCompletionListener(music -> {
            play_cave = true;
            Constants.CURRENT_SONG = "cave";
        }
        );
        //third song
        if (play_cave && !music_cave.isPlaying()) {
            music_cave.play();
            play_cave = false;
        }
        music_cave.setOnCompletionListener(music -> {
            play_black_vortex = true;
            Constants.CURRENT_SONG = "black_vortex";
        }
        );
        //make songs quieter when paused
        if (level.inventory.paused || level.defeat || level.victory) {
            music_black_vortex.setVolume(0.5f);
            music_enter_the_maze.setVolume(0.5f);
            music_cave.setVolume(0.5f);
        } else {
            music_black_vortex.setVolume(1f);
            music_enter_the_maze.setVolume(1f);
            music_cave.setVolume(1f);
        }

        levels.update(level, delta);
        level.update(delta);
        //update camera
        chaseCam.update();
        chaseCam.shake = level.shake;
        //change camera shake intensity
        if (levels.currentLevel == 14 && level.currentBubble > 34) {
            chaseCam.shakeDilation = 3f;
        } else {
            chaseCam.shakeDilation = 1f;
        }
        level.cameraPosition.x = (chaseCam.camera.position.x);
        level.cameraPosition.y = (chaseCam.camera.position.y);

        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        level.render(renderer);
        level.viewport.apply();
        batch.setProjectionMatrix(level.viewport.getCamera().combined);
        batch.begin();
        level.renderWords(batch, font3);
        batch.end();
        level.renderHUD(renderer);

        //apply hud
        hudViewport.apply();
        //set projection matrix
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();

        level.renderText(batch, font, font2);
        if (!level.inventory.paused && !level.victory && !level.defeat) {
            for (Word word: level.words) {
                word.renderCollectedText(batch, font, hudViewport);
            }
        }

        batch.end();
    }

    public void showMenuScreen() {
        //dispose of background music
        music_black_vortex.dispose();
        music_enter_the_maze.dispose();
        music_cave.dispose();
        //set screen
        game.showMenuScreen();
    }

    @Override
    public void pause() {
        //pause level
        if (!level.victory && !level.defeat) {
            level.inventory.paused = true;
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

}
