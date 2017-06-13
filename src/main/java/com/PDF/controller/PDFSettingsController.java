package com.PDF.controller;

import com.PDF.model.PDFSettings;
import com.PDF.model.watermark.ImageWatermark;
import com.PDF.service.PDFSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;

/**
 * Created by martanase on 3/1/2017.
 */
@Controller
@RequestMapping("/pdf-settings")
public class PDFSettingsController {

    @Autowired
    private PDFSettingsService pdfSettingsService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getPdfSettingsPage(){
        System.out.println("Taking user to pdf-settings page");
        return "template/pdf-settings";
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    @ResponseBody
    public PDFSettings getPDFSettings(){
        System.out.println("PDFSettings controller");
        return pdfSettingsService.getPDFSettings();
    }

    @RequestMapping(value="/save/", method = RequestMethod.POST)
    @ResponseBody
    public PDFSettings setPDFSettings(@RequestBody PDFSettings pdfSettings) {
        System.out.println("controller: " + pdfSettings.toString());
        return pdfSettingsService.createPDFSettings(pdfSettings);
    }

    @RequestMapping(value="/upload-watermark-picture/", method = RequestMethod.POST)
    @ResponseBody
    public ImageWatermark addImageWatermark(@RequestParam(value="file", required = false) MultipartFile file) {
        System.out.println("set PDF settings controller - set7");
        if(file == null){
            System.out.println("file is null");
            pdfSettingsService.deleteWatermark();
            return null;
        }
        return pdfSettingsService.addImageWatermark(file);
    }
}