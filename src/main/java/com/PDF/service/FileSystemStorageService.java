package com.PDF.service;

import com.PDF.exception.StorageException;
import com.PDF.exception.StorageFileNotFoundException;
import com.PDF.model.MyFile;
import com.PDF.model.StorageProperties;
import com.PDF.model.settings.ImageSettings;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


/**
 * Created by martanase on 12/9/2016.
 */
@Service
public class FileSystemStorageService implements StorageService{

    private final Path rootLocation;

    private static List<MyFile> myFiles;

    static{
        myFiles = new ArrayList<>();
    }

    @Autowired
    @Value("${upload.directory}")
    public String uploadPath;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        System.out.println("I am in store() Service");
        checkUploadDirectory();
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), Paths.get(uploadPath).resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }



    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }



    @Override
    public List<MyFile> getAll(){
        File currentDirectory = checkUploadDirectory();
        List<File> filesInMyFiles = new ArrayList<>();
        for (MyFile myFile : myFiles) {
            filesInMyFiles.add(myFile.getFile());
        }

        for (File file : currentDirectory.listFiles()) {
            if(!filesInMyFiles.contains(file) && !file.isDirectory()) {
                myFiles.add(new MyFile(file));
                filesInMyFiles.add(file);
            }
        }

        return myFiles;
    }

    public boolean deleteFile(String name, String extension){
        boolean deleteStatus = false;
        if(existsUploadDirectory()){
            List<MyFile> files = getAll();
            for(MyFile file: files){
                if(file.getName().equals(name) && file.getExtension().equals(extension)){
                    try {
                        Files.delete(file.getFile().toPath());
                        myFiles.remove(file);
                        deleteStatus = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return deleteStatus;
    }

    private File checkUploadDirectory(){
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()) {
            return !uploadDir.mkdir() ? null : uploadDir;
        }else{
            return uploadDir;
        }
    }

    private boolean existsUploadDirectory(){
        File uploadDir = new File(uploadPath);
        return uploadDir.exists();
    }

    public void updateOrderOfFiles(List<Integer> listOfOrder){

        List<MyFile> myFilesNewOrder = new ArrayList<>();

        if(myFiles.size() != listOfOrder.size()){
            System.out.println("Eroare de reordonare in FileSystemStorageService:updateOrderOfFiles()");
        } else {
            for (Integer id : listOfOrder) {
                for (MyFile myFile : myFiles) {
                    if (myFile.getId() == id) {
                        myFilesNewOrder.add(myFile);
                    }
                }
            }
            myFiles = new ArrayList<>();
            myFiles = myFilesNewOrder;
        }
    }

    public void updateSettingsOfFile(List<MyFile> files){
        System.out.println("Updating...");
        for(MyFile myFile: files){
            System.out.println(myFile.getName());
        }
        if(files.size() == myFiles.size()){
            myFiles = files;
        }

    }

    public String generatePDF() {
        Document document = new Document();
        try {
            File outputDir = new File(uploadPath + "output");
            if(!outputDir.exists()){
                outputDir.mkdir();
            }

            File pdfFile = new File(uploadPath + "output/GeneratedPDF.pdf");
            if(!pdfFile.exists())
                pdfFile.createNewFile();
            else {
                pdfFile.delete();
                pdfFile.createNewFile();
            }
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            FontFactory.defaultEmbedding = true;
            for(MyFile myFile : myFiles){
                if(myFile.getSettings().isPageBreak()){
                    System.out.println("pageBreak");
                    document.newPage();
                }
                switch(myFile.getSettings().getType()){
                    case "image" :
                        Image image = Image.getInstance(myFile.getFile().getPath());
                        ImageSettings imageSettings = ((ImageSettings)myFile.getSettings());
                        image.setRotationDegrees(imageSettings.getRotationDegrees());
                        document.add(image);
                        break;
                    case "text" :
                        String FONT = "resources/fonts/FreeSans.ttf";
                        Font font = FontFactory.getFont(FONT, "Cp1250", BaseFont.EMBEDDED);
                        BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
                        document.add(new Paragraph(org.apache.commons.io.FileUtils
                                .readFileToString(myFile.getFile())));

                        break;
                    case "document" :
                        break;
                }

            }
            document.close();
            //opening the file in default application (Acrobat Reader)
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + pdfFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }
}