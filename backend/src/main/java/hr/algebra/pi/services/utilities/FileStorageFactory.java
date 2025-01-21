package hr.algebra.pi.services.utilities;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileStorageFactory {
    private static FileStorageFactory instance;

    private final String storageDirectory = "uploads";

    private FileStorageFactory() {
        File directory = new File(storageDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static FileStorageFactory getInstance() {
        if (instance == null) {
            instance = new FileStorageFactory();
        }
        return instance;
    }

    public Path storeFile(MultipartFile file) throws IOException {
        Path filePath = Paths.get(storageDirectory, file.getOriginalFilename());
        Files.write(filePath, file.getBytes());
        return filePath;
    }
}
