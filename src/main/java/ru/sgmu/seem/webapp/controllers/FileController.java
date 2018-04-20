package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sgmu.seem.webapp.services.FileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;

import static ru.sgmu.seem.utils.FolderManager.threadFilesPath;

@Controller
@PreAuthorize("hasAnyRole('USER','TEACHER','MODER','ADMIN')")
public class FileController {

    private static final String FILENAME = "{filename:.+}";

    private final ResourceLoader resourceLoader;
    private FileService fileService;

    @Autowired
    public FileController(ResourceLoader resourceLoader,
                          FileService fileService) {
        this.resourceLoader = resourceLoader;
        this.fileService = fileService;
    }

    private ResponseEntity<?> findOne(String filename, String path) throws NullPointerException {
        try {
            ru.sgmu.seem.webapp.domains.File file = fileService.getAll().stream()
                    .filter(e -> e.getName().substring(0,e.getName().indexOf(".")).equals(filename.substring(0,filename.indexOf("."))))
                    .findFirst().orElse(null);
            Resource resource = resourceLoader.getResource("file:" + path + "/" + filename);
            return ResponseEntity.ok().contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-disposition", "attachment; filename="+ file.getOriginName())
                    .body(new InputStreamResource(resource.getInputStream()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return ResponseEntity.badRequest().body("Файл не найден!");
        } catch (NullPointerException npe){
            npe.printStackTrace();
            return ResponseEntity.badRequest().body("Ошибка при попытке загрузки файла!");
        }
    }

    @RequestMapping(value = "files/thread/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> threadFile(@PathVariable String filename) {
        return findOne(filename, threadFilesPath.toString());
    }
}
