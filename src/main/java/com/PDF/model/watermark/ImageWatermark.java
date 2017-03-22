package com.PDF.model.watermark;

import com.PDF.model.settings.image.PositionAbsolute;
import com.PDF.utils.Position;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by martanase on 3/19/2017.
 */
public class ImageWatermark {

    private File watermark;
    private String fileName;

    private float width;
    private float height;

    private boolean absolutePosition = false;
    private Position positionPredefined = Position.CENTER;
    private PositionAbsolute positionAbsolute;
    private int scale = 100;
    private float opacity = 0.3f;

    public File getWatermark() {
        return watermark;
    }

    public void setWatermark(File watermark) {
        this.watermark = watermark;
        if (watermark != null){
            this.fileName = watermark.getName();
            Image imageWatermark = null;
            try {
                imageWatermark = Image.getInstance(watermark.getPath());
                this.width = imageWatermark.getWidth();
                this.height = imageWatermark.getHeight();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (BadElementException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isAbsolutePosition() {
        return absolutePosition;
    }

    public void setAbsolutePosition(boolean absolutePosition) {
        this.absolutePosition = absolutePosition;
    }

    public Position getPositionPredefined() {
        return positionPredefined;
    }

    public void setPositionPredefined(Position positionPredefined) {
        this.positionPredefined = positionPredefined;
    }

    public PositionAbsolute getPositionAbsolute() {
        return positionAbsolute;
    }

    public void setPositionAbsolute(PositionAbsolute positionAbsolute) {
        this.positionAbsolute = positionAbsolute;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    @Override
    public String toString() {
        return "ImageWatermark{" +
                "watermark=" + watermark +
                ", fileName='" + fileName + '\'' +
                ", absolutePosition=" + absolutePosition +
                ", positionPredefined=" + positionPredefined +
                ", positionAbsolute=" + positionAbsolute +
                ", scale=" + scale +
                ", opacity=" + opacity +
                '}';
    }
}
