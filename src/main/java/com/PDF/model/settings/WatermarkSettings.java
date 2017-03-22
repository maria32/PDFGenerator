package com.PDF.model.settings;

import com.PDF.utils.Position;

/**
 * Created by martanase on 3/6/2017.
 */
public class WatermarkSettings {

    private String size = "original";
    private Position position = Position.CENTER;
    private float opacity = 0.3f;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        if (opacity >= 0 && opacity <= 1) {
            this.opacity = opacity;
        }else{
            System.out.println("Error: opacity out of counce. Result: opacity not set. Present opacity value: " + this.opacity);
        }
    }

    @Override
    public String toString() {
        return "WatermarkSettings{" +
                "size='" + size + '\'' +
                '}';
    }
}
