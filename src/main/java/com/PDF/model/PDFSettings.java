package com.PDF.model;

import com.PDF.deserializer.MyFileDeserializer;
import com.PDF.deserializer.PDFSettingsDeserializer;
import com.PDF.model.settings.SettingsImage;
import com.PDF.model.settings.WatermarkSettings;
import com.PDF.model.watermark.ImageWatermark;
import com.PDF.model.watermark.TextWatermark;
import com.PDF.utils.LockProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.File;

/**
 * Created by martanase on 2/28/2017.
 */
@JsonDeserialize(using = PDFSettingsDeserializer.class)
public class PDFSettings {

    private String password;
    private LockProperties lockProperties;
    private TextWatermark textWatermark = new TextWatermark();
    private ImageWatermark imageWatermark = new ImageWatermark();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LockProperties getLockProperties() {
        return lockProperties;
    }

    public void setLockProperties(LockProperties lockProperties) {
        this.lockProperties = lockProperties;
    }

    public TextWatermark getTextWatermark() {
        return textWatermark;
    }

    public void setTextWatermark(TextWatermark textWatermark) {
        this.textWatermark = textWatermark;
    }

    public ImageWatermark getImageWatermark() {
        return imageWatermark;
    }

    public void setImageWatermark(ImageWatermark imageWatermark) {
        this.imageWatermark = imageWatermark;
    }

    @Override
    public String toString() {
        return "PDFSettings{" +
                "password='" + password + '\'' +
                ", lockProperties=" + lockProperties +
                ", textWatermark=" + textWatermark +
                ", imageWatermark=" + imageWatermark +
                '}';
    }
}


