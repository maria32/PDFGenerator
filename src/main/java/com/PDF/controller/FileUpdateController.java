package com.PDF.controller;

import com.PDF.deserializer.MyFileDeserializer;
import com.PDF.model.MyFile;
import com.PDF.service.StorageService;
import com.PDF.service.UpdateService;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martanase on 2/16/2017.
 */
@Controller
@RequestMapping("/update")
public class FileUpdateController {

    @Autowired
    private StorageService storageService;

    @RequestMapping(value="/order-of-files", method = RequestMethod.POST)
    public void updateOrderOfFiles(@RequestBody ArrayList<Integer> listOfOrder) {
        storageService.updateOrderOfFiles(listOfOrder);
    }


    @RequestMapping(value="/settings", method = RequestMethod.POST)
    public void updateSettingsOfFiles(@RequestBody ArrayList<MyFile> files,
                                   RedirectAttributes redirectAttributes) {
        System.out.println("******I am in the update settings of files controller java");
        storageService.updateSettingsOfFile(files);
    }

    @RequestMapping(value="/settings/image-alignment-options/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getImageAlignmentOptions() {
        return storageService.getImageAlignmentOptions();
    }
}
