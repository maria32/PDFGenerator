package com.PDF.model;

import com.PDF.deserializer.MyFileDeserializer;
import com.PDF.deserializer.PDFSettingsDeserializer;
import com.PDF.model.settings.SettingsImage;
import com.PDF.model.settings.WatermarkSettings;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.File;

/**
 * Created by martanase on 2/28/2017.
 */
@JsonDeserialize(using = PDFSettingsDeserializer.class)
public class PDFSettings {

    private String password;

    private File watermarkPic;

    private WatermarkSettings settings;

    private String fileName;

    public PDFSettings() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public File getWatermarkPic() {
        return watermarkPic;
    }

    public void setWatermarkPic(File watermarkPic) {
        this.watermarkPic = watermarkPic;
        if(watermarkPic == null){
            this.fileName = null;
        }else {
            this.fileName = watermarkPic.getName();
        }
    }

    public WatermarkSettings getSettings() {
        return settings;
    }

    public void setSettings(WatermarkSettings settings) {
        this.settings = settings;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "PDFSettings{" +
                "password='" + password + '\'' +
                ", watermarkPic=" + watermarkPic +
                ", settings=" + settings +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}


