package com.PDF.deserializer;

import com.PDF.model.PDFSettings;
import com.PDF.model.PDFSettings;
import com.PDF.model.Settings;
import com.PDF.model.settings.*;
import com.PDF.model.settings.image.PositionAbsolute;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.PDF.model.PDFSettings.*;

/**
 * Created by martanase on 3/6/2017.
 */
public class PDFSettingsDeserializer extends StdDeserializer<PDFSettings> {

    public PDFSettingsDeserializer() {
        this(null);
    }

    public PDFSettingsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public PDFSettings deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        System.out.println("PDFSettingsDeserializer");
        PDFSettings pdfSettings = new PDFSettings();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        pdfSettings.setPassword(node.get("password").asText());
        pdfSettings.setWatermarkPic(new File(node.get("watermarkPic").asText()));
        WatermarkSettings settings = new WatermarkSettings();
        pdfSettings.setSettings(settings);
        return pdfSettings;
    }
}
