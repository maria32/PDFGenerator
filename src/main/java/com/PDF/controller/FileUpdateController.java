package com.PDF.controller;

import com.PDF.model.MyFile;
import com.PDF.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value="/{userId}/order-of-files/", method = RequestMethod.POST)
    @ResponseBody
    public void updateOrderOfFiles(@PathVariable("userId") Long userId,
            @RequestBody ArrayList<Integer> listOfOrder) {
        storageService.updateOrderOfFiles(userId, listOfOrder);
    }


    @RequestMapping(value="/{userId}/settings", method = RequestMethod.POST)
    @ResponseBody
    public void updateSettingsOfFiles(@PathVariable("userId") Long userId,
            @RequestBody ArrayList<MyFile> files,
                                   RedirectAttributes redirectAttributes) {
        System.out.println("******I am in the update settings of files controller java");
        storageService.updateSettingsOfFile(userId, files);
    }

    @RequestMapping(value="/settings/image-alignment-options/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getImageAlignmentOptions() {
        return storageService.getImageAlignmentOptions();
    }
}
