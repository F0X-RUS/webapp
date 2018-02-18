package ru.sgmu.seem.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.sgmu.seem.utils.FolderManager;

import java.util.Locale;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"ru.sgmu.seem.utils", "ru.sgmu.seem.webapp"})
public class WebappApplication {

    public static void main(String[] args) {
        Locale.setDefault(new Locale("ru", "RU"));
        FolderManager.createImageFolder();
        SpringApplication.run(WebappApplication.class, args);
    }
}
