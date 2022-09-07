package com.Carrot.CR_Service;

import com.Carrot.CR_Model.Photo_SaleProduct;
import com.Carrot.Repository.PhotoRepository.PhotoRepository;
import com.File.FileDownloadException;
import com.File.FileUploadException;
import com.File.FileUploadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadDownloadService {
    private final Path fileLocation;
    private final PhotoRepository photoRepository;

    @Autowired
    public FileUploadDownloadService(FileUploadProperties fileUploadProperties, PhotoRepository photoRepository) {
        this.fileLocation = Paths.get(fileUploadProperties.getUploadDir()).toAbsolutePath(). normalize();
        this.photoRepository = photoRepository;
        try {
            Files.createDirectories(this.fileLocation);
        } catch(Exception e) {
            throw new FileUploadException("File Directory maked failed");
        }
    }

    public Photo_SaleProduct storeFile(MultipartFile file, String category, String id, int postId) {

        Photo_SaleProduct photo_saleProduct;
        if(file == null)
            photo_saleProduct = buildPhoto(
                    id, category, postId, "null", "null", "null", "null", 0);
        else
            photo_saleProduct = uploadFile(file, category, id, postId);

        photoRepository.save(photo_saleProduct);

        return photo_saleProduct;
    }

    public Photo_SaleProduct updateFile(MultipartFile file, String category, String id, int postId) {

        Photo_SaleProduct photo_saleProduct;
        deleteFile(postId, category);
        if(file == null)
            photo_saleProduct = buildPhoto(
                    id, category, postId, "null", "null", "null", "null", 0);
        else
            photo_saleProduct = uploadFile(file, category, id, postId);

        photoRepository.save(photo_saleProduct);

        return photo_saleProduct;
    }

    public ResponseEntity<Resource> loadFileAsResource(HttpServletRequest request, String fileName) {
        try {
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            String OriginFileName = getOriginFileName(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                String contentType = getContentType(request, resource);

                if(contentType == null) {
                    System.out.println("ContentType is Null");
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + OriginFileName + "\"")
                        .body(resource);
            } else
                throw new FileDownloadException(fileName + "File Not Found");
        } catch(MalformedURLException e) {
            throw new FileDownloadException(fileName + " File Not Found");
        }
    }

    public String getOriginFileName(String uuid) {
        return photoRepository.findByPostUuId(uuid).get().getFileName();
    }

    private String getContentType(HttpServletRequest request, Resource resource) {
        try {
            return request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getFileDownloadUri(String downloadPath, String uuidFileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(downloadPath)
                .path(uuidFileName)
                .toUriString();
    }

    private String getUUIDFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + extension;
    }

    private Photo_SaleProduct buildPhoto(String id, String category, int postId, String originFileName, String uuidFileName, String filePath, String fileDownloadUri, long bytes) {
        return Photo_SaleProduct.builder()
                .category(category)
                .postId(postId)
                .id(id)
                .fileName(originFileName)
                .uuid(uuidFileName)
                .filePath(filePath)
                .fileDownloadPath(fileDownloadUri)
                .fileSize(bytes).build();
    }

    private Photo_SaleProduct uploadFile(MultipartFile file, String category, String id, int postId) {
        String originFileName = file.getOriginalFilename();
        String fileName = StringUtils.cleanPath(getUUIDFileName(originFileName));
        try {
            if(fileName.contains(".."))
                throw new FileUploadException("File Name is not visible");
            Path targetLocation = this.fileLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String filePath = targetLocation.toString();
            File FileOfFilePath = new File(filePath);
            long bytes = (FileOfFilePath.length() / 1024);
            String uuidFileName = FileOfFilePath.getName();
            String fileDownloadUri = getFileDownloadUri("/downloadFile/", uuidFileName);

            return buildPhoto(
                    id, category, postId, originFileName, uuidFileName, filePath, fileDownloadUri, bytes);
        } catch(Exception e) {
            e.printStackTrace();
            throw new FileUploadException("["+fileName+"] File upload failed");
        }
    }

    private void deleteFile(long postId, String category) {
        List<Photo_SaleProduct> photo_saleProductList = photoRepository.findByPostIdAndCategory(postId, category);

        for(Photo_SaleProduct list : photo_saleProductList) {
            File listOfFile = new File(list.getFilePath());
            if(listOfFile.exists())
                listOfFile.delete();
            photoRepository.deleteByPostIdAndCategory(postId, category);
        }
    }
}
