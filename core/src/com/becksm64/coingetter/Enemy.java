package com.becksm64.coingetter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Enemy {

    public static final float SIZE = Gdx.graphics.getWidth() / 15.0f;

    private Texture enemyImage;
    private Rectangle bounds;
    private Vector3 position;
    private Vector3 velocity;

    public Enemy(int x, int y) {

        enemyImage = new Texture("sprites/enemy.png");
        position = new Vector3(x, y, 0);
        bounds = new Rectangle(x, y, SIZE, SIZE);
        velocity = new Vector3(5 * Gdx.graphics.getDensity(), 5 * Gdx.graphics.getDensity(), 0);
    }

    public void update() {

        position.add(velocity.x, velocity.y, 0);
        setBounds(position.x, position.y);
        constrainEnemy();
    }

    private void constrainEnemy() {

        if(position.x > Gdx.graphics.getWidth() - SIZE || position.x < 0) {
            velocity.x *= -1;
        } else if(position.y > Gdx.graphics.getHeight() - SIZE || position.y < 0) {
            velocity.y *= -1;
        }
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position = new Vector3(x, y, 0);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private void setBounds(float x, float y) {
        bounds.setPosition(x, y);
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(float velX, float velY) {
        this.velocity = new Vector3(velX, velY, 0);
    }

    public Texture getEnemyImage() {
        return enemyImage;
    }

    public void dispose() {
        enemyImage.dispose();
    }
}
