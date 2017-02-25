package com.PDF.model.settings;

/**
 * Created by martanase on 1/28/2017.
 */
public class TextSettings extends Settings{

    private int alignment;

    public TextSettings() {
        super.setType("text");
    }

    public int getAlignment() {
        super.setType("text");
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }
}
