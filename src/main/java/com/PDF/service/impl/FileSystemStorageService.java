package com.PDF.service.impl;

import com.PDF.exception.StorageException;
import com.PDF.exception.StorageFileNotFoundException;
import com.PDF.model.*;
import com.PDF.model.settings.SettingsImage;
import com.PDF.model.settings.SettingsPDF;
import com.PDF.service.PDFSettingsService;
import com.PDF.service.StorageService;
import com.PDF.service.UserService;
import com.PDF.service.UserSessionService;
import com.PDF.utils.MailSender;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;


/**
 * Created by martanase on 12/9/2016.
 */
@Service
public class FileSystemStorageService implements StorageService {

    private static final Logger LOGGER = Logger.getLogger(FileSystemStorageService.class.getName());

    private final Path rootLocation;
//    private static Map<userId, userFiles> myFiles
    private static Map<Long, List<MyFile>> myFiles = new HashMap<>();
    private static int generationProgressBar = 0;

    @Autowired
    private PDFSettingsService pdfSettingsService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserService userService;

    @Autowired
    @Value("${workspace.directory}")
    public String workingDirPath;


    public String getWorkingDirPath() {
        return workingDirPath;
    }

    public void setWorkingDirPath(String workingDirPath) {
        this.workingDirPath = workingDirPath;
    }

    public int getGenerationProgressBar() {
        return generationProgressBar;
    }

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    private Path getUserDirPath(){
        return Paths.get(workingDirPath + "upload" + userSessionService.getUsername() + "/");
    }

    private String getUserTempDir(){
        return getUserDirPath() + "/temp/";
    }

