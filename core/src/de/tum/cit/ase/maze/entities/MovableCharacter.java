/*
package de.tum.cit.ase.maze.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MovableCharacter {
    protected Texture texture;
    protected Vector2 position;
    protected float speed;

    public MovableCharacter(float x, float y, String imagePath, float speed) {
        this.position = new Vector2(x, y);
        this.texture = new Texture(imagePath);
        this.speed = speed;
    }

    public void update(float deltaTime) {
        handleInput();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    private void handleInput() {
        float moveSpeed = speed * Gdx.graphics.getDeltaTime();

        // Keyboard input for movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.x += moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.y += moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.y -= moveSpeed;
        }
    }
}
*/

package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.MazeRunnerGame;

public abstract class MovableCharacter {
    protected Texture texture;
    protected Vector2 position;
    protected float speed;
    protected Animation<TextureRegion> characterDownAnimation;
    protected Animation<TextureRegion> characterRightAnimation;
    protected Animation<TextureRegion> characterUpAnimation;
    protected Animation<TextureRegion> characterLeftAnimation;
    protected Animation<TextureRegion> enemyDownAnimation;
    protected Animation<TextureRegion> enemyLeftAnimation;
    protected Animation<TextureRegion> enemyRightAnimation;
    protected Animation<TextureRegion> enemyUpAnimation;
    protected SpriteBatch spriteBatch;

    public MovableCharacter(float x, float y, String imagePath, float speed, SpriteBatch batch) {
        this.position = new Vector2(x, y);
        this.texture = new Texture(Gdx.files.internal(imagePath));
        this.speed = speed;
        this.spriteBatch = batch;
        loadCharacterAnimation();
    }

    public void update(float deltaTime) {
        handleActor(deltaTime);
    }
    abstract void render(float deltaTime);

    public void dispose() {
        texture.dispose();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    abstract void handleActor(float deltaTime);

    abstract void loadCharacterAnimation();


}

