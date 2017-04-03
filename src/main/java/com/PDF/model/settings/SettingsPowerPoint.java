package com.PDF.model.settings;

import com.PDF.model.Settings;

/**
 * Created by martanase on 3/27/2017.
 */
public class SettingsPowerPoint extends Settings {

    private int noOfSlides;

    public SettingsPowerPoint() {
        super.setType("powerPoint");
    }

    public int getNoOfSlides() {
        return noOfSlides;
    }

    public void setNoOfSlides(int noOfSlides) {
        this.noOfSlides = noOfSlides;
    }
}