    @Override
    public void store(MultipartFile file) {
        System.out.println("I am in store() Service");
        checkUploadDirectory();
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path userDirPath = getUserDirPath();
            Files.copy(file.getInputStream(), userDirPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

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
    public List<MyFile> getAll(Long userId){
        User existsUser = userService.getOne(userId);
        if (existsUser != null) {
            if (myFiles.get(existsUser.getId()) == null){
                myFiles.put(existsUser.getId(), new ArrayList<>());
            }
            List<MyFile> userFiles = myFiles.get(existsUser.getId());
            File currentDirectory = checkUploadDirectory();
            List<File> filesInMyFiles = new ArrayList<>();

            for (MyFile myFile : userFiles) {
                filesInMyFiles.add(myFile.getFile());
            }

            for (File file : currentDirectory.listFiles()) {
                if (!file.isDirectory()) {
                    if (!filesInMyFiles.contains(file)) {
                        userFiles.add(new MyFile(file));
                        filesInMyFiles.add(file);
                    }
                }
            }

            myFiles.put(existsUser.getId(), userFiles);
            return myFiles.get(existsUser.getId());
        }

        return null;
    }

    public boolean deleteFile(Long userId, String name, String extension){
        boolean deleteStatus = false;
        if(existsUploadDirectory()){
            List<MyFile> files = getAll(userId);
            for(MyFile file: files){
                if(file.getName().equals(name) && file.getExtension().equals(extension)){
                    try {
                        Files.delete(file.getFile().toPath());
                        myFiles.get(userId).remove(file);
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
        //Files.createDirectories(userDirPath);
        File uploadDir = new File(getUserDirPath().toString());
        if(!uploadDir.exists()) {
            return !uploadDir.mkdirs() ? null : uploadDir;
        }else{
            return uploadDir;
        }
    }

    private boolean existsUploadDirectory(){
        File uploadDir = new File(getUserDirPath().toString());
        return uploadDir.exists();
    }

    private File checkTempDirectory(){
        File tempDir = new File(getUserDirPath().toString() + "/temp");
        if(!tempDir.exists()) {
            return !tempDir.mkdirs() ? null : tempDir;
        }else{
            return tempDir;
        }
    }

    public void updateOrderOfFiles(Long userId, List<Integer> listOfOrder){

        List<MyFile> myFilesNewOrder = new ArrayList<>();

        if(myFiles.get(userId).size() != listOfOrder.size()){
            System.out.println("Eroare de reordonare in FileSystemStorageService:updateOrderOfFiles()");
        } else {
            for (Integer id : listOfOrder) {
                for (MyFile myFile : myFiles.get(userId)) {
                    if (myFile.getId() == id) {
                        myFilesNewOrder.add(myFile);
                    }
                }
            }
            myFiles.put(userId, myFilesNewOrder);
        }
    }

    public void updateSettingsOfFile(Long userId, List<MyFile> files){
        System.out.println("Updating...");
        for(MyFile myFile: files){
            System.out.println(myFile.getName());
        }
        if(files.size() == myFiles.get(userId).size()){
            myFiles.put(userId, files);
        }

    }

    public File generatePDF(Long userId, String downloadMethod) {
        LOGGER.log(Level.INFO, "PDF merger started");
        generationProgressBar = 0;
        File pdfFile = new File(getUserDirPath().toString() + "/output/GeneratedPDF.pdf");

        User existsUser = userService.getOne(userId);
        if (existsUser != null) {
            Document document = new Document();
            try {
                File outputDir = new File(getUserDirPath().toString() + "/output");
                if (!outputDir.exists()) {
                    outputDir.mkdir();
                }

                if (!pdfFile.exists())
                    pdfFile.createNewFile();
                else {
                    pdfFile.delete();
                    pdfFile.createNewFile();
                }
                PdfWriter pdfWriter = null;
                try {
                    pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
                } catch (FileNotFoundException fnfe) {
                    System.out.println("GenartedPDF.pdf: Fisier in uz");
                }
                //pdf settings: password, watermark
                PDFSettings settingsPDF = pdfSettingsService.getPDFSettings();
                if (settingsPDF.getPassword() != null && !settingsPDF.getPassword().equals("")) {
                    String owner = "Go4PDF";
                    pdfWriter.setEncryption(settingsPDF.getPassword().getBytes(), owner.getBytes(), 0, PdfWriter.ENCRYPTION_AES_128);
                }
                if ((settingsPDF.getImageWatermark() != null && settingsPDF.getImageWatermark().getWatermark() != null) ||
                        (settingsPDF.getTextWatermark() != null && settingsPDF.getTextWatermark().getWatermark() != null)) {
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

                for (MyFile myFile : myFiles.get(userId)) {
                    if (myFile.getSettings().isPageBreak()) {
                        document.newPage();
                    }
                    checkTempDirectory();
                    System.out.println(myFile.getName());
                    switch (myFile.getSettings().getType()) {
                        case "image":
                            Image image = applyImageSettings(myFile, document);
                            document.add(image);
                            break;

                        case "text":
                            document.add(Chunk.NEWLINE); // solution to image aligment TEXTWRAP
                            String FONT = "resources/fonts/FreeSans.ttf";
//                        Font font = FontFactory.getFont(FONT, "Cp1250", BaseFont.EMBEDDED);
//                        BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
//                        Paragraph pHeading = new Paragraph(new Chunk(new Phrase(org.apache.commons.io.FileUtils.readFileToString(myFile.getFile())).toString(), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL)));
                            //pHeading.setAlignment(Paragraph.ALIGN_RIGHT);
                            //document.add(pHeading);
                            document.add(new Phrase(org.apache.commons.io.FileUtils.readFileToString(myFile.getFile()))); // I used Phrase instead of Paragraph

                            break;

                        case "html":
                            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(myFile.getFile())));
                            String line = null;

                            StringBuilder htmlString = new StringBuilder();
                            while((line = in.readLine()) != null) {
                                htmlString.append(line);
                            }

                            InputStream is = new ByteArrayInputStream(htmlString.toString().getBytes());
                            XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, is);
                            break;

                        case "word":
                            String wordJsPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/", "") + "scripts/SaveWordAsPDF.js";
                            String wordToPDFPath = getUserTempDir() + FilenameUtils.getBaseName(myFile.getName()) + ".pdf";
                            System.out.println(wordToPDFPath);
                            if (fileToPDFAndMerge(myFile, wordJsPath, wordToPDFPath)) {
                                addPdf(document, pdfWriter, reader, new File(wordToPDFPath));
                            }
                            break;

                        case "excel":
                            String excelJsPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/", "") + "scripts/SaveExcelAsPDF.js";
                            String excelToPDFPath = getUserTempDir() + FilenameUtils.getBaseName(myFile.getName()) + myFile.getExtension() + ".pdf";
                            System.out.println(excelToPDFPath);
                            if (fileToPDFAndMerge(myFile, excelJsPath, excelToPDFPath)) {
                                addPdf(document, pdfWriter, reader, new File(excelToPDFPath));
                            }
                            break;

                        case "powerPoint":
                            String pptJsPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/", "") + "scripts/SavePowerPointAsPDF.js";
                            String pptToPDFPath = getUserTempDir() + FilenameUtils.getBaseName(myFile.getName()) + ".pdf";
                            System.out.println(pptToPDFPath);
                            if (fileToPDFAndMerge(myFile, pptJsPath, pptToPDFPath)) {
                                addPdf(document, pdfWriter, reader, new File(pptToPDFPath));
                            }
                            break;

                        case "pdf":
                            addPdf(document, pdfWriter, reader, myFile);
                            break;
                    }
                    generationProgressBar++;
                }
                document.close();
//            PdfReader pdfReader = new PdfReader(pdfFile.getAbsolutePath());
//            PdfStamper stamper = new PdfStamper(pdfReader, new FileOutputStream(pdfFile.getAbsolutePath()));
//            stamper.setEncryption(null, null, ~(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING), PdfWriter.STANDARD_ENCRYPTION_128);
//            stamper.close();
//            pdfReader.close();
                for (PdfReader readerItem : reader) {
                    readerItem.close();
                }

                if ("email".equals(downloadMethod)) {
                    System.out.println(existsUser.getEmail());
                    MailSender.sendMail(existsUser.getEmail(), pdfFile.getAbsolutePath());
                }
                //opening the file in default application (Acrobat Reader if installed)
                //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + pdfFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LOGGER.log(Level.INFO, "PDF merge complete.");
        return pdfFile;
    }

    private Image applyImageSettings(MyFile myFile, Document document) throws IOException, BadElementException {
        SettingsImage settingsImage = (SettingsImage) myFile.getSettings();
        if (settingsImage != null) {
            Image image = Image.getInstance(myFile.getFile().getPath());

            image.scalePercent(settingsImage.getScale());
            image.setRotationDegrees(settingsImage.getRotationDegrees());
            if (settingsImage.isFitToPage()){
                image.scaleToFit(PageSize.A4.getWidth() - (document.leftMargin() + document.rightMargin()), PageSize.A4.getHeight() - (document.topMargin() + document.bottomMargin()));
            }

            if (settingsImage.getWrappingStyle() != SettingsImage.ImageAlignment.None){
                image.setAlignment(settingsImage.getWrappingStyle().getValue());
            } else {
                //image watermark
                if (settingsImage.isAbsolutePosition()) {
                    image.setAbsolutePosition(settingsImage.getPositionAbsolute().getX(), settingsImage.getPositionAbsolute().getY());
                } else {
                    switch (settingsImage.getPositionPredefined()) {
                        case TOP_LEFT:
                            image.setAbsolutePosition(0,
                                    document.getPageSize().getHeight() - image.getScaledHeight());
                            break;
                        case TOP_CENTER:
                            image.setAbsolutePosition((document.getPageSize().getWidth() - image.getScaledWidth()) / 2,
                                    document.getPageSize().getHeight() - image.getScaledHeight());
                            break;
                        case TOP_RIGHT:
                            image.setAbsolutePosition(document.getPageSize().getWidth() - image.getScaledWidth(),
                                    document.getPageSize().getHeight() - image.getScaledHeight());
                            break;
                        case MIDDLE_LEFT:
                            image.setAbsolutePosition(0, (document.getPageSize().getHeight() - image.getScaledHeight()) / 2);
                            break;
                        case CENTER:
                            image.setAbsolutePosition((document.getPageSize().getWidth() - image.getScaledWidth()) / 2,
                                    (document.getPageSize().getHeight() - image.getScaledHeight()) / 2);
                            break;
                        case MIDDLE_RIGHT:
                            image.setAbsolutePosition(document.getPageSize().getWidth() - image.getScaledWidth(),
                                    (document.getPageSize().getHeight() - image.getScaledHeight()) / 2);
                            break;
                        case BOTTOM_LEFT:
                            image.setAbsolutePosition(0, 0);
                            break;
                        case BOTTOM_CENTER:
                            image.setAbsolutePosition((document.getPageSize().getWidth() - image.getScaledWidth()) / 2, 0);
                            break;
                        case BOTTOM_RIGHT:
                            image.setAbsolutePosition(document.getPageSize().getWidth() - image.getScaledWidth(), 0);
                            break;
                        default:
                            image.setAbsolutePosition((document.getPageSize().getWidth() - image.getScaledWidth()) / 2,
                                    (document.getPageSize().getHeight() - image.getScaledHeight()) / 2);
                    }
                }
            }
            return image;
        }
        return null;
    }

    private void addPdf(Document document, PdfWriter pdfWriter, List<PdfReader> reader, MyFile myFile) throws IOException {
        Rectangle originalPageSize = pdfWriter.getPageSize();
        document.newPage();
        PdfContentByte cb = pdfWriter.getDirectContent();
        reader.add(new PdfReader(myFile.getFile().getAbsolutePath()));
        if(reader.get(reader.size()-1).isEncrypted()){
            LOGGER.log(Level.WARNING, myFile.getFile().getName() + " is encrypted. Cannot perform merge operation.");
            reader.add(reader.size()-1,unlockPdf(reader.get(reader.size()-1)));
        }
        if (!reader.get(reader.size()-1).isEncrypted()){
            SettingsPDF pdfSettings = (SettingsPDF) myFile.getSettings();
            if (!pdfSettings.getPagesIncluded().equals("All")) {
                reader.get(reader.size() - 1).selectPages(pdfSettings.getPagesIncluded());
            }
            for (int i = 1; i <= reader.get(reader.size() - 1).getNumberOfPages(); i++) {
                Rectangle pageSize = reader.get(reader.size() - 1).getPageSize(i);
                document.setPageSize(pageSize);
                document.newPage();
                PdfImportedPage page = pdfWriter.getImportedPage(reader.get(reader.size() - 1), i);
                cb.addTemplate(page, 0, 0);
            }
            document.setPageSize(originalPageSize);
            document.newPage();
        }
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

    private boolean fileToPDFAndMerge(MyFile myFile, String jsPath, String fileToPDFPath) throws IOException, InterruptedException{
        File fileToPDFFile = new File(fileToPDFPath);
        if (fileToPDFFile.exists()) {
            fileToPDFFile.deleteOnExit();
            Thread.sleep(1000);
        }
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cscript.exe //nologo " + jsPath + " " + myFile.getFile().getAbsolutePath());

        StringBuilder processOutput = new StringBuilder();
        try (BufferedReader processOutputReader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));) {
            String outputLine;
            while ((outputLine = processOutputReader.readLine()) != null) {
                processOutput.append(outputLine).append(System.lineSeparator());
            }
            process.waitFor();
            if (processOutput.indexOf("Done.") != -1) {
                File ppt = new File(fileToPDFPath);
            } else {
                System.out.println("Eroare conversie fisier");
                return false;
            }
        }

        return true;
    }

    public static PdfReader unlockPdf(PdfReader reader) {
        if (reader == null) {
            return reader;
        }
        try {
            Field f = reader.getClass().getDeclaredField("encrypted");
            f.setAccessible(true);
            f.set(reader, false);
            LOGGER.log(Level.INFO, "Document unlocked for edit mode");
        } catch (Exception e) { // ignore
            LOGGER.log(Level.INFO, "Document could not be unlocked for edit mode");
        }
        return reader;
    }
}