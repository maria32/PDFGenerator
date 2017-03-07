package com.PDF.model.settings;

/**
 * Created by martanase on 3/6/2017.
 */
public class WatermarkSettings {

    private String size = "original";

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "WatermarkSettings{" +
                "size='" + size + '\'' +
                '}';
    }
}
