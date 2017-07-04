package com.PDF.controller;

import com.PDF.model.MyFile;
import com.PDF.service.StorageService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by martanase on 12/12/2016.
 */
@Controller
@RequestMapping("/convert")
public class FileConvertController {

    @Autowired
    private StorageService storageService;

    @RequestMapping(value="/{userId}/", method = RequestMethod.GET)
    @ResponseBody
    public List<MyFile> getAll(@PathVariable("userId") Long userId) {
        return storageService.getAll(userId);
    }

    @RequestMapping(value="/{userId}/{name}/{extension}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteFile(@PathVariable("userId") Long userId,
                                @PathVariable("name") String name,
                                @PathVariable("extension") String extension) {
        System.out.println("deleting " + name + "." + extension);

        return storageService.deleteFile(userId, name, extension);
    }

    @RequestMapping(value="/{userId}/generatePDF/{downloadMethod}/{pdfName}", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public ResponseEntity<byte[]> generatePDF(@PathVariable("userId") Long userId,
                                              @PathVariable("downloadMethod") String downloadMethod,
                                              @PathVariable("pdfName") String pdfName) {
        File file = storageService.generatePDF(userId, downloadMethod, pdfName);
        try {
            byte[] contents = IOUtils.toByteArray(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String filename = "test.pdf";
            headers.setContentDispositionFormData(filename, filename);
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value="/generatePDF/progress-bar", method = RequestMethod.GET)
    @ResponseBody
    public int progressBar() {
        return storageService.getGenerationProgressBar();
    }



}