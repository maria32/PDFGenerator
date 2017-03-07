package com.PDF.model;

import com.PDF.service.PDFSettingsService;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
            Image watermark = Image.getInstance(pdfSettings.getWatermarkPic().getPath());
            float widthDoc = document.getPageSize().getWidth();
            float heightDoc = document.getPageSize().getHeight();


          /*  watermark.setAbsolutePosition((document.getPageSize().getWidth()-watermark.getWidth())/2,
                    (document.getPageSize().getHeight()-watermark.getWidth())/2);*/
            watermark.setAbsolutePosition(0,0);
            PdfContentByte canvas = writer.getDirectContentUnder();
            canvas.saveState();
            PdfGState state = new PdfGState();
            state.setFillOpacity(0.3f);
            canvas.setGState(state);
            canvas.addImage(watermark);
            canvas.restoreState();
        }catch (IOException e){
            e.printStackTrace();
        }catch (BadElementException b){
            b.printStackTrace();
        }catch (DocumentException d){
            d.printStackTrace();
        }
    }
}
