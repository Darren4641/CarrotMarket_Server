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
    public Photo_SaleProduct uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("id") String id, @RequestParam("postId") int postId) {
        return fileUploadDownloadService.storeFile(file, "saleProduct", id, postId);
    }

    @PostMapping("/uploadMultipleFiles")
    public List<Photo_SaleProduct> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("id") String id, @RequestParam("postId") int postId) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, id, postId))
                .collect(Collectors.toList());

    }


    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request, @PathVariable String fileName) {
        return fileUploadDownloadService.loadFileAsResource(request, fileName);



    }



}
