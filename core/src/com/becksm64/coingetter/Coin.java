package com.becksm64.coingetter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Coin {

    public static final float WIDTH = Gdx.graphics.getWidth() / 10.0f;
    public static final float HEIGHT = Gdx.graphics.getHeight() / 6.0f;

    private Texture coinImage;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;

    public Coin(int x, int y, int velX, int velY) {

        coinImage = new Texture("sprites/coin.png");
        position = new Vector3(x, y, 0);
        velocity = new Vector3(velX, velY, 0);
        bounds = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void update() {
        position.add(velocity.x, velocity.y, 0);
        setBounds(position.x, position.y);
        constrainCoin();
    }

    public void constrainCoin() {

        if(position.x > Gdx.graphics.getWidth() - WIDTH || position.x < 0) {
            velocity.x *= -1;
        } else if(position.y > Gdx.graphics.getHeight() - HEIGHT || position.y < 0) {
            velocity.y *= -1;
        }
    }

    private void setBounds(float x, float y) {
        bounds.setPosition(x, y);
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
