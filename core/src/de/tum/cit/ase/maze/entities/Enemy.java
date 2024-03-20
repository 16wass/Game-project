package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Enemy extends MovableCharacter {
    private float autoMoveTimer;
    private float autoMoveDuration;
    private Array<Vector2> waypoints;
    private int currentWaypointIndex;
    private float sinusInput;
    private float timerCounterLock;
    private int i;
    private World world;
    private String direction;
    private HashMap<String, String> propertiesMap;  // Add this field
    private Rectangle enemyBounds;

    private TextureRegion enemyDirectionAnimation;
    public Enemy(float x, float y, SpriteBatch batch, World world) {
        super(x, y, "assets/mobs.png", 0.1f, batch);
        waypoints = new Array<>();
        currentWaypointIndex = 0;
        sinusInput = MathUtils.random(); // Initialize with a random value
        this.timerCounterLock = 0;
        this.world = world;
        String[] movementPositions = new String[]{"left", "right", "up", "down"};
        int enemyMovementInt = i % 4;
        this.direction = movementPositions[enemyMovementInt];
        this.enemyBounds = new Rectangle(x, y, 20, 20);
        this.propertiesMap = world.getPropertiesMap();  // Initialize propertiesMap
    }
    private void setAction(float deltaTime) {
        timerCounterLock++;
        if (timerCounterLock == 240) {
            Random random = new Random();
            i = random.nextInt(200) + 1; //picks a random number from 1 to 100
            if (i <= 50) {
                direction = "up";
                enemyDirectionAnimation = enemyUpAnimation.getKeyFrame(deltaTime, true);
            }
            if (i > 50 && i <= 100) {
                direction = "down";
                enemyDirectionAnimation = enemyDownAnimation.getKeyFrame(deltaTime, true);
            }
            if (i > 100 && i <= 150) {
                direction = "left";
                enemyDirectionAnimation = enemyLeftAnimation.getKeyFrame(deltaTime, true);
            }
            if (i > 150 && i <= 200) {
                direction = "right";
                enemyDirectionAnimation = enemyRightAnimation.getKeyFrame(deltaTime, true);
            }
            timerCounterLock = 0;
        }
    }
    //make everything should be 16x16
    @Override
    public void render(float deltaTime) {
        spriteBatch.draw(enemyDownAnimation.getKeyFrame(deltaTime, false), position.x, position.y, 32, 32);
    }
    public boolean checkCollisionWithWalls(Rectangle enemyBounds) {
        // Iterates over the walls in the world and check for collision
        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
            int entryValue = Integer.parseInt(entry.getValue());
            if (entryValue == 0 || entryValue == 2 || entryValue == 1) { // Wall
                String[] splitted = entry.getKey().split(",");
                float wallX = Float.parseFloat(splitted[0]) * 32;
                float wallY = Float.parseFloat(splitted[1]) * 32;
                Rectangle wallBounds = new Rectangle(wallX, wallY, 30, 30);
                if (enemyBounds.overlaps(wallBounds)) {
                    return true; // Collision detected
                }
            }
        }
        return false; // No collision detected
    }

    /*public void update(float deltaTime, String enemyName) {
        float moveSpeed = speed;
        float newX = position.x;
        float newY = position.y;
        String[] movementPositions = new String[]{"left", "right", "up", "down"};
        int rnd = new Random().nextInt(movementPositions.length);
        String pos = movementPositions[rnd];
        // Check for movement keys
        if (enemyName.contains("left")) {
            spriteBatch.draw(enemyLeftAnimation.getKeyFrame(deltaTime, true), position.x, position.y, 32, 32);
            position.x -= moveSpeed;
        }
        if (enemyName.contains("right")) {
            spriteBatch.draw(enemyRightAnimation.getKeyFrame(deltaTime, true), position.x, position.y, 32, 32);
            position.x += moveSpeed;
        }
        if (enemyName.contains("up")) {
            spriteBatch.draw(enemyUpAnimation.getKeyFrame(deltaTime, true), position.x, position.y, 32, 32);
            position.y += moveSpeed;
        }
        if (enemyName.contains("down")) {
            spriteBatch.draw(enemyDownAnimation.getKeyFrame(deltaTime, true), position.x, position.y, 32, 32);
            position.y -= moveSpeed;
        }
    }*/
    @Override
    void handleActor(float deltaTime) {
        setAction(deltaTime);
        // Create a rectangle representing the new player position
        // Rectangle newEnemyBounds = new Rectangle(position.x, position.y, 20, 20);
        enemyBounds.setPosition(position.x, position.y);
        switch (direction) {
            case "up":
                if (!checkCollisionWithWalls(enemyBounds)) {
                    position.y += speed;
                    enemyDirectionAnimation = enemyUpAnimation.getKeyFrame(deltaTime, true);
                } else {
                    direction = "down";
                    enemyDirectionAnimation = enemyDownAnimation.getKeyFrame(deltaTime, true);
                    position.y -= speed;
                }
                break;
            case "down":
                if (!checkCollisionWithWalls(enemyBounds)) {
                    position.y -= speed;
                    enemyDirectionAnimation = enemyDownAnimation.getKeyFrame(deltaTime, true);
                } else {
                    direction = "up";
                    enemyDirectionAnimation = enemyUpAnimation.getKeyFrame(deltaTime, true);
                    position.y += speed;
                }
                break;
            case "left":
                if (!checkCollisionWithWalls(enemyBounds)) {
                    position.x -= speed;
                    enemyDirectionAnimation = enemyLeftAnimation.getKeyFrame(deltaTime, true);
                } else {
                    direction = "right";
                    enemyDirectionAnimation = enemyRightAnimation.getKeyFrame(deltaTime, true);
                    position.x += speed;
                }
                break;
            case "right":
                if (!checkCollisionWithWalls(enemyBounds)) {
                    position.x += speed;
                    enemyDirectionAnimation = enemyRightAnimation.getKeyFrame(deltaTime, true);
                } else {
                    direction = "left";
                    enemyDirectionAnimation = enemyLeftAnimation.getKeyFrame(deltaTime, true);
                    position.x -= speed;
                }
                break;
        }
        spriteBatch.draw(enemyDirectionAnimation, position.x, position.y, 32, 32);

        /*this.speed = 1f;
        float newX = position.x;
        float newY = position.y;
        timerCounterLock += Gdx.graphics.getDeltaTime();
        //System.out.println(timerCounterLock);
        Random random = new Random();
        i = random.nextInt(100) + 1; //picks a random number from 1 to 100
        if (i <= 25) {
            newY -= speed;
            position.y = newY;
            spriteBatch.draw(enemyDownAnimation.getKeyFrame(deltaTime, false), position.x, position.y, 32, 32);
        }
        spriteBatch.draw(enemyLeftAnimation.getKeyFrame(deltaTime, false), position.x, position.y, 32, 32);
        if (i > 25 && i <= 50) {
            newX += speed;
            position.x = newX;
        }
        spriteBatch.draw(enemyRightAnimation.getKeyFrame(deltaTime, false), position.x, position.y, 32, 32);
        if (i > 50 && i <= 75) {
            newX += speed;
            position.x = newX;
        }
        spriteBatch.draw(enemyUpAnimation.getKeyFrame(deltaTime, false), position.x, position.y, 32, 32);
        if (i > 75 && i <= 100) {
            newY += speed;
            position.y = newY;
        }*/
    }

    public Rectangle getBounds() {
        return this.enemyBounds;
    }

    @Override
    void loadCharacterAnimation() {

        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 3;

        Array<TextureRegion> walkFramesEnemyDown = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesEnemyLeft = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesEnemyRight = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesEnemyUp = new Array<>(TextureRegion.class);

        for (int col = 0; col < animationFrames; col++) {
            walkFramesEnemyDown.add(new TextureRegion(this.texture, 9 * frameWidth + col * frameWidth, 0, frameWidth, frameHeight));
        }
        enemyDownAnimation = new Animation<>(0.4f, walkFramesEnemyDown);
        for (int col = 0; col < animationFrames; col++) {
            walkFramesEnemyLeft.add(new TextureRegion(this.texture, 9 * frameWidth + col * frameWidth, 16, frameWidth, frameHeight));
        }
        enemyLeftAnimation = new Animation<>(0.4f, walkFramesEnemyLeft);
        for (int col = 0; col < animationFrames; col++) {
            walkFramesEnemyRight.add(new TextureRegion(this.texture, 9 * frameWidth + col * frameWidth, 32, frameWidth, frameHeight));
        }
        enemyRightAnimation = new Animation<>(0.4f, walkFramesEnemyRight);
        for (int col = 0; col < animationFrames; col++) {
            walkFramesEnemyUp.add(new TextureRegion(this.texture, 9 * frameWidth + col * frameWidth, 48, frameWidth, frameHeight));
        }
        enemyUpAnimation = new Animation<>(0.4f, walkFramesEnemyUp);
    }
}

