package com.Carrot.Repository.SaleProduct;

import com.Carrot.CR_Model.SaleProduct;

import java.util.List;
import java.util.Optional;

public interface SaleProductRepository {
    int save(SaleProduct saleProduct);
    int update(int postId, SaleProduct saleProduct);
    int updateForUpdateDate(int postId);
    List<SaleProduct> findAll();
    Optional<SaleProduct> findById(int postId);
    Optional<SaleProduct> findByIdJoinPhoto(int postId);
    List<SaleProduct> findByTitleOrContent(String title);
    List<SaleProduct> findPageCount(int limit, int offset);
}
