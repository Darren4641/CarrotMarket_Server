package com.Carrot.Repository.SaleProduct;

import com.Carrot.CR_Model.SaleProduct;

import java.util.List;
import java.util.Optional;

public interface SaleProductRepository {
    Double save(SaleProduct saleProduct);
    int update(int postId, SaleProduct saleProduct);
    List<SaleProduct> findAll();
    Optional<SaleProduct> findById(Double postId);
    List<SaleProduct> findByTitleOrContent(String title);
    List<SaleProduct> findPageCount(int limit, int offset);
}
