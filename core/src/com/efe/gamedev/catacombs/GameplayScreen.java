package com.efe.gamedev.catacombs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.efe.gamedev.catacombs.entities.Player;
import com.efe.gamedev.catacombs.entities.SpeechBubble;
import com.efe.gamedev.catacombs.entities.Word;
import com.efe.gamedev.catacombs.util.ChaseCam;
import com.efe.gamedev.catacombs.util.Constants;


/**
 * Created by coder on 11/13/2017.
 */

public class GameplayScreen extends ScreenAdapter {

    private static final String TAG = GameplayScreen.class.getName();

    //HUD
    private Viewport hudViewport;
    //Level
    private Levels levels;
    private Level level;
    //ShapeRenderer
    private ShapeRenderer renderer;
    //text setup
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont font2;
    private BitmapFont font3;

    private ChaseCam chaseCam;

    @Override
    public void show () {
        //initialize hud
        hudViewport = new ScreenViewport();

        levels = new Levels();
        level = new Level(hudViewport, levels);
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

        //allow player to use external touch inputs
        Gdx.input.setInputProcessor(level);
        chaseCam = new ChaseCam(level.viewport.getCamera(), level.getPlayer());
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
    }

    @Override
    public void render (float delta) {
        levels.update(level);
        level.update(delta);
        chaseCam.update();

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
        if (!level.inventory.paused) {
            for (Word word: level.words) {
                word.renderCollectedText(batch, font, hudViewport);
            }
        }

        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

}
