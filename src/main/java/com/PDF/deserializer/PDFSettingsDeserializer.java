package com.PDF.deserializer;

import com.PDF.model.PDFSettings;
import com.PDF.model.PDFSettings;
import com.PDF.model.Settings;
import com.PDF.model.settings.*;
import com.PDF.model.settings.image.PositionAbsolute;
import com.PDF.model.watermark.ImageWatermark;
import com.PDF.model.watermark.TextWatermark;
import com.PDF.utils.Position;
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

        pdfSettings.setPassword(node.get("password").isNull() ? null : node.get("password").asText());
        //lock properties

        if (node.get("textWatermark").isObject()){
            JsonNode nodeTextWatermark = node.get("textWatermark");
            TextWatermark textWatermark = new TextWatermark();
            if (!nodeTextWatermark.get("watermark").isNull() && !nodeTextWatermark.get("watermark").asText().equals("")){
                textWatermark.setWatermark(nodeTextWatermark.get("watermark").asText());
            }
            pdfSettings.setTextWatermark(textWatermark);
        }

        if(node.get("imageWatermark").isObject()) {
            JsonNode nodeImageWatermark = node.get("imageWatermark");
            ImageWatermark imageWatermark = new ImageWatermark();
            if (!nodeImageWatermark.get("watermark").isNull()) {
                imageWatermark.setWatermark(new File(nodeImageWatermark.get("watermark").asText()));
            }
            if (!nodeImageWatermark.get("absolutePosition").isNull()){
                imageWatermark.setAbsolutePosition(nodeImageWatermark.get("absolutePosition").asBoolean());
                if (imageWatermark.isAbsolutePosition()){
                    if (!nodeImageWatermark.get("positionAbsolute").isNull()) {
                        PositionAbsolute positionAbsolute = new PositionAbsolute();
                        positionAbsolute.setX( (float) nodeImageWatermark.get("positionAbsolute").get("x").asDouble());
                        positionAbsolute.setY( (float) nodeImageWatermark.get("positionAbsolute").get("y").asDouble());
                        imageWatermark.setPositionAbsolute(positionAbsolute);
                    }
                }else{
                    if (!nodeImageWatermark.get("positionPredefined").isNull()) {
                        imageWatermark.setPositionPredefined(Position.valueOf(nodeImageWatermark.get("positionPredefined").asText()));
                    }
                }
            }
            imageWatermark.setScale(nodeImageWatermark.get("scale").asInt());
            imageWatermark.setOpacity((float) nodeImageWatermark.get("opacity").asDouble());
            pdfSettings.setImageWatermark(imageWatermark);

        }

//        pdfSettings.setWatermarkPic(new File(node.get("watermarkPic").asText()));
//        WatermarkSettings settings = new WatermarkSettings();
//        pdfSettings.setSettings(settings);
        return pdfSettings;
    }
}
