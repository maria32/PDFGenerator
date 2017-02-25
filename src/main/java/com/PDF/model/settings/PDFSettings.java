package com.PDF.model.settings;

/**
 * Created by martanase on 2/25/2017.
 */
public class PDFSettings extends Settings{

    private String pagesIncluded = "All"; // "All" or enumeration eg: "1-5,9"
    private int pages;

    public PDFSettings() {
        super.setType("pdf");
    }

    public PDFSettings(int pages) {
        super.setType("pdf");
        this.pages = pages;
    }

    public PDFSettings(int pages, String pagesIncluded) {
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
