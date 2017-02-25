package com.PDF.deserializer;

import com.PDF.model.MyFile;
import com.PDF.model.settings.*;
import com.PDF.model.settings.image.PositionAbsolute;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.PDF.model.MyFile.*;

/**
 * Created by martanase on 2/20/2017.
 */
public class MyFileDeserializer extends StdDeserializer<MyFile>{

    public MyFileDeserializer(){
        this(null);
    }

    public MyFileDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public MyFile deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        MyFile myFile = new MyFile();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        myFile.setId((Integer) (node.get("id")).numberValue());
        myFile.setName(node.get("name").asText());
        String extension = node.get("extension").asText();
        myFile.setExtension(extension);
        myFile.setFile(new File(node.get("file").asText()));
        Settings settings;
        JsonNode settingsNode = node.get("settings");
        boolean pageBreak = settingsNode.get("pageBreak").asBoolean();
        if(image.contains(extension)){
            //scale
            String scale = settingsNode.get("scale").asText();
            //rotation degrees
            int rotationDegrees = settingsNode.get("rotationDegrees").asInt();
            //alignment
            List<String> alignmentsStringList = new ObjectMapper().convertValue(settingsNode.get("alignment"), List.class);
            List<ImageSettings.ImageAlignment> alignment = new ArrayList<>();
            for(String align : alignmentsStringList){
                System.out.println(align);
                alignment.add(ImageSettings.ImageAlignment.valueOf(align));
            }
            //position absolute
            PositionAbsolute positionAbsolute = null;
            if(settingsNode.get("positionAbsolute").isObject()) {
                //nu intra aici. de schimbat conditia
                positionAbsolute = new PositionAbsolute(Float.valueOf(settingsNode.get("positionAbsolute").get("x").asText()), Float.valueOf(settingsNode.get("positionAbsolute").get("y").asText()));
                System.out.println("x= " + positionAbsolute.getX());
                System.out.println("y= " + positionAbsolute.getY());
            }
            settings = new ImageSettings(scale, alignment, rotationDegrees, positionAbsolute);
        } else if(document.contains(extension)) {
            //fields to be added
            settings = new DocumentSettings();
        }else if(text.contains(extension)) {
            //fields to be added
            settings = new TextSettings();
        }else if(pdf.contains(extension)){
            int pages = settingsNode.get("pages").asInt();
            String pagesIncluded = settingsNode.get("pagesIncluded").asText();
            settings = new PDFSettings(pages, pagesIncluded);
        }else{
            settings = new Settings();
            System.out.println("Deserializer: eroare la setare campuri 'Settings'");
        }

        settings.setPageBreak(pageBreak);
        myFile.setSettings(settings);
        return myFile;
    }
}
