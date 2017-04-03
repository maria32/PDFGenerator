package com.PDF.service;

import com.PDF.model.MyFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.ArrayList;
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

    List<MyFile> getAll();

    boolean deleteFile(String name, String type);

    void updateOrderOfFiles(List<Integer> listOfOrder);

    void updateSettingsOfFile(List<MyFile> files);

    String generatePDF();

    int getGenerationProgressBar();


}
