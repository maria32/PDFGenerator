package com.PDF.model.settings;

/**
 * Created by martanase on 1/26/2017.
 */
public class ImageSettings extends Settings{

    private int scale;
    private int rotationDegrees;
    private boolean positionAbsolute = true;

    public ImageSettings() {
        super.setType("image");
    }

    public ImageSettings(int scale, int rotationDegrees, boolean positionAbsolute) {
        this.scale = scale;
        this.rotationDegrees = rotationDegrees;
        this.positionAbsolute = positionAbsolute;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getRotationDegrees() {
        return rotationDegrees;
    }

    public void setRotationDegrees(int rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    public boolean isPositionAbsolute() {
        return positionAbsolute;
    }

    public void setPositionAbsolute(boolean positionAbsolute) {
        this.positionAbsolute = positionAbsolute;
    }
}
