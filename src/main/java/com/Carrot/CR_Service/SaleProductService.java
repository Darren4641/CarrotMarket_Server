package com.Carrot.CR_Service;

import com.Carrot.CR_Model.SaleProduct;
import com.Carrot.Repository.SaleProduct.SaleProductRepository;
import com.Carrot.Repository.SaleProduct.SaleProductRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleProductService {

    private final SaleProductRepository saleProductRepository;

    public SaleProductService(SaleProductRepositoryImpl saleProductRepository) {
        this.saleProductRepository = saleProductRepository;
    }

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
