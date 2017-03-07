package com.PDF.model.settings;

import com.PDF.model.Settings;

/**
 * Created by martanase on 1/28/2017.
 */
public class SettingsText extends Settings {

    private int alignment;

    public SettingsText() {
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
