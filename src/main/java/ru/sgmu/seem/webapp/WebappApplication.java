package ru.sgmu.seem.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.sgmu.seem.utils.FolderManager;

@SpringBootApplication
@EnableAutoConfiguration
public class WebappApplication {

    public static void main(String[] args) {
        FolderManager folderManager = FolderManager.getInstance();
        folderManager.createImageFolder();
        SpringApplication.run(WebappApplication.class, args);
    }
}
