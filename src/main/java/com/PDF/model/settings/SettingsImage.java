package com.PDF.model.settings;


import com.PDF.model.Settings;
import com.PDF.model.settings.image.PositionAbsolute;
import com.PDF.utils.Position;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by martanase on 1/26/2017.
 */
public class SettingsImage extends Settings {

    public enum ImageAlignment{
        None(0), Left(0), Middle(1), Right(2), Textwrap(4), Underlying(8);
        private int value;
        ImageAlignment(int value){
            this.value = value;
        }
        public int getValue(){ return value;}
    }


    private float width;
    private float height;
    private ImageAlignment wrappingStyle = ImageAlignment.None;
    private boolean fitToPage = false;
    private boolean absolutePosition = false;
    private Position positionPredefined = Position.CENTER;
    private PositionAbsolute positionAbsolute;
    private int rotationDegrees = 0;
    private int scale = 100;
//    private float opacity = 1f;

    public SettingsImage() {
        super.setType("image");
    }

    public SettingsImage(File file) {
        super.setType("image");
        super.setPageBreak(true);
        if (file != null){
            Image image;
            try {
                image = Image.getInstance(file.getPath());
                this.width = image.getWidth();
                this.height = image.getHeight();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (BadElementException e) {
                e.printStackTrace();
            }
        }
    }

    public SettingsImage(File file, ImageAlignment wrappingStyle, boolean fitToPage, boolean absolutePosition, Position positionPredefined, PositionAbsolute positionAbsolute, int rotationDegrees, int scale) {
        this(file);
        this.wrappingStyle = wrappingStyle;
        this.fitToPage = fitToPage;
        this.absolutePosition = absolutePosition;
        this.positionPredefined = positionPredefined;
        this.positionAbsolute = positionAbsolute;
        this.rotationDegrees = rotationDegrees;
        this.scale = scale;
//        this.opacity = opacity;

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

    public ImageAlignment getWrappingStyle() {
        return wrappingStyle;
    }

    public void setWrappingStyle(ImageAlignment wrappingStyle) {
        this.wrappingStyle = wrappingStyle;
    }

    public boolean isFitToPage() {
        return fitToPage;
    }

    public void setFitToPage(boolean fitToPage) {
        this.fitToPage = fitToPage;
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

    public int getRotationDegrees() {
        return rotationDegrees;
    }

    public void setRotationDegrees(int rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

//    public float getOpacity() {
//        return opacity;
//    }

//    public void setOpacity(float opacity) {
//        this.opacity = opacity;
//    }
}
