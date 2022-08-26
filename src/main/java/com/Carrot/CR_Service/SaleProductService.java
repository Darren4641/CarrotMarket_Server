package com.Carrot.CR_Service;

import com.Carrot.CR_Model.SaleProduct;
import com.Carrot.Repository.SaleProduct.SaleProductRepository;
import com.Carrot.Repository.SaleProduct.SaleProductRepositoryImpl;
import com.File.FileUploadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class SaleProductService {

    private final SaleProductRepository saleProductRepository;

    //@Autowired
    public SaleProductService(SaleProductRepositoryImpl saleProductRepository, FileUploadProperties fileUploadProperties) {
        this.saleProductRepository = saleProductRepository;
    }

    /*public String saveFile(MultipartFile multipartFile) {
            String fileName = multipartFile.getOriginalFilename();
            Path location = this.dirLocation.resolve(fileName);
            try {
            Files.copy(multipartFile.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public Resource loadFile(String fileName) throws FileNotFoundException {
        try {
            Path file = this.dirLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable())
                return resource;
        } catch(MalformedURLException e) {
            throw new FileNotFoundException("Could not download file");
        }
        return null;
    }*/

    public SaleProduct write(SaleProduct saleProduct) {

        Double status = saleProductRepository.save(saleProduct);
        return saleProductRepository.findById(status).get();
    }

    public SaleProduct update(SaleProduct saleProduct) {
        int status = saleProductRepository.update(saleProduct.getPostId(), saleProduct);
        boolean result = (status != 0);
        return result ? saleProduct : null;
    }

    public List<SaleProduct> getPage(int limit, int offset) {
        return saleProductRepository.findPageCount(limit, offset);
    }

}
