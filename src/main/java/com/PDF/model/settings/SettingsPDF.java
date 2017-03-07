package com.PDF.model.settings;

import com.PDF.model.Settings;

/**
 * Created by martanase on 2/25/2017.
 */
public class SettingsPDF extends Settings {

    private String pagesIncluded = "All"; // "All" or enumeration eg: "1-5,9"
    private int pages;

    public SettingsPDF() {
        super.setType("pdf");
    }

    public SettingsPDF(int pages) {
        super.setType("pdf");
        this.pages = pages;
    }

    public SettingsPDF(int pages, String pagesIncluded) {
        super.setType("pdf");
        this.pages = pages;
        this.pagesIncluded = pagesIncluded;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getPagesIncluded() {
        return pagesIncluded;
    }

    public void setPagesIncluded(String pagesIncluded) {
        this.pagesIncluded = pagesIncluded;
    }
}
