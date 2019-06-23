package com.becksm64.coingetter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Player {

    public static final float SIZE = Gdx.graphics.getWidth() / 15.0f;//Will be used for width and height because player is a square

    private Texture playerImage;
    private Vector3 position;
    private Rectangle bounds;
    private int coinsCollected;

    public Player(float x, float y) {

        playerImage = new Texture("sprites/player.png");
        position = new Vector3(x, y, 0);
        bounds = new Rectangle(x, y, SIZE, SIZE);
        coinsCollected = 0;//Starts at 0 because player starts with no coins
    }

    public void update() {
        setBounds(position.x, position.y);
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

    public void dispose() {
        playerImage.dispose();
    }
}
