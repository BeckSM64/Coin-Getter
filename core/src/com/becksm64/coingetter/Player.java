package com.becksm64.coingetter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player {

    public static final float SIZE = Gdx.graphics.getWidth() / 15.0f;//Will be used for width and height because player is a square
    public static final float SPEED = 400f * Gdx.graphics.getDensity();//Speed calculated based on screen size
    public static final Vector2 tmp = new Vector2();//Vector to represent distance between player position and current touch position

    private Texture playerImage;
    private Vector3 position;
    private Rectangle bounds;
    private int coinsCollected;
    private int health;
    private int score;

    public Player(float x, float y) {

        playerImage = new Texture("sprites/player.png");
        position = new Vector3(x, y, 0);
        bounds = new Rectangle(x, y, SIZE, SIZE);
        coinsCollected = 0;//Starts at 0 because player starts with no coins
        health = 100;
        score = 0;
    }

    /*
     * Keep player within the screen bounds
     */
    private void constrainPlayer() {

        if(position.x >= Gdx.graphics.getWidth() - SIZE)
            position.x = Gdx.graphics.getWidth() - SIZE;
        else if(position.x <= 0)
            position.x = 0;

        if(position.y >= Gdx.graphics.getHeight() - SIZE)
            position.y = Gdx.graphics.getHeight() - SIZE;
        else if(position.y <= 0)
            position.y = 0;
    }

    public void update() {
        setBounds(position.x, position.y);
        constrainPlayer();
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position = new Vector3(x, y, 0);
    }

    public Texture getPlayerImage() {
        return playerImage;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private void setBounds(float x, float y) {
        bounds.setPosition(x, y);
    }

    public int getCoinsCollected() {
        return coinsCollected;
    }

    public void setCoinsCollected(int coinsCollected) {
        this.coinsCollected = coinsCollected;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void dispose() {
        playerImage.dispose();
    }
}
