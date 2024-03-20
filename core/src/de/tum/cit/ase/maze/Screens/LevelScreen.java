package de.tum.cit.ase.maze.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.tum.cit.ase.maze.GameScreen;
import de.tum.cit.ase.maze.MazeRunnerGame;
import de.tum.cit.ase.maze.MenuScreen;

import java.io.IOException;

public class LevelScreen implements Screen{

    final MazeRunnerGame game;

    private static String filePath;

    private Texture backgroundTexture;
    private Sprite backgroundSprite;

    Stage mainStage;




    public LevelScreen(MazeRunnerGame game) {
        this.game = game;
        this.mainStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(mainStage);
        backgroundTexture = new Texture(Gdx.files.internal("LevelScreen.jpg"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize((float) (mainStage.getWidth()*1.5), (float) (mainStage.getHeight()*1.5));
        // Set the position to center the image on the screen
        backgroundSprite.setPosition((mainStage.getWidth() - backgroundSprite.getWidth()) / 2,
                (mainStage.getHeight() - backgroundSprite.getHeight()) / 2);



    }


    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        mainStage.addActor(table);
        //create 5 buttons for level selection
        TextButton level1Button = new TextButton("Level 1", game.getSkin());
        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // load map for level 1
                filePath = "maps/level-1.properties";
                // transition to the game screen
                game.goToGame();
            }
        });
        table.add(level1Button).width(200).row();
        TextButton level2Button = new TextButton("Level 2", game.getSkin());
        level2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // load map for level 2
                filePath = "maps/level-2.properties";
                // transition to the game screen
                game.goToGame();
            }
        });
        table.add(level2Button).width(200).row();
        TextButton level3Button = new TextButton("Level 3", game.getSkin());
        level3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // load map for level 3
                filePath = "maps/level-3.properties";
                // transition to the game screen
                game.goToGame();
            }
        });
        table.add(level3Button).width(200).row();
        TextButton level4Button = new TextButton("Level 4", game.getSkin());
        level4Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // load map for level 4
                filePath = "maps/level-4.properties";
                // transition to the game screen
                game.goToGame();
            }
        });
        table.add(level4Button).width(200).row();
        TextButton level5Button = new TextButton("Level 5", game.getSkin());
        level5Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // load map for level 5
                filePath = "maps/level-5.properties";
                // transition to the game screen
                game.goToGame();
            }
        });
        table.add(level5Button).width(200).row();




        TextButton backButton = new TextButton("Back", game.getSkin());
        backButton.getLabel().setColor(20,20,90,1);
        backButton.setColor(0,1,1,1);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Go back to the title screen
                game.goToMenu();

            }
        });

        table.add(backButton).width(300).padTop(20);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Draw the background
        game.getSpriteBatch().begin();
        backgroundSprite.draw(game.getSpriteBatch());
        game.getSpriteBatch().end();
        // Draw the stage
        mainStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        mainStage.draw();

    }

    @Override
    public void resize(int width, int height) {

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

    @Override
    public void dispose() {

    }


    public String getPropertiesFilePath() {
        return filePath;
    }
}






