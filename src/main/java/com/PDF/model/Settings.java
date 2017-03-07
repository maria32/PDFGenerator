package com.PDF.model;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by martanase on 1/26/2017.
 */
public class Settings {

    private String type;

    private boolean pageBreak;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPageBreak() {
        return pageBreak;
    }

    public void setPageBreak(boolean pageBreak) {
        this.pageBreak = pageBreak;
    }
}
