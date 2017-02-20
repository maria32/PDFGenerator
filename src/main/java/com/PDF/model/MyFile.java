package com.PDF.model;

import com.PDF.deserializer.MyFileDeserializer;
import com.PDF.model.settings.DocumentSettings;
import com.PDF.model.settings.ImageSettings;
import com.PDF.model.settings.Settings;
import com.PDF.model.settings.TextSettings;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.io.FilenameUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by martanase on 12/12/2016.
 */
@JsonDeserialize(using = MyFileDeserializer.class)
public class MyFile<S extends Settings> {

    private static final AtomicInteger count = new AtomicInteger(0);
    public static final List<String> image = Arrays.asList("jpg", "png", "bmp");
    public static final List<String> document = Arrays.asList("doc", "docx");
    public static final List<String> text = Arrays.asList("txt", "rtf");

    private Integer id;
    private String name;
    private String extension;
    private File file;
    private S settings;

    public MyFile(){ //used by ajax call

    }
    public MyFile(File file) {
        if(this.id == null)
            this.id = count.incrementAndGet();
        this.file = file;
        this.name = FilenameUtils.getBaseName(file.getName());
        this.extension = FilenameUtils.getExtension(file.getName()).toLowerCase();
        //set "settings" type based on extension
        Settings settings;
        if(image.contains(extension)){
            settings = new ImageSettings();
        } else if(document.contains(extension)) {
            settings = new DocumentSettings();
        }else if(text.contains(extension)){
            settings = new TextSettings();
        }else{
            settings = new Settings();
            System.out.println("eroare la setare campuri 'Settings'");
        }
        setSettings((S) settings);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = FilenameUtils.getBaseName(name);
        this.extension = FilenameUtils.getExtension(name);
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public S getSettings() {
        return settings;
    }

    public void setSettings(S settings) {
        this.settings = settings;
    }


    @Override
    public String toString() {
        return "MyFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", extension='" + extension + '\'' +
                ", file=" + file +
                ", settings=" + settings +
                '}';
    }
}

