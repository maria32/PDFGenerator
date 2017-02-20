package com.PDF.deserializer;

import com.PDF.model.MyFile;
import com.PDF.model.settings.DocumentSettings;
import com.PDF.model.settings.ImageSettings;
import com.PDF.model.settings.Settings;
import com.PDF.model.settings.TextSettings;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;


import java.io.File;
import java.io.IOException;

import static com.PDF.model.MyFile.document;
import static com.PDF.model.MyFile.image;
import static com.PDF.model.MyFile.text;

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
            int scale = settingsNode.get("scale").asInt();
            int rotationDegrees = settingsNode.get("rotationDegrees").asInt();
            boolean positionAbsolute = settingsNode.get("positionAbsolute").asBoolean();
            settings = new ImageSettings(scale, rotationDegrees, positionAbsolute);
            settings.setType("image");
        } else if(document.contains(extension)) {
            //fields to be added
            settings = new DocumentSettings();
            settings.setType("document");
        }else if(text.contains(extension)){
            //fields to be added
            settings = new TextSettings();
            settings.setType("text");
        }else{
            settings = new Settings();
            System.out.println("eroare la setare campuri 'Settings'");
        }

        settings.setPageBreak(pageBreak);
        myFile.setSettings(settings);
        return myFile;
    }
}
