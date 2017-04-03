package com.PDF.service;

import com.PDF.exception.StorageException;
import com.PDF.exception.StorageFileNotFoundException;
import com.PDF.model.MyFile;
import com.PDF.model.PDFSettings;
import com.PDF.model.PDFWatermark;
import com.PDF.model.StorageProperties;
import com.PDF.model.settings.SettingsImage;
import com.PDF.model.settings.SettingsPDF;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.sevenpdf.service.PDFService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    private static int generationProgressBar = 0;

    @Autowired
    private PDFSettingsService pdfSettingsService;

    @Autowired
    @Value("${upload.directory}")
    public String uploadPath;

    static{
        myFiles = new ArrayList<>();
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public int getGenerationProgressBar() {
        return generationProgressBar;
    }

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
    public List<String> getImageAlignmentOptions() {
        List<String> imageAlignmentOptions = new ArrayList<>();
        for (SettingsImage.ImageAlignment imageAlignmentOption : SettingsImage.ImageAlignment.values()) {
            imageAlignmentOptions.add(imageAlignmentOption.toString());
        }
        return imageAlignmentOptions;
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
                    break;
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

    private File checkTempDirectory(){
        File tempDir = new File(uploadPath + "temp");
        if(!tempDir.exists()) {
            return !tempDir.mkdir() ? null : tempDir;
        }else{
            return tempDir;
        }
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
        generationProgressBar = 0;
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
            PdfWriter pdfWriter = null;
            try {
                pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            }catch (FileNotFoundException fnfe){
                System.out.println("Fisier in uz");
            }
            //pdf settings: password, watermark
            PDFSettings settingsPDF = pdfSettingsService.getPDFSettings();
            if(settingsPDF.getPassword() != null && !settingsPDF.getPassword().equals("")){
                String owner = "Magda";
                pdfWriter.setEncryption(settingsPDF.getPassword().getBytes(), owner.getBytes(), 0, PdfWriter.ENCRYPTION_AES_128);
            }
            if((settingsPDF.getImageWatermark() != null && settingsPDF.getImageWatermark().getWatermark() != null) ||
                    (settingsPDF.getTextWatermark() != null && settingsPDF.getTextWatermark().getWatermark() != null)){
                PDFWatermark pdfWatermark = new PDFWatermark();
                pdfWatermark.setPdfSettings(settingsPDF);
                pdfWriter.setPageEvent(pdfWatermark);
            }

        //end of pdf settings: password, watermark
            document.open();
            document.setMargins(30, 30, 45, 30);
            FontFactory.defaultEmbedding = true;
            //for PDF instances
            List<PdfReader> reader = new ArrayList<>();

            for(MyFile myFile : myFiles){
                if(myFile.getSettings().isPageBreak()){
                    document.newPage();
                }
                Runtime runtime;
                Process process;
                checkTempDirectory();
                System.out.println(myFile.getName());
                switch(myFile.getSettings().getType()){
                    case "image" :
                        Image image = Image.getInstance(myFile.getFile().getPath());
                        //scale
                        SettingsImage imageSettings = (SettingsImage)myFile.getSettings();
                        if(imageSettings.getScale().equals("fit")){
                            image.scaleToFit(PageSize.A4.getWidth() - (document.leftMargin() + document.rightMargin()), PageSize.A4.getHeight() - (document.topMargin() + document.bottomMargin()));
                        }
                        //absolute position
                        //image.setAbsolutePosition(imageSettings.getPositionAbsolute().getX(), imageSettings.getPositionAbsolute().getY());
                        //alignment
                        List<SettingsImage.ImageAlignment> alignmentsList = imageSettings.getAlignment();
                        int alignment = 0;
                        for(SettingsImage.ImageAlignment imageAlignment: alignmentsList){
                            alignment+=imageAlignment.getValue();
                        }
                        image.setAlignment(alignment);
                        //transparency doesn't work. It only works for monochrome or grayscale masks
                        //image.setTransparency(new int[] {0xF0, 0xFF});
                        //rotation
                        image.setRotationDegrees(imageSettings.getRotationDegrees());
                        document.add(image);
                        break;
                    case "text" :
                        document.add(Chunk.NEWLINE); // solution to image aligment TEXTWRAP
                        String FONT = "resources/fonts/FreeSans.ttf";
//                        Font font = FontFactory.getFont(FONT, "Cp1250", BaseFont.EMBEDDED);
//                        BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
//                        Paragraph pHeading = new Paragraph(new Chunk(new Phrase(org.apache.commons.io.FileUtils.readFileToString(myFile.getFile())).toString(), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL)));
                        //pHeading.setAlignment(Paragraph.ALIGN_RIGHT);
                        //document.add(pHeading);
                        document.add(new Phrase(org.apache.commons.io.FileUtils.readFileToString(myFile.getFile()))); // I used Phrase instead of Paragraph

                        break;
                    case "word" :
                        StringBuilder processOutput = new StringBuilder();
                        String wordJsPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/","") + "scripts/SaveWordAsPDF.js";
                        runtime = Runtime.getRuntime();
                        process = runtime.exec("cscript.exe //nologo " + wordJsPath + " " + myFile.getFile().getAbsolutePath());
                        try (BufferedReader processOutputReader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()));) {
                            String outputLine;
                            while ((outputLine = processOutputReader.readLine()) != null) {
                                processOutput.append(outputLine).append(System.lineSeparator());
                            }
                            process.waitFor();
                            if (processOutput.indexOf("Done.") != -1){
                                File word = new File(uploadPath + "temp/" + FilenameUtils.getBaseName(myFile.getName()) + ".pdf");
                                addPdf(document, pdfWriter, reader, word);
                            } else{
                                //eroare conversie
                            }
                        }
                        /*File wordFile = myFile.getFile();
                        File outputPdfFile = new File(uploadPath + "temp/" + FilenameUtils.getBaseName(myFile.getName()) + ".pdf");

                        PDFService MySevenPDFServiceObject = new PDFService("http://localhost:8080", "sevenpdf", "sevenpdf");
                        MySevenPDFServiceObject.Convert(wordFile, outputPdfFile);*/

                        break;
                    case "excel":
                        String excelJsPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/","") + "scripts/SaveExcelAsPDF.js";
                        runtime = Runtime.getRuntime();
                        process = runtime.exec("cscript.exe //nologo " + excelJsPath + " " + myFile.getFile().getAbsolutePath());
                        processOutput = new StringBuilder();
                        try (BufferedReader processOutputReader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()));) {
                            String outputLine;
                            while ((outputLine = processOutputReader.readLine()) != null) {
                                processOutput.append(outputLine).append(System.lineSeparator());
                            }
                            process.waitFor();
                            if (processOutput.indexOf("Done.") != -1){
                                File excel = new File(uploadPath + "temp/" + FilenameUtils.getBaseName(myFile.getName()) + ".pdf");
                                addPdf(document, pdfWriter, reader, excel);
                            } else{
                                //eroare conversie
                            }
                        }
                        break;
                    case "powerPoint":
                        String pptJsPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/","") + "scripts/SavePowerPointAsPDF.js";
                        runtime = Runtime.getRuntime();
                        process = runtime.exec("cscript.exe //nologo " + pptJsPath + " " + myFile.getFile().getAbsolutePath());
                        processOutput = new StringBuilder();
                        try (BufferedReader processOutputReader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()));) {
                            String outputLine;
                            while ((outputLine = processOutputReader.readLine()) != null) {
                                processOutput.append(outputLine).append(System.lineSeparator());
                            }
                            process.waitFor();
                            if (processOutput.indexOf("Done.") != -1){
                                File ppt = new File(uploadPath + "temp/" + FilenameUtils.getBaseName(myFile.getName()) + ".pdf");
                                addPdf(document, pdfWriter, reader, ppt);
                            } else{
                                //eroare conversie
                            }
                        }

                        break;
                    case "pdf" :
                        addPdf(document, pdfWriter, reader, myFile);
                        break;
                }
                generationProgressBar++;
            }

            document.close();
            for (PdfReader readerItem : reader){
                readerItem.close();
            }
            //opening the file in default application (Acrobat Reader if installed)
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + pdfFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //generationProgressBar = 0;
        return "ok";
    }

    private void addPdf(Document document, PdfWriter pdfWriter, List<PdfReader> reader, MyFile myFile) throws IOException {
        Rectangle originalPageSize = pdfWriter.getPageSize();
        document.newPage();
        PdfContentByte cb = pdfWriter.getDirectContent();
        reader.add(new PdfReader(myFile.getFile().getAbsolutePath()));
        SettingsPDF pdfSettings = (SettingsPDF) myFile.getSettings();
        if(!pdfSettings.getPagesIncluded().equals("All")){
            reader.get(reader.size()-1).selectPages(pdfSettings.getPagesIncluded());
        }
        for(int i = 1; i <= reader.get(reader.size()-1).getNumberOfPages(); i++) {
            Rectangle pageSize = reader.get(reader.size()-1).getPageSize(i);
            document.setPageSize(pageSize);
            document.newPage();
            PdfImportedPage page = pdfWriter.getImportedPage(reader.get(reader.size()-1), i);
            cb.addTemplate(page, 0, 0);
        }
        document.setPageSize(originalPageSize);
        document.newPage();
    }

    private void addPdf(Document document, PdfWriter pdfWriter, List<PdfReader> reader, File file) throws IOException {
        Rectangle originalPageSize = pdfWriter.getPageSize();
        document.newPage();
        PdfContentByte cb = pdfWriter.getDirectContent();
        reader.add(new PdfReader(file.getAbsolutePath()));
        for(int i = 1; i <= reader.get(reader.size()-1).getNumberOfPages(); i++) {
            Rectangle pageSize = reader.get(reader.size()-1).getPageSize(i);
            document.setPageSize(pageSize);
            document.newPage();
            PdfImportedPage page = pdfWriter.getImportedPage(reader.get(reader.size()-1), i);
            cb.addTemplate(page, 0, 0);
        }
        document.setPageSize(originalPageSize);
        document.newPage();
    }
}