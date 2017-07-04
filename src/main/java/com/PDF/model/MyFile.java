package com.PDF.model;

import com.PDF.deserializer.MyFileDeserializer;
import com.PDF.model.settings.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itextpdf.text.pdf.PdfReader;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by martanase on 12/12/2016.
 */
@JsonDeserialize(using = MyFileDeserializer.class)
public class MyFile<S extends Settings> {

    private static final AtomicInteger count = new AtomicInteger(0);
    public static final List<String> image = Arrays.asList("jpg", "png", "bmp", "gif", "tif", "tiff", "svg", "svm", "pcx", "mov");
    public static final List<String> word = Arrays.asList("doc", "docx", "odt", "rtf");
    public static final List<String> excel = Arrays.asList("xls", "xlsx", "xla", "xlt", "ods", "csv", "xlsb");
    public static final List<String> powerPoint = Arrays.asList("ppt", "pptx", "pps", "ppsx");
    public static final List<String> text = Arrays.asList("txt", "rtf");
    public static final List<String> pdf = Arrays.asList("pdf");
    public static final List<String> html = Arrays.asList("html");

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
            settings = new SettingsImage(file);
        }else if(text.contains(extension)) {
            settings = new SettingsText();
        } else if (html.contains(extension)){
            settings = new SettingsHtml();
        } else if(word.contains(extension)) {
            settings = new SettingsWord();
        } else if(excel.contains(extension)) {
            settings = new SettingsExcel();
        } else if(powerPoint.contains(extension)) {
            settings = new SettingsPowerPoint();
        }else if(pdf.contains(extension)){
            try {
                PdfReader reader = new PdfReader(getFile().getAbsolutePath());
                settings = new SettingsPDF(reader.getNumberOfPages());
                reader.close();
            }catch (IOException e){
                settings = new SettingsPDF();
                e.printStackTrace();
            }
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

