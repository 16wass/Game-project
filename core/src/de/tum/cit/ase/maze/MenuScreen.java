package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.Screens.InstructionsScreen;
import de.tum.cit.ase.maze.Screens.LevelScreen;
import de.tum.cit.ase.maze.entities.World;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;

    public MazeRunnerGame game;
    private Sprite backgroundSprite;
    private Texture backgroundTexture;
    private World world;
    Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Menuscreen.wav"));

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        this.game = game;
        this.world = new World(game.getSpriteBatch());
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view
        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements
        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage
        // Load the background image and create a Sprite object
        backgroundTexture = new Texture(Gdx.files.internal("MenuScreen.jpg"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize((float) (stage.getWidth()*1.5), (float) (stage.getHeight()*1.5));
        // Set the position to center the image on the screen
        backgroundSprite.setPosition((stage.getWidth() - backgroundSprite.getWidth()) / 2,
                (stage.getHeight() - backgroundSprite.getHeight()) / 2);



        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        // Add a label as a title
        table.add(new Label("Hello World from the creators of the game!", game.getSkin(), "title")).padBottom(80).row();
        // Create and add a button to go to the game screen
        TextButton goToGameButton = new TextButton("Go To Game", game.getSkin());
        table.add(goToGameButton).width(300).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.chooseLevel();
                world.getPropertiesMapForFileChooser();
                game.goToGame(); // Change to the game screen when button is pressed
                backgroundMusic.stop();

            }
        });

        //Create and add a button to choose maps
        TextButton levelsButton = new TextButton("Go to Levels", game.getSkin());
        table.add(levelsButton).width(300).row();
        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Go to the LevelScreen
                game.goToMaps();
                backgroundMusic.stop();


            }
        });

        table.row();
        //add a button to go to the instructions screen
        TextButton instructionsButton = new TextButton("Instructions", game.getSkin());
        table.add(instructionsButton).width(300).row();
        instructionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Go to the InstructionsScreen
                game.setScreen(new InstructionsScreen(game));
                backgroundMusic.stop();
            }
        });

        //empty row
        table.row();
        //add a label for the credits
        table.add(new Label("Made by MazeMinds", game.getSkin(), "title")).padBottom(200).row();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        // Draw the background
        game.getSpriteBatch().begin();
        backgroundSprite.draw(game.getSpriteBatch());


        game.getSpriteBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
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