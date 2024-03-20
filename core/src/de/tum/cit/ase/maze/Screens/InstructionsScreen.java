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

public class InstructionsScreen implements Screen{

     final MazeRunnerGame game;

    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    Label Instructions;
     Stage mainStage;


    public InstructionsScreen(MazeRunnerGame game) {
        this.game = game;
        this.mainStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(mainStage);
        // load the texture with the image file name "background.jpg"
        backgroundTexture = new Texture(Gdx.files.internal("InstructionsScreen.jpg"));
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
        // create instructions label
        Instructions = new Label("Instructions", game.getSkin());
        Instructions.setColor(20,20,90,1);
        // set position of the label in center of the screen
        Instructions.setPosition(Gdx.graphics.getWidth()/2 - Instructions.getWidth()/2, Gdx.graphics.getHeight()/2 - Instructions.getHeight()/2);
        table.add(Instructions).width(1300).row();

        // create text field
        TextButton button = new TextButton("Instructions", game.getSkin());
        button.getLabel().setColor(20,20,90,1);
        button.setColor(20,0,100,1);
        table.add(button).width(1300).row();


        TextButton button1 = new TextButton("Press 'Go to Levels ' to choose your level", game.getSkin());
        button1.getLabel().setColor(1,1,1,1);
        button1.setColor(0,1,1,1);
        table.add(button1).width(1300).row();

        TextButton button2 = new TextButton("Use the arrow keys to move the character", game.getSkin());
        button2.getLabel().setColor(20,20,90,1);
        button2.setColor(0,1,1,1);
        table.add(button2).width(1300).row();

        TextButton button3 = new TextButton("Collect all the keys to open the exit", game.getSkin());
        button3.getLabel().setColor(20,20,90,1);
        button3.setColor(0,1,1,1);
        table.add(button3).width(1300).row();

        TextButton button4 = new TextButton("Avoid the enemies and traps", game.getSkin());
        button4.getLabel().setColor(20,20,90,1);
        button4.setColor(0,1,1,1);
        table.add(button4).width(1300).row();

        TextButton button5 = new TextButton("Collect keys and reach the exit before the time ends", game.getSkin());
        button5.getLabel().setColor(20,20,90,1);
        button5.setColor(0,1,1,1);
        table.add(button5).width(1300).row();

        TextButton button6 = new TextButton(" You have 60 seconds to play ", game.getSkin());
        button6.getLabel().setColor(20,20,90,1);
        button6.setColor(0,1,1,1);
        table.add(button6).width(1300).row();

        TextButton button7 = new TextButton(" Press 'esc' to exit the game ", game.getSkin());
        button7.getLabel().setColor(20,20,90,1);
        button7.setColor(0,1,1,1);
        table.add(button7).width(1300).row();




        TextButton backButton = new TextButton("Back", game.getSkin());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Go back to the title screen
                game.setScreen(new MenuScreen(game));
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
}
