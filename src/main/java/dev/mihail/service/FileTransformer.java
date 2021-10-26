package dev.mihail.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileTransformer {

    public String fileChange(String filePath) throws IOException {

        String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
        return "Hey welcome to your destination: " + fileContent;
    }
}
