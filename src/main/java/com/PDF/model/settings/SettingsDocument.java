package com.PDF.model.settings;

import com.PDF.model.Settings;

/**
 * Created by martanase on 1/26/2017.
 */
public class SettingsDocument extends Settings {

    private String pagesToBind;
    private String pageSizing;
    private String orientation;
    private int fontSize;

    public SettingsDocument() {
        super.setType("document");
        this.pagesToBind = "All";
        this.pageSizing = "Fit";
    }

    public String getPagesToBind() {
        return pagesToBind;
    }

    public void setPagesToBind(String pagesToBind) {
        this.pagesToBind = pagesToBind;
    }

    public String getPageSizing() {
        return pageSizing;
    }

    public void setPageSizing(String pageSizing) {
        this.pageSizing = pageSizing;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
