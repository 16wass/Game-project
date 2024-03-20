package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.GameScreen;
import de.tum.cit.ase.maze.MazeRunnerGame;
import de.tum.cit.ase.maze.Screens.LevelScreen;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class World {
    protected SpriteBatch spriteBatch;
    protected Vector2 position;
    protected Texture texture;
    protected Texture texture1;
    protected Texture textureKey;
    protected Enemy enemy;
    private Texture pathTexture;
    private LevelScreen levelScreen;

    private float entryPointX;
    private float entryPointY;
    private MazeRunnerGame game;
    private HashMap<String, Enemy> enemiesHashmap;

    public World(SpriteBatch batch) {
        this.levelScreen = new LevelScreen(game);
        this.spriteBatch = batch;
        this.texture = new Texture(Gdx.files.internal("assets/basictiles.png"));
        this.texture1 = new Texture(Gdx.files.internal("assets/mobs.png"));
        this.textureKey = new Texture(Gdx.files.internal("assets/key1.png"));
        this.pathTexture = new Texture(Gdx.files.internal("assets/path1.png"));
        this.enemiesHashmap = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, String> entry : findEnemies().entrySet()) {
            String entryKey = entry.getKey();
            String[] splitted = entryKey.split(",");
            float y = Float.parseFloat(splitted[0]);
            float x = Float.parseFloat(splitted[1]);
            enemy = new Enemy(x * 32, y * 32, spriteBatch, this);
            this.enemiesHashmap.put("Enemy" + i + entry.getValue(), enemy);
            i++;
        }
        findEntryPoint();
    }

    public float getEntryPointX() {
        return entryPointX;
    }

    public void setEntryPointX(float entryPointX) {
        this.entryPointX = entryPointX;
    }

    public float getEntryPointY() {
        return entryPointY;
    }

    public void setEntryPointY(float entryPointY) {
        this.entryPointY = entryPointY;
    }

    public HashMap<String, String> getPropertiesMapForFileChooser() {
        Properties worldProperties = new Properties();
        HashMap<String, String> loadingProperties = new HashMap<>();
        try (FileInputStream fileInputStream = new FileInputStream(game.chooseLevel())) {
            worldProperties.load(fileInputStream);
            Map step1 = worldProperties;
            Map<String, String> step2 = (Map<String, String>) step1;
            loadingProperties = new HashMap<>(step2);
        } catch (Exception exception) {
        }
        return loadingProperties;
    }

    public HashMap<String, String> getPropertiesMap() {
        Properties worldProperties = new Properties();
        HashMap<String, String> loadingProperties = new HashMap<>();
        try (FileInputStream fileInputStream = new FileInputStream(levelScreen.getPropertiesFilePath())) {
            worldProperties.load(fileInputStream);
            Map step1 = worldProperties;
            Map<String, String> step2 = (Map<String, String>) step1;
            loadingProperties = new HashMap<>(step2);
        } catch (Exception exception) {
        }
        return loadingProperties;
    }

    private void findEntryPoint() {
        HashMap<String, String> propertiesForWorld = getPropertiesMap();
        for (Map.Entry<String, String> entry : propertiesForWorld.entrySet()) {
            String entryKey = entry.getKey();
            int entryValue = Integer.parseInt(entry.getValue());
            String[] splitted = entryKey.split(",");
            if (isInt(splitted[0]) && isInt(splitted[1])) {
                int x = Integer.parseInt(splitted[0]);
                int y = Integer.parseInt(splitted[1]);
                if (entryValue == 1) {
                    this.setEntryPointX(x * 32);
                    this.setEntryPointY(y * 32);
                }
            }
        }
    }

    public void update(float deltaTime) {
    }

    public void renderForFileChooser(float deltaTime) {
        HashMap<String, String> propertiesForWorld = getPropertiesMapForFileChooser();
        for (Map.Entry<String, String> entry : propertiesForWorld.entrySet()) {
            String entryKey = entry.getKey();
            int entryValue = Integer.parseInt(entry.getValue());
            String[] splitted = entryKey.split(",");
            int x = Integer.parseInt(splitted[0]);
            int y = Integer.parseInt(splitted[1]);
            switch (entryValue) {
                case 0:
                    TextureRegion wall = new TextureRegion(this.texture, 0, 0, 16, 16);
                    //System.out.println("X axis value " + x + " Y axis value " + y);
                    this.position = new Vector2(x, y);
                    spriteBatch.draw(wall, x * 32, y * 32, 32, 32);
                    break;
                case 1:
                    TextureRegion entryPoint = new TextureRegion(this.texture, 0, 112, 16, 16);
                    spriteBatch.draw(entryPoint, x * 32, y * 32, 32, 32);
                    break;
                case 2:
                    TextureRegion exitPoint = new TextureRegion(this.texture, 32, 96, 16, 16);
                    spriteBatch.draw(exitPoint, x * 32, y * 32, 32, 32);
                    break;
                case 3:
                    TextureRegion trap = new TextureRegion(this.texture, 32, 144, 16, 16);
                    spriteBatch.draw(trap, x * 32, y * 32, 32, 32);
                    break;
                case 4:
                    enemy = new Enemy(x * 32, y * 32, spriteBatch, this);
                    enemy.update(deltaTime);
                    enemy.render(deltaTime);
                    break;
                case 5:
                    TextureRegion key = new TextureRegion(this.textureKey, 0, 0, 50, 50);
                    spriteBatch.draw(key, x * 32, y * 32, 16, 16);
                    break;
                default:
                    // draw path
                    TextureRegion path = new TextureRegion(pathTexture, 32, 96, 16, 16);
                    spriteBatch.draw(path, x * 32, y * 32, 32, 32);

            }
        }
    }

    public ConcurrentHashMap<String, String> findEnemies() {
        HashMap<String, String> propertiesForWorld = getPropertiesMap();
        ConcurrentHashMap<String, String> enemiesMap = new ConcurrentHashMap<>();
        int i = 1;
        String[] movementPositions = new String[]{"left", "right", "up", "down"};
        for (Map.Entry<String, String> entry : propertiesForWorld.entrySet()) {
            String entryKey = entry.getKey();
            int entryValue = Integer.parseInt(entry.getValue());
            if (entryValue == 4) {
                int enemyMovementInt = i % 4;
                String enemyMovement = movementPositions[enemyMovementInt];
                enemiesMap.put(entryKey, enemyMovement);
            }
            i++;
        }
        return enemiesMap;
    }

    public void render(float deltaTime) {
        HashMap<String, String> propertiesForWorld = getPropertiesMap();
        for (Map.Entry<String, String> entry : propertiesForWorld.entrySet()) {
            String entryKey = entry.getKey();
            int entryValue = Integer.parseInt(entry.getValue());
            String[] splitted = entryKey.split(",");
            if (isInt(splitted[0]) && isInt(splitted[1])) {
                int x = Integer.parseInt(splitted[0]);
                int y = Integer.parseInt(splitted[1]);
                switch (entryValue) {
                    case 0:
                        TextureRegion wall = new TextureRegion(this.texture, 0, 0, 16, 16);
                        this.position = new Vector2(x, y);
                        spriteBatch.draw(wall, x * 32, y * 32, 32, 32);
                        break;
                    case 1:
                        TextureRegion entryPoint = new TextureRegion(this.texture, 0, 112, 16, 16);
                        spriteBatch.draw(entryPoint, x * 32, y * 32, 32, 32);
                        break;
                    case 2:
                        TextureRegion exitPoint = new TextureRegion(this.texture, 32, 96, 16, 16);
                        spriteBatch.draw(exitPoint, x * 32, y * 32, 32, 32);
                        break;
                    case 3:
                        TextureRegion trap = new TextureRegion(this.texture, 80, 64, 16, 16);
                        spriteBatch.draw(trap, x * 32, y * 32, 32, 32);
                        break;
                    case 4:
                        //enemy = new Enemy(x * 32, y * 32, spriteBatch, this);
                        //enemy.update(deltaTime);
                        //enemy.render(deltaTime);
                        renderEnemiesInWorld(deltaTime);
                        break;
                    case 5:
                        TextureRegion key = new TextureRegion(this.textureKey, 0, 0, 50, 50);
                        spriteBatch.draw(key, x * 32, y * 32, 16, 16);
                        break;
                    default:
                        // draw path
                        TextureRegion path = new TextureRegion(pathTexture, 32, 96, 16, 16);
                        spriteBatch.draw(path, x * 32, y * 32, 32, 32);
                }
            }
        }
    }
    private void renderEnemiesInWorld(float deltaTime) {
        for (Map.Entry<String, Enemy> entry : this.enemiesHashmap.entrySet()) {
            entry.getValue().update(deltaTime);
            //entry.getValue().render(deltaTime);
        }
    }

    private boolean isInt(String notInt) {
        try {
            Integer.parseInt(notInt);
        } catch (Exception exp) {
            return false;
        }
        return true;
    }

    public void getProperties() {
    }

    public Vector2 getPosition() {
        return position;
    }
}

