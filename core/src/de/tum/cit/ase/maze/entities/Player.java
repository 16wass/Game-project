package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.HUD;
import de.tum.cit.ase.maze.MazeRunnerGame;
import de.tum.cit.ase.maze.Screens.WinScreen;

import java.util.HashMap;
import java.util.Map;

public class Player extends MovableCharacter {

    public MazeRunnerGame game;
    private HUD hud;
    private Vector2 previousPosition;

    private World world;
    private Rectangle boundingBox; // Rectangle to represent the player's bounding box
    private Rectangle playerBounds;

    private boolean isMoving = false;  // New variable to track movement state
    private HashMap<String, String> propertiesMap;  // Add this field



    public Player(float x, float y, SpriteBatch batch, World world,MazeRunnerGame game,HUD hud) {
        super(x, y, "assets/character.png", 200, batch);
        this.world = world;
        this.game = game;
        this.hud = hud;
        this.playerBounds = new Rectangle(x, y, 32, 40);
        this.hud = new HUD(game,spriteBatch, game.getSkin(),this,0,1);
        this.world = world;
        this.propertiesMap = world.getPropertiesMap();  // Initialize propertiesMap
    }

    @Override
    public void render(float deltaTime) {
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            spriteBatch.draw(characterDownAnimation.getKeyFrame(deltaTime, false), position.x, position.y, 16, 32);
        }

    }

    @Override
    public void handleActor(float deltaTime) {
        float moveSpeed = speed * Gdx.graphics.getDeltaTime();
        float newX = position.x;
        float newY = position.y;

        // Check for movement keys
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX -= moveSpeed;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX += moveSpeed;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            newY += moveSpeed;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            newY -= moveSpeed;
            isMoving = true;
        }

        // Update animation based on movement state
        updateAnimation(deltaTime);

        // Create a rectangle representing the new player position

        Rectangle newPlayerBounds = new Rectangle(newX, newY, 16, 20);

        playerBounds.setPosition(newX, newY);

        // Check for collision with walls in the world
        if (!checkCollisionWithWalls(newPlayerBounds)) {
            position.x = newX;
            position.y = newY;
        } else {
            isMoving = false;  // Stop animation if there is a collision
        }

        if (checkCollisionWithKey(newPlayerBounds)) {
            int i1 = hud.getKeys();
            i1++;
            for (int i = 0; i < i1; i++) {
                spriteBatch.draw(
                         new  Texture("key.png"),
                        i*100,
                        Gdx.graphics.getHeight() - hud.getKeysLabel().getHeight() - 110,
                        100,
                        100

                );
            }

        }



    }
    private void updateAnimation(float deltaTime) {
        if (isMoving) {
            // Choose the appropriate animation based on the direction of movement
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                spriteBatch.draw(characterLeftAnimation.getKeyFrame(deltaTime, true), position.x, position.y, 16, 32);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                spriteBatch.draw(characterRightAnimation.getKeyFrame(deltaTime, true), position.x, position.y, 16, 32);
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                spriteBatch.draw(characterUpAnimation.getKeyFrame(deltaTime, true), position.x, position.y, 16, 32);
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                spriteBatch.draw(characterDownAnimation.getKeyFrame(deltaTime, true), position.x, position.y, 16, 32);
            }
        } else {
            // Use a default frame when not moving
            spriteBatch.draw(characterDownAnimation.getKeyFrame(deltaTime, true), position.x, position.y, 16, 32);
        }
    }
    public boolean checkCollisionWithWalls(Rectangle playerBounds) {
        // Iterates over the walls in the world and check for collision
        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
            int entryValue = Integer.parseInt(entry.getValue());
            if (entryValue == 0) { // Wall
                String[] splitted = entry.getKey().split(",");
                float wallX = Float.parseFloat(splitted[0]) * 32;
                float wallY = Float.parseFloat(splitted[1]) * 32;
                Rectangle wallBounds = new Rectangle(wallX, wallY, 20, 20);

                if (playerBounds.overlaps(wallBounds)) {
                    return true; // Collision detected
                }
            }
        }
        return false; // No collision detected
    }
    public boolean checkCollisionWithKey(Rectangle playerBounds) {
        // Iterates over the keys in the world and check for collision
        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
            int entryValue = Integer.parseInt(entry.getValue());
            if (entryValue == 5) { // Key
                String[] splitted = entry.getKey().split(",");
                float keyX = Float.parseFloat(splitted[0]) * 32;
                float keyY = Float.parseFloat(splitted[1]) * 32;
                Rectangle keyBounds = new Rectangle(keyX, keyY, 20, 20);
                if (playerBounds.overlaps(keyBounds)) {
                    propertiesMap.remove(entry.getKey());
                    return true; // Collision detected
                }
            }
        }
        return false; // No collision detected with keys
    }
    public boolean checkCollisionWithEnemies(Rectangle playerBounds){
        for (Map.Entry<String, String> entry : world.findEnemies().entrySet()) {
            String enemyCoordinates = entry.getKey();
            String[] coordinates = enemyCoordinates.split(",");
            float enemyX = Float.parseFloat(coordinates[1]) * 32; // Adjust based on your coordinate system
            float enemyY = Float.parseFloat(coordinates[0]) * 32; // Adjust based on your coordinate system

            Rectangle enemyBounds = new Rectangle(enemyX, enemyY,20,20); // Adjust width and height

            if (playerBounds.overlaps(enemyBounds)) {
                return true; // Collision detected with an enemy
            }
        }
        return false; // No collision detected with enemies
    }

    public boolean checkCollisionWithExit(Rectangle playerBounds) {
        // Iterates over the exit in the world and check for collision
        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
            int entryValue = Integer.parseInt(entry.getValue());
            if (entryValue == 2) { // Exit
                String[] splitted = entry.getKey().split(",");
                float exitX = Float.parseFloat(splitted[0]) * 32;
                float exitY = Float.parseFloat(splitted[1]) * 32;
                Rectangle exitBounds = new Rectangle(exitX, exitY, 32, 32);

                if (playerBounds.overlaps(exitBounds)) {

                    return true; // Collision detected
                }
            }
        }
        return false; // No collision detected with exit
    }
    public boolean checkCollisionWithTraps(Rectangle playerBounds) {
        // Iterates over the traps in the world and checks for collision
        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
            int entryValue = Integer.parseInt(entry.getValue());
            if (entryValue == 3) { // Trap
                String[] splitted = entry.getKey().split(",");
                float trapX = Float.parseFloat(splitted[0]) * 32;
                float trapY = Float.parseFloat(splitted[1]) * 32;
                Rectangle trapBounds = new Rectangle(trapX, trapY, 20, 20);

                if (playerBounds.overlaps(trapBounds)) {
                    return true; // Collision detected with traps
                }
            }
        }
        return false; // No collision detected with traps
    }
    public Vector2 getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Vector2 previousPosition) {
        this.previousPosition = previousPosition;
    }

    @Override
    protected void loadCharacterAnimation() {
        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFramesDown = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesRight = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesUp = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesLeft = new Array<>(TextureRegion.class);


        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkFramesDown.add(new TextureRegion(this.texture, col * frameWidth, 0, frameWidth, frameHeight));
        }
        characterDownAnimation = new Animation<>(0.1f, walkFramesDown);

        for (int col = 0; col < animationFrames; col++) {
            walkFramesRight.add(new TextureRegion(this.texture, col * frameWidth, 32, frameWidth, frameHeight));
        }
        characterRightAnimation = new Animation<>(0.1f, walkFramesRight);

        for (int col = 0; col < animationFrames; col++) {
            walkFramesUp.add(new TextureRegion(this.texture, col * frameWidth, 64, frameWidth, frameHeight));
        }
        characterUpAnimation = new Animation<>(0.1f, walkFramesUp);

        for (int col = 0; col < animationFrames; col++) {
            walkFramesLeft.add(new TextureRegion(this.texture, col * frameWidth, 96, frameWidth, frameHeight));
        }
        characterLeftAnimation = new Animation<>(0.1f, walkFramesLeft);
    }

    public Rectangle getPlayerBounds() {
        return playerBounds;
    }

    public HashMap<String, String> getPropertiesMap() {
        return propertiesMap;
    }
}
