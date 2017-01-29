package com.PDF.model.settings;

/**
 * Created by martanase on 1/26/2017.
 */
public class ImageSettings extends Settings{

    private int scale;

    private boolean position_absolute = true;

    public ImageSettings() {
        super.setType("image");
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public boolean isPosition_absolute() {
        return position_absolute;
    }

    public void setPosition_absolute(boolean position_absolute) {
        this.position_absolute = position_absolute;
    }
}
