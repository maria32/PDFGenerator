package com.PDF.controller;

import com.PDF.model.PDFSettings;
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

    @RequestMapping(value="/", method = RequestMethod.GET)
    @ResponseBody
    public PDFSettings getPDFSettings(){
        System.out.println("PDFSettings controller");
        return pdfSettingsService.getPDFSettings();
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    @ResponseBody
    public File setPDFSettings(@RequestParam(value="file", required = false) MultipartFile file) {
        System.out.println("set PDF settings controller - set7");
        if(file == null){
            pdfSettingsService.deleteWatermark();
            return null;
        }
        return pdfSettingsService.addFile(file);
    }

    @RequestMapping(value="/", method = RequestMethod.POST)
    public PDFSettings setPDFSettings(@RequestBody PDFSettings pdfSettings) {
        System.out.println("controller: " + pdfSettings.toString());
        return pdfSettingsService.createPDFSettings(pdfSettings);
    }
}