package de.tum.cit.ase.maze.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class WinScreen implements Screen{

    final MazeRunnerGame game;

    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    Stage mainStage;


    public WinScreen(MazeRunnerGame game) {
        this.game = game;
        this.mainStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(mainStage);

        // load the texture with the image file name "background.jpg"
        backgroundTexture = new Texture(Gdx.files.internal("VictoryScreen.jpg"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize((float) (Gdx.graphics.getWidth()*0.5), (float) (Gdx.graphics.getHeight()*0.5));

        // set to center of the screen

        // Set the position to center the image on the screen

        backgroundSprite.setPosition((mainStage.getWidth()/6 - backgroundSprite.getWidth()) / 2,
                (mainStage.getHeight()/6 - backgroundSprite.getHeight()) / 2);



    }


    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        mainStage.addActor(table);
        table.add(new Label(" Congratulations! ", game.getSkin(), "title")).padBottom(70).row();
        // add an empty row
        table.row();
        table.add(new Label(" You have successfully escaped the maze ", game.getSkin(), "title")).padBottom(70).row();
        // add an empty row
        table.row();
        // create a button to go back to the main menu
        TextButton goToMenuButton = new TextButton("Go To Menu", game.getSkin());
        // add a listener to the button to handle clicks
        goToMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to the menu screen when the button is clicked
                game.goToMenu();
            }
        });
        table.add(goToMenuButton).width(300).row();

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
}
