package com.Carrot.CR_Controller;

import com.Carrot.CR_Model.Photo_SaleProduct;
import com.Carrot.CR_Service.FileUploadDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class FileController {

    @Autowired
    private FileUploadDownloadService fileUploadDownloadService;

    @PostMapping("/uploadFile")
    public Photo_SaleProduct uploadFile(@RequestParam("file") MultipartFile file) {
        String originFileName = file.getOriginalFilename();

        String filePath = fileUploadDownloadService.storeFile(file);

        File fileFullPath = new File(filePath);
        long bytes = (fileFullPath.length() / 1024);

        String uuidFileName = fileFullPath.getName();

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(uuidFileName)
                .toUriString();

        return Photo_SaleProduct.builder()
                .category("saleProduct")
                .postId(1)
                .id("zxz4641")
                .fileName(originFileName)
                .filePath(filePath)
                .fileDownloadPath(fileDownloadUri)
                .fileSize(bytes).build();
    }

    @PostMapping("/uploadMultipleFiles")
    public List<Photo_SaleProduct> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());

    }


    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileUploadDownloadService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(contentType == null) {
            System.out.println("ContentType is Null");
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }



}
