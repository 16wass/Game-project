package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Screens.GameOverScreen;
import de.tum.cit.ase.maze.Screens.LevelScreen;
import de.tum.cit.ase.maze.Screens.WinScreen;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;

import java.io.File;
import java.io.FilenameFilter;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;
    private NativeFileChooser fileChooser;
    private String chosenFilePath;

    // UI Skin
    private Skin skin;
    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        this.fileChooser = fileChooser;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        // Play some background music
        // Background sound
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Gamescreen.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0f);
        backgroundMusic.play();
        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Constructor for MazeRunnerGame.
     *
     * Open The file chooser for the game to choose the level and open the game .
     */

    public String chooseLevel() {
        NativeFileChooserConfiguration config = new NativeFileChooserConfiguration();
        config.directory = Gdx.files.absolute("maps");
        config.mimeFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".properties");
            }
        }.toString();
        config.title = "Choose a level";

        fileChooser.chooseFile(config, new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(FileHandle file) {
                chosenFilePath = file.path();
                Gdx.app.log("Chosen file: " , file.path());
            }

            @Override
            public void onCancellation() {
                System.out.println("User cancelled.");
            }

            @Override
            public void onError(Exception exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        });


        return chosenFilePath;
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }
    /**
     * Switches to the maps screen.
     */
    public void goToMaps() {
        this.setScreen(new LevelScreen(this)); // Set the current screen to MapsScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the Win screen.
     */

    public void goToWin() {
        this.setScreen(new WinScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }
    /**
     * Switches to the GameOverScreen screen.
     */
    public void goToGameOver() {
        this.setScreen(new GameOverScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }


    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
    }
    // Getter methods
    public Skin getSkin() {
        return skin;
    }
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
