package com.PDF.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by martanase on 12/9/2016.
 */

@ConfigurationProperties("storage")
public class StorageProperties {

    private String location = "upload";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
