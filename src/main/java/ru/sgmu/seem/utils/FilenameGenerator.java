package ru.sgmu.seem.utils;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.UUID;
import java.util.Random;

@Component
public class FilenameGenerator {

    public String nextString() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
