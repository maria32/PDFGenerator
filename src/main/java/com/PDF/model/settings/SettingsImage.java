package com.PDF.model.settings;


import com.PDF.model.Settings;
import com.PDF.model.settings.image.PositionAbsolute;

import java.util.Arrays;
import java.util.List;

/**
 * Created by martanase on 1/26/2017.
 */
public class SettingsImage extends Settings {

    public enum ImageAlignment{
        LEFT(0), MIDDLE(1), RIGHT(2), TEXTWRAP(4), UNDERLYING(8);
        private int value;
        private ImageAlignment(int value){
            this.value = value;
        }
        public int getValue(){ return value;}
    }
    private String scale = "original"; //original, fit, custom
    private int rotationDegrees;
    private List<ImageAlignment> alignment = Arrays.asList(ImageAlignment.LEFT);
    private PositionAbsolute positionAbsolute;

    public SettingsImage() {
        super.setType("image");
    }

    public SettingsImage(String scale, List<ImageAlignment> alignment, int rotationDegrees, PositionAbsolute positionAbsolute) {
        super.setType("image");
        this.scale = scale;
        this.alignment = alignment;
        this.rotationDegrees = rotationDegrees;
        this.positionAbsolute = positionAbsolute;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public int getRotationDegrees() {
        return rotationDegrees;
    }

    public void setRotationDegrees(int rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    public List<ImageAlignment> getAlignment() {
        return alignment;
    }

    public void setAlignment(List<ImageAlignment> alignment) {
        this.alignment = alignment;
    }

    public PositionAbsolute getPositionAbsolute() {
        return positionAbsolute;
    }

    public void setPositionAbsolute(PositionAbsolute positionAbsolute) {
        this.positionAbsolute = positionAbsolute;
    }
}
