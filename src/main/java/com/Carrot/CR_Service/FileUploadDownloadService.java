package com.Carrot.CR_Service;

import com.File.FileDownloadException;
import com.File.FileUploadException;
import com.File.FileUploadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadDownloadService {
    private final Path fileLocation;

    @Autowired
    public FileUploadDownloadService(FileUploadProperties fileUploadProperties) {
        this.fileLocation = Paths.get(fileUploadProperties.getUploadDir()).toAbsolutePath(). normalize();

        try {
            Files.createDirectories(this.fileLocation);
        } catch(Exception e) {
            throw new FileUploadException("File Directory maked failed");
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(getUUIDFileName(file.getOriginalFilename()));

        try {
            if(fileName.contains(".."))
                throw new FileUploadException("File Name is not visible");
            Path targetLocation = this.fileLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();
        } catch(Exception e) {
            throw new FileUploadException("["+fileName+"] File upload failed");
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists())
                return resource;
            else
                throw new FileDownloadException(fileName + "File Not Found");
        } catch(MalformedURLException e) {
            throw new FileDownloadException(fileName + " File Not Found");
        }
    }


    private String getUUIDFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + extension;
    }
}
