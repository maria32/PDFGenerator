package com.PDF.service;

import com.PDF.model.MyFile;
import com.PDF.model.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by martanase on 12/9/2016.
 */
public interface StorageService {

    void init();

    void store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    List<String> getImageAlignmentOptions();

    void deleteAll();

    List<MyFile> getAll(Long userId);

    boolean deleteFile(Long userId, String name, String type);

    void updateOrderOfFiles(Long userId, List<Integer> listOfOrder);

    void updateSettingsOfFile(Long userId, List<MyFile> files);

    File generatePDF(Long userId, String downloadMethod);

    int getGenerationProgressBar();


}
