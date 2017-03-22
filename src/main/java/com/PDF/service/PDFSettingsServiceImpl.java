package com.PDF.service;

import com.PDF.exception.StorageException;
import com.PDF.model.MyFile;
import com.PDF.model.PDFSettings;
import com.PDF.model.settings.WatermarkSettings;
import com.PDF.model.watermark.ImageWatermark;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martanase on 3/1/2017.
 */
@Service
public class PDFSettingsServiceImpl implements PDFSettingsService {

    private static PDFSettings pdfSettings;

    @Autowired
    @Value("${pdfSettings.directory}")
    private String watermarkDirPath;

    private static Logger logger = Logger.getLogger(PDFSettingsServiceImpl.class.getName());

    static {
        logger.setLevel(Level.FINE);
    }

    private void existsWatermarkDir() {
        File watermarkDir = new File(watermarkDirPath);
        if (!watermarkDir.exists()) {
            watermarkDir.mkdir();
            logger.log(Level.INFO, "Created watermark directory");
        }
    }

    @Override
    public File addFile(MultipartFile multipartFile) {
        existsWatermarkDir();
        File file;
        try {
            if (multipartFile.isEmpty()) {
                throw new StorageException("Failed to store empty file " + multipartFile.getOriginalFilename());
            }
            FileUtils.cleanDirectory(new File(watermarkDirPath));
            Files.copy(multipartFile.getInputStream(), Paths.get(watermarkDirPath).resolve(multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            file = new File(watermarkDirPath + multipartFile.getOriginalFilename());

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + multipartFile.getOriginalFilename(), e);
        }

        return file;
    }

    //Attention: the object returned by this method focuses only on the file
    //the rest of the fields will be null
    public ImageWatermark addImageWatermark(MultipartFile file){

//        ImageWatermark imageWatermark = new ImageWatermark();
        File watermark = addFile(file);
//        imageWatermark.setWatermark(watermark);
        pdfSettings.getImageWatermark().setWatermark(watermark);
        return pdfSettings.getImageWatermark();
    }

    @Override
    public void deleteWatermark() {
        existsWatermarkDir();
        try {
            FileUtils.cleanDirectory(new File(watermarkDirPath));
            //pdfSettings.setWatermarkPic(null);
            logger.log(Level.INFO, "Deleted watermark");
        }catch (IOException e){
            System.out.println("Error deleting file");
            e.printStackTrace();
        }
    }

    @Override
    public PDFSettings getPDFSettings() {
        if(pdfSettings == null) {
            pdfSettings = new PDFSettings();
            //pdfSettings.setSettings(new WatermarkSettings());
        }
        return pdfSettings;
    }

    @Override
    public void setPDFSettings(PDFSettings pdfSettingsNew) {
        pdfSettings = pdfSettingsNew;
    }


    @Override
    public PDFSettings createPDFSettings(PDFSettings pdfSettingsNew) {
        existsWatermarkDir();
        pdfSettings = pdfSettingsNew;
        return pdfSettings;
    }

}