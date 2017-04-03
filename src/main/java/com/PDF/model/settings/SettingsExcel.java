package com.PDF.model.settings;

import com.PDF.model.Settings;

/**
 * Created by martanase on 3/27/2017.
 */
public class SettingsExcel extends Settings {

    private int noOfSheets;

    public SettingsExcel() {
        super.setType("excel");
    }

    public int getNoOfSheets() {
        return noOfSheets;
    }

    public void setNoOfSheets(int noOfSheets) {
        this.noOfSheets = noOfSheets;
    }
}
