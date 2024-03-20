package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.ase.maze.entities.Enemy;
import de.tum.cit.ase.maze.entities.Player;
import de.tum.cit.ase.maze.entities.World;

import java.util.HashMap;
import java.util.Map;


/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private float sinusInput = 0f;
    private SpriteBatch batch;
    private Player player;
    private Enemy enemy;
    private World world;
    private long lastTrapActivationTime = 0;


    private HUD hud;
    Sound winMusic = Gdx.audio.newSound(Gdx.files.internal("assets/WinMusic.wav"));
    private float totalTime = 60; // Total time in seconds
    private float elapsedTime = 0; // Elapsed time in seconds
    int keys = 0;
    int lives = 3;
    private int frameCounter = 0;
    private float timeSinceLastFrameUpdate = 0;
    private int fps = 0;
    Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Gamescreen.mp3"));

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.5f;
        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
        batch = new SpriteBatch();
        world = new World(game.getSpriteBatch());
        player = new Player(world.getEntryPointX(), world.getEntryPointY(), batch,world, game, hud); // Initial position of the player
        //enemy = new Enemy(500, 100, batch); // Initial position of the enemy
        this.hud = new HUD(game,batch, game.getSkin(),player,keys,lives);
        backgroundMusic.play();
        backgroundMusic.setLooping(true);


    }

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        System.out.println("this is the delta value" + delta);
        frameCounter++;
        timeSinceLastFrameUpdate += delta;
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        camera.update(); // Update the camera
        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        float textX = (float) (camera.position.x / 2);//+ Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y);//+ Math.cos(sinusInput) * 100);
        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        float deltaTime = Gdx.graphics.getDeltaTime();


        if (timeSinceLastFrameUpdate >= 1.0f) {
            fps = Gdx.graphics.getFramesPerSecond();
            frameCounter = 0;
            timeSinceLastFrameUpdate = 0;
        }
        // Update timer
        elapsedTime += delta;
        if (elapsedTime >= totalTime) {
            // For now, let's just reset the timer to 0
            elapsedTime = 0;
            game.goToGameOver();
        }

        batch.begin();
        game.getSpriteBatch().begin(); // Important to call this before drawing anything
        // Update character logic
        player.update(sinusInput);
        if (world.getPropertiesMapForFileChooser()!=null){
            world.renderForFileChooser(delta);
        }
        // Render the text
        //font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);
        world.render(sinusInput);
        // Render characters
        player.render(sinusInput);
        //enemy.render(batch);
        batch.end();
        game.getSpriteBatch().end(); // Important to call this after drawing everything

        // render HUD
        hud.renderTimer(totalTime - elapsedTime);
        hud.renderFps(fps);
        hud.renderkeysMessage(keys);
        hud.renderLivesMessage(lives);

        // Check for collision with traps or enemies in the world
        if (player.checkCollisionWithTraps(player.getPlayerBounds())|| player.checkCollisionWithEnemies(player.getPlayerBounds())) {
            Music trapSound = Gdx.audio.newMusic(Gdx.files.internal("trapSound.wav"));
            trapSound.play();
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTrapActivationTime > 1000) {

                if (lives >= 1) {
                    lives--;
                    hud.renderLivesDecrease(lives);
                }
                lastTrapActivationTime = currentTime;
            }

        }
        if (lives == 0) {
            backgroundMusic.stop();
            Sound winMusic = Gdx.audio.newSound(Gdx.files.internal("assets/WinMusic.wav"));
            game.goToGameOver();
        }




        // Check for collision with keys in the world
        if (player.checkCollisionWithKey(player.getPlayerBounds())){
            keys++;
            hud.renderKeysIncrease(keys);
            Music keySound = Gdx.audio.newMusic(Gdx.files.internal("KeyMusic.wav"));
            keySound.play();


        }

        if (player.checkCollisionWithExit(player.getPlayerBounds()) && keys != 0){

            backgroundMusic.stop();
            game.goToWin();
            winMusic.play();
            winMusic.setLooping(1,true);
        }

        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        game.getSpriteBatch().setProjectionMatrix(camera.combined);


    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {


    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        enemy.dispose();

    }
    // Additional methods and logic can be added as needed for the game screen
    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
