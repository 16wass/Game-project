package de.tum.cit.ase.maze;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.tum.cit.ase.maze.entities.Enemy;
import de.tum.cit.ase.maze.entities.MovableCharacter;
import de.tum.cit.ase.maze.entities.Player;

import java.util.Map;

public class HUD {

    private SpriteBatch batch;
    private Label keysLabel;
    private Label livesLabel;
    private Label fpsLabel;
    private Label messageLabel;
    private int keys = 0;
    private int lives = 1;
    private MazeRunnerGame game;
    private GameScreen gameScreen;
    private Player player;
    private BitmapFont font;

    public HUD(MazeRunnerGame game,SpriteBatch batch, Skin skin, Player player, int keys, int lives) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.batch = batch;
        this.keys = keys;
        this.lives = lives;
        this.player = player;
        this.keysLabel = new Label("", skin);
        this.messageLabel = new Label("", skin);
        this.livesLabel = new Label("", skin);
        this.fpsLabel = new Label("", skin);
        this.font = skin.getFont("font");
        initialize();
    }

    private void initialize() {
        keysLabel.setColor(Color.WHITE);
        messageLabel.setColor(Color.WHITE);
        fpsLabel.setColor(Color.WHITE);
    }

    public void render(int keys, int lives, float timerValue, int fps) {
        // Render frames per second

        batch.begin();

        batch.end();
    }
    public void renderTimer(float timerValue) {
        batch.begin();
        // Render timer
        font.draw(
                batch,
                "Time: " + (int) timerValue,
                Gdx.graphics.getWidth() - 150,
                Gdx.graphics.getHeight() - 20
        );
        batch.end();
    }
    public void renderFps(int fps) {
        batch.begin();
        font.draw(
                batch,
                "FPS: " + fps,
                Gdx.graphics.getWidth() - 150,
                Gdx.graphics.getHeight() - 60
        );
        batch.end();
    }
    public void renderkeysMessage(int keys) {
        batch.begin();
        keysLabel.setText("Keys Collected: " + keys);
        keysLabel.draw(batch, 1);
        keysLabel.setPosition(10, Gdx.graphics.getHeight() - keysLabel.getHeight() - 20);
        batch.end();
    }

    public void renderKeysIncrease(int keys){
        batch.begin();
//        keyMusic.play();
        keys++;
        keysLabel.setText("Keys Collected: " + keys);
        keysLabel.draw(batch, 1);
        keysLabel.setPosition(10, Gdx.graphics.getHeight() - keysLabel.getHeight() - 20);
        int i1 = keys;
        for (int i = 0; i < i1; i++) {
            batch.draw(
                    new Texture("key.png"),
                    i * 100,
                    Gdx.graphics.getHeight() - keysLabel.getHeight() - 110,
                    100,
                    100

            );
        }
        batch.end();

    }
    public void renderLivesMessage(int lives) {
        batch.begin();
//        liveMusic.play();
        livesLabel.setText("Lives: " + lives);
        livesLabel.draw(batch, 1);
        livesLabel.setPosition(10, Gdx.graphics.getHeight() - keysLabel.getHeight() - 150);
        for (int j = 0; j < lives; j++) {
            batch.draw(
                    new Texture("assets/3 lifes.png"),
                    200 + j * 60,
                    Gdx.graphics.getHeight() - livesLabel.getHeight() - 200,
                    100,
                    100);
        }
        batch.end();

    }
    public void renderLivesDecrease(int lives) {
        batch.begin();
        livesLabel.setText("Lives: " + lives);
        livesLabel.draw(batch, 1);
        livesLabel.setPosition(10, Gdx.graphics.getHeight() - keysLabel.getHeight() - 150);
        int LivesUpdated = lives;
        for (int j = 0; j < LivesUpdated; j++) {
            batch.draw(
                    new Texture("assets/3 lifes.png"),
                    200 + j * 60,
                    Gdx.graphics.getHeight() - livesLabel.getHeight() - 200,
                    100,
                    100);
        }
        batch.end();

    }
    public Label getKeysLabel() {
        return keysLabel;
    }

    public Label getLivesLabel() {
        return livesLabel;
    }

    public int getKeys() {
        return keysLabel.getText().toString().equals("") ? 0 : Integer.parseInt(keysLabel.getText().toString().split(" ")[2]);
    }

    public int getLIVES() {
        return livesLabel.getText().toString().equals("") ? 0 : Integer.parseInt(livesLabel.getText().toString().split(" ")[1]);
    }

}