//package de.tum.cit.ase.maze.entities;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector2;
//import de.tum.cit.ase.maze.GameScreen;
//import de.tum.cit.ase.maze.MazeRunnerGame;
//import de.tum.cit.ase.maze.Screens.LevelScreen;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//public class World {
//    protected SpriteBatch spriteBatch;
//    protected Vector2 position;
//    protected Texture texture;
//    protected Texture texture1;
//    protected Texture textureKey;
//    protected Enemy enemy;
//    private Texture pathTexture;
//    private LevelScreen levelScreen;
//
//    private float entryPointX;
//    private float entryPointY;
//    private MazeRunnerGame game;
//
//    public World(SpriteBatch batch) {
//        this.levelScreen = new LevelScreen(game);
//        this.spriteBatch = batch;
//
//        this.texture = new Texture(Gdx.files.internal("assets/basictiles.png"));
//        this.texture1 = new Texture(Gdx.files.internal("assets/mobs.png"));
//        this.textureKey = new Texture(Gdx.files.internal("assets/key1.png"));
//        this.pathTexture = new Texture(Gdx.files.internal("assets/path1.png"));
//        findEntryPoint();
//    }
//    public float getEntryPointX() {
//        return entryPointX;
//    }
//
//    public void setEntryPointX(float entryPointX) {
//        this.entryPointX = entryPointX;
//    }
//
//    public float getEntryPointY() {
//        return entryPointY;
//    }
//
//    public void setEntryPointY(float entryPointY) {
//        this.entryPointY = entryPointY;
//    }
//
//    public HashMap<String, String> getPropertiesMapForFileChooser() {
//        Properties worldProperties = new Properties();
//        HashMap<String, String> loadingProperties = new HashMap<>();
//        try (FileInputStream fileInputStream = new FileInputStream(game.chooseLevel())) {
//            worldProperties.load(fileInputStream);
//            Map step1 = worldProperties;
//            Map<String, String> step2 = (Map<String, String>) step1;
//            loadingProperties = new HashMap<>(step2);
//        } catch (Exception exception) {
//        }
//        return loadingProperties;
//    }
//    public HashMap<String, String> getPropertiesMap() {
//        Properties worldProperties = new Properties();
//        HashMap<String, String> loadingProperties = new HashMap<>();
//        try (FileInputStream fileInputStream = new FileInputStream(levelScreen.getPropertiesFilePath())) {
//            worldProperties.load(fileInputStream);
//            Map step1 = worldProperties;
//            Map<String, String> step2 = (Map<String, String>) step1;
//            loadingProperties = new HashMap<>(step2);
//        } catch (Exception exception) {
//        }
//        return loadingProperties;
//    }
//    public void findEntryPoint() {
//        HashMap<String, String> propertiesForWorld = getPropertiesMap();
//        for (Map.Entry<String, String> entry : propertiesForWorld.entrySet()) {
//            String entryKey = entry.getKey();
//            int entryValue = Integer.parseInt(entry.getValue());
//            String[] splitted = entryKey.split(",");
//            if (isInt(splitted[0]) && isInt(splitted[1])) {
//                int x = Integer.parseInt(splitted[0]);
//                int y = Integer.parseInt(splitted[1]);
//                if (entryValue == 1) {
//                    this.setEntryPointX(x * 32);
//                    this.setEntryPointY(y * 32);
//                }
//            }
//        }
//    }
//    public void update(float deltaTime) {
//    }
//    public void renderForFileChooser(float deltaTime) {
//
//
//        HashMap<String, String> propertiesForWorld = getPropertiesMapForFileChooser();
//        for (Map.Entry<String, String> entry : propertiesForWorld.entrySet()) {
//            //System.out.println("this is the Key" + entry.getKey());
//            //System.out.println("this is the value" + entry.getValue());
//            String entryKey = entry.getKey();
//            int entryValue = Integer.parseInt(entry.getValue());
//            String[] splitted = entryKey.split(",");
//            int x = Integer.parseInt(splitted[0]);
//            int y = Integer.parseInt(splitted[1]);
//            switch (entryValue) {
//                case 0:
//                    TextureRegion wall = new TextureRegion(this.texture, 0, 0, 16, 16);
//                    //System.out.println("X axis value " + x + " Y axis value " + y);
//                    this.position = new Vector2(x, y);
//                    spriteBatch.draw(wall, x * 32, y * 32, 32, 32);
//                    break;
//                case 1:
//                    TextureRegion entryPoint = new TextureRegion(this.texture, 0, 112, 16, 16);
//                    spriteBatch.draw(entryPoint, x * 32, y * 32, 32, 32);
//                    break;
//                case 2:
//                    TextureRegion exitPoint = new TextureRegion(this.texture, 32, 96, 16, 16);
//                    spriteBatch.draw(exitPoint, x * 32, y * 32, 32, 32);
//                    break;
//                case 3:
//                    TextureRegion trap = new TextureRegion(this.texture, 32, 112, 16, 16);
//                    spriteBatch.draw(trap, x * 32, y * 32, 32, 32);
//                    break;
//                case 4:
//                    enemy = new Enemy(x *32,y *32,spriteBatch);
//                    enemy.update(deltaTime);
//                    enemy.render(deltaTime);
//                    break;
//                case 5:
//                    TextureRegion key = new TextureRegion(this.textureKey, 0, 0, 50, 50);
//                    spriteBatch.draw(key, x * 32, y * 32, 16, 16);
//                    break;
//                default:
//                    // draw path
//                    TextureRegion path = new TextureRegion(pathTexture, 32, 96, 16, 16);
//                    spriteBatch.draw(path, x * 32, y * 32, 32, 32);
//
//
//            }
//        }
//    }
//
//    public void render(float deltaTime) {
//
//
//        HashMap<String, String> propertiesForWorld = getPropertiesMap();
//        for (Map.Entry<String, String> entry : propertiesForWorld.entrySet()) {
//            //System.out.println("this is the Key" + entry.getKey());
//            //System.out.println("this is the value" + entry.getValue());
//            String entryKey = entry.getKey();
//            int entryValue = Integer.parseInt(entry.getValue());
//            String[] splitted = entryKey.split(",");
//            if (isInt(splitted[0]) && isInt(splitted[1])) {
//                int x = Integer.parseInt(splitted[0]);
//                int y = Integer.parseInt(splitted[1]);
//                switch (entryValue) {
//                    case 0:
//                        TextureRegion wall = new TextureRegion(this.texture, 0, 0, 16, 16);
//                        //System.out.println("X axis value " + x + " Y axis value " + y);
//                        this.position = new Vector2(x, y);
//                        spriteBatch.draw(wall, x * 32, y * 32, 32, 32);
//                        break;
//                    case 1:
//                        TextureRegion entryPoint = new TextureRegion(this.texture, 0, 112, 16, 16);
//                        spriteBatch.draw(entryPoint, x * 32, y * 32, 32, 32);
//                        break;
//                    case 2:
//                        TextureRegion exitPoint = new TextureRegion(this.texture, 32, 96, 16, 16);
//                        spriteBatch.draw(exitPoint, x * 32, y * 32, 32, 32);
//                        break;
//                    case 3:
//                        TextureRegion trap = new TextureRegion(this.texture, 32, 112, 16, 16);
//                        spriteBatch.draw(trap, x * 32, y * 32, 32, 32);
//                        break;
//                    case 4:
//                        enemy = new Enemy(x *32,y *32,spriteBatch);
//                        enemy.update(deltaTime);
//                        enemy.render(deltaTime);
//                        break;
//                    case 5:
//                        TextureRegion key = new TextureRegion(this.textureKey, 0, 0, 50, 50);
//                        spriteBatch.draw(key, x * 32, y * 32, 16, 16);
//                        break;
//
//
//                    default:
//                        // draw path
//                        TextureRegion path = new TextureRegion(pathTexture, 32, 96, 16, 16);
//                        spriteBatch.draw(path, x * 32, y * 32, 32, 32);
//                }
//            }
//        }
//    }
//    private boolean isInt(String notInt) {
//        try {
//            Integer.parseInt(notInt);
//        } catch(Exception exp) {
//            return false;
//        }
//        return true;
//    }
//    public void getProperties() {
//    }
//    public Vector2 getPosition() {
//        return position;
//    }
//}
