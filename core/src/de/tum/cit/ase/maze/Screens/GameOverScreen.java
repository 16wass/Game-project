package de.tum.cit.ase.maze.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.tum.cit.ase.maze.MazeRunnerGame;
import de.tum.cit.ase.maze.MenuScreen;

public class GameOverScreen implements Screen{

    final MazeRunnerGame game;

    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    Stage mainStage;


    public GameOverScreen(MazeRunnerGame game) {
        this.game = game;
        this.mainStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(mainStage);

        Music GameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("GameOver.wav"));
        GameOverMusic.play();
        GameOverMusic.stop();
        Music GameOverMusic2 = Gdx.audio.newMusic(Gdx.files.internal("GameOver2.wav"));
        GameOverMusic2.play();
        GameOverMusic.play();


    }


    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        mainStage.addActor(table);

        // load the texture with the image file name "background.jpg"
        backgroundTexture = new Texture(Gdx.files.internal("GameOverScreen.jpg"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize((float) (Gdx.graphics.getWidth()*0.75), (float) (Gdx.graphics.getHeight()*0.5));
        // Set the position to center the image on the screen
        backgroundSprite.setPosition((mainStage.getWidth()/4 - backgroundSprite.getWidth()) / 2,
                (mainStage.getHeight()/2 - backgroundSprite.getHeight()) / 2);

        table.add(new Label(" You lose ! ", game.getSkin(), "title")).padBottom(40).row();
        // set position of the label in center of the screen
        Label  label = new Label(" You have been caught by the enemy ", game.getSkin(), "title");
        table.add(label).padBottom(70).row();
        label.setPosition(Gdx.graphics.getWidth()/2 - label.getWidth()/2, Gdx.graphics.getHeight()/2 - label.getHeight()/2);
        table.row();
        table.add(new Label(" ", game.getSkin(), "title")).padBottom(70).row();
        table.add(new Label(" ", game.getSkin(), "title")).padBottom(70).row();

        // create a button to go back to the main menu
        TextButton goToMenuButton = new TextButton("Go To Menu", game.getSkin());
        goToMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to the menu screen when the button is clicked
                game.goToMenu();
            }
        });
        table.add(goToMenuButton).width(300).row();
        // Set the background sprite's position to be fixed relative to the screen
        backgroundSprite.setPosition(0, 1); // Set the x and y coordinates to 0 to fix the position


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
        mainStage.getViewport().update(width, height, true); // Update the stage viewport on resize

        /** Set the position to center the image on the screen*/
//        backgroundSprite.setPosition((mainStage.getWidth()/3 - backgroundSprite.getWidth()) / 2,
//                (mainStage.getHeight()/2 - backgroundSprite.getHeight()) / 2);

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
}
