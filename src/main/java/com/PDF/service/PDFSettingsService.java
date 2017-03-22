package com.PDF.service;

import com.PDF.model.PDFSettings;
import com.PDF.model.watermark.ImageWatermark;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by martanase on 3/1/2017.
 */
public interface PDFSettingsService {

    PDFSettings getPDFSettings();

    void setPDFSettings(PDFSettings pdfSettings);

    PDFSettings createPDFSettings(PDFSettings pdfSettings);

    File addFile(MultipartFile file);

    ImageWatermark addImageWatermark(MultipartFile file);

    void deleteWatermark();

}
