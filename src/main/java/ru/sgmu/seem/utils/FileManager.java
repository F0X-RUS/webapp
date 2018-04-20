package ru.sgmu.seem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileManager {

    private final FilenameGenerator filenameGenerator;

    @Autowired
    public FileManager(FilenameGenerator filenameGenerator){
        this.filenameGenerator = filenameGenerator;
    }

    public void create(MultipartFile file, String path, String filename) throws IOException {
        try {
            if (!file.isEmpty()) {
                Files.copy(file.getInputStream(), Paths.get(path, filename));
            }
        } catch (FileAlreadyExistsException faee) {
            faee.printStackTrace();
        }
    }

    public void delete(Path filepath) throws IOException {
        if (Files.exists(filepath)) {
            Files.deleteIfExists(filepath);
        }
    }

    public String getName(Path folder){
        String filename;
        do {
            filename = filenameGenerator.nextString();
        }while (Files.exists(Paths.get(folder.toString(), filename)));
        return filename;
    }

    public String getExtension(MultipartFile file){
        return file.getContentType().split("/")[1].toUpperCase();
    }
}
