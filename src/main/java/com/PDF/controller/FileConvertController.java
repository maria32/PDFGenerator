package com.PDF.controller;

import com.PDF.model.MyFile;
import com.PDF.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="/{userId}/generatePDF", method = RequestMethod.GET)
    @ResponseBody
    public String generatePDF(@PathVariable("userId") Long userId) {
        return storageService.generatePDF(userId);
    }

    @RequestMapping(value="/generatePDF/progress-bar", method = RequestMethod.GET)
    @ResponseBody
    public int progressBar() {
        return storageService.getGenerationProgressBar();
    }



}