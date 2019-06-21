package com.becksm64.coingetter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Coin {

    private Texture coinImage;
    private Vector3 position;
    private Vector3 velocity;

    public Coin(int x, int y, int velX, int velY) {

        coinImage = new Texture("sprites/coin.png");
        position = new Vector3(x, y, 0);
        velocity = new Vector3(velX, velY, 0);
    }

    public Texture getCoinImage() {
        return coinImage;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position = new Vector3(x, y, 0);
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(int velX, int velY) {
        this.velocity = new Vector3(velX, velY, 0);
    }
}
