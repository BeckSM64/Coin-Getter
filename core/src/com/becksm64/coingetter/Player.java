package com.becksm64.coingetter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player {

    public static final float SIZE = Gdx.graphics.getWidth() / 15.0f;//Will be used for width and height because player is a square
    public static final Vector2 tmp = new Vector2();//Vector to represent distance between player position and current touch position

    private Texture playerImage;
    private Vector3 position;
    private Rectangle bounds;
    private int coinsCollected;
    private int health;
    private int score;
    private Vector3 velocity;
    private float speed;
    private boolean isInvincible;
    private float invincibleTime;
    private boolean hasRunningShoes;

    public Player(float x, float y) {

        playerImage = new Texture("sprites/player.png");
        position = new Vector3(x, y, 0);
        bounds = new Rectangle(x, y, SIZE, SIZE);
        coinsCollected = 0;//Starts at 0 because player starts with no coins
        health = 100;
        score = 0;
        velocity = new Vector3(0, 0, 0);
        speed = 400f * Gdx.graphics.getDensity();//Speed calculated based on screen size
        isInvincible = false;
        invincibleTime = 0f;//Player is invincible for 2 seconds after hit
        hasRunningShoes = false;
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
        position.add(velocity.x, velocity.y, 0);
        constrainPlayer();
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position = new Vector3(x, y, 0);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
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

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(float velX, float velY) {
        this.velocity = new Vector3(velX, velY, 0);
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

    public boolean hasFullHealth() {
        return health == 100;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    public float getInvincibleTime() {
        return invincibleTime;
    }

    public void setInvincibleTime(float invincibleTime) {
        this.invincibleTime = invincibleTime;
    }

    public boolean hasRunningShoes() {
        return hasRunningShoes;
    }

    public void setHasRunningShoes(boolean hasRunningShoes) {
        this.hasRunningShoes = hasRunningShoes;
    }

    public void dispose() {
        playerImage.dispose();
    }
}
