package com.PDF.model;

import com.PDF.model.watermark.ImageWatermark;
import com.PDF.model.watermark.TextWatermark;
import com.PDF.service.PDFSettingsService;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;

/**
 * Created by martanase on 3/7/2017.
 */
public class PDFWatermark extends PdfPageEventHelper {

    @Autowired
    private PDFSettings pdfSettings;

    public PDFSettings getPdfSettings() {
        return pdfSettings;
    }

    public void setPdfSettings(PDFSettings pdfSettings) {
        this.pdfSettings = pdfSettings;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfContentByte canvas = writer.getDirectContentUnder();
            if (pdfSettings.getImageWatermark() != null && pdfSettings.getImageWatermark().getWatermark() != null) {
                Image image = Image.getInstance(pdfSettings.getImageWatermark().getWatermark().getPath());
                float widthDoc = document.getPageSize().getWidth();
                float heightDoc = document.getPageSize().getHeight();

                ImageWatermark imageWatermark = pdfSettings.getImageWatermark();
                image.scalePercent(imageWatermark.getScale());

                //image watermark
                if (imageWatermark.isAbsolutePosition()) {
                    image.setAbsolutePosition(imageWatermark.getPositionAbsolute().getX(), imageWatermark.getPositionAbsolute().getY());
                } else {
                    switch (imageWatermark.getPositionPredefined()) {
                        case TOP_LEFT:
                            image.setAbsolutePosition(0,
                                    document.getPageSize().getHeight() - image.getScaledHeight());
                            break;
                        case TOP_CENTER:
                            image.setAbsolutePosition((document.getPageSize().getWidth() - image.getScaledWidth()) / 2,
                                    document.getPageSize().getHeight() - image.getScaledHeight());
                            break;
                        case TOP_RIGHT:
                            image.setAbsolutePosition(document.getPageSize().getWidth() - image.getScaledWidth(),
                                    document.getPageSize().getHeight() - image.getScaledHeight());
                            break;
                        case MIDDLE_LEFT:
                            image.setAbsolutePosition(0, (document.getPageSize().getHeight() - image.getScaledHeight()) / 2);
                            break;
                        case CENTER:
                            image.setAbsolutePosition((document.getPageSize().getWidth() - image.getScaledWidth()) / 2,
                                    (document.getPageSize().getHeight() - image.getScaledHeight()) / 2);
                            break;
                        case MIDDLE_RIGHT:
                            image.setAbsolutePosition(document.getPageSize().getWidth() - image.getScaledWidth(),
                                    (document.getPageSize().getHeight() - image.getScaledHeight()) / 2);
                            break;
                        case BOTTOM_LEFT:
                            image.setAbsolutePosition(0, 0);
                            break;
                        case BOTTOM_CENTER:
                            image.setAbsolutePosition((document.getPageSize().getWidth() - image.getScaledWidth()) / 2, 0);
                            break;
                        case BOTTOM_RIGHT:
                            image.setAbsolutePosition(document.getPageSize().getWidth() - image.getScaledWidth(), 0);
                            break;
                        default:
                            image.setAbsolutePosition((document.getPageSize().getWidth() - image.getScaledWidth()) / 2,
                                    (document.getPageSize().getHeight() - image.getScaledHeight()) / 2);
                    }
                }


                canvas.saveState();
                PdfGState state = new PdfGState();
                state.setFillOpacity(pdfSettings.getImageWatermark().getOpacity());
                canvas.setGState(state);
                canvas.addImage(image);
                canvas.restoreState();
            }
            //text watermark
            if (pdfSettings.getTextWatermark() != null
                    && pdfSettings.getTextWatermark().getWatermark() != null
                    && !pdfSettings.getTextWatermark().getWatermark().equals("")) {
                TextWatermark textWatermark = pdfSettings.getTextWatermark();

                canvas.saveState();
                PdfGState state = new PdfGState();
                state.setFillOpacity(0.3f);
                canvas.setGState(state);
                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                Font font = new Font(Font.FontFamily.HELVETICA,
                        60,
                        Font.ITALIC,
                        BaseColor.BLACK);
                Paragraph phrase = new Paragraph(textWatermark.getWatermark(), font);
//                    ColumnText.getWidth(phrase);
                if(textWatermark.getWatermark().contains("\n")){
                    String[] lines = textWatermark.getWatermark().split("\n");
                    for(String line: lines){
                        System.out.println(line);
                        phrase.add(new Chunk(line));
                        phrase.add(Chunk.NEWLINE);
                    }
                    System.out.println("--------------------");
                }
                ColumnText.showTextAligned(canvas,
                        Element.ALIGN_CENTER, //Keep waterMark center aligned
                        phrase,
                        (document.getPageSize().getLeft() + document.getPageSize().getRight()) / 2,
                        (document.getPageSize().getTop() + document.getPageSize().getBottom()) / 2,
                        0f); // 45f is the rotation angle
                canvas.restoreState();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException b) {
            b.printStackTrace();
        } catch (DocumentException d) {
            d.printStackTrace();
        }
    }
}
