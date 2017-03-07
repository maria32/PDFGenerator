package com.PDF.controller;

import com.PDF.model.MyFile;
import com.PDF.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by martanase on 12/12/2016.
 */
@Controller
@RequestMapping("/convert")
public class FileConvertController {

    @Autowired
    private StorageService storageService;

    @RequestMapping(value="/", method = RequestMethod.GET)
    @ResponseBody
    public List<MyFile> getAll() {
        return storageService.getAll();
    }

    @RequestMapping(value="/{name}/{extension}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteFile(@PathVariable("name") String name,
                              @PathVariable("extension") String extension) {
        System.out.println("deleting " + name + "." + extension);
        return storageService.deleteFile(name, extension);
    }

    @RequestMapping(value="/generatePDF", method = RequestMethod.GET)
    @ResponseBody
    public String generatePDF() {
        return storageService.generatePDF();
    }

}