package com.PDF.service.impl;

import com.PDF.exception.StorageException;
import com.PDF.model.PDFSettings;
import com.PDF.model.watermark.ImageWatermark;
import com.PDF.service.PDFSettingsService;
import com.PDF.service.UserSessionService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    @Value("${workspace.directory}")
    private String watermarkDirPath;

    @Autowired
    private UserSessionService userSessionService;

    private static Logger logger = Logger.getLogger(PDFSettingsServiceImpl.class.getName());

    static {
        logger.setLevel(Level.FINE);
    }

    private String getWatermarkDirPath(){
        return watermarkDirPath + "upload" + userSessionService.getUsername() + "/watermark/";
    }

    private void existsWatermarkDir() {
        File watermarkDir = new File(getWatermarkDirPath());
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
            FileUtils.cleanDirectory(new File(getWatermarkDirPath()));
            Files.copy(multipartFile.getInputStream(), Paths.get(getWatermarkDirPath()).resolve(multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            file = new File(getWatermarkDirPath() + multipartFile.getOriginalFilename());

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
            FileUtils.cleanDirectory(new File(getWatermarkDirPath()));
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