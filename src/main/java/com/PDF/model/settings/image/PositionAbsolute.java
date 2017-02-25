package com.PDF.model.settings.image;

/**
 * Created by martanase on 2/24/2017.
 */
public class PositionAbsolute {

    private float x;
    private float y;

    public PositionAbsolute() {
    }

    public PositionAbsolute(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
