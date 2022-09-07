package com.Carrot.Repository.PhotoRepository;

import com.Carrot.CR_Model.Photo_SaleProduct;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository {
    long save(Photo_SaleProduct photo_saleProduct);
    int update(int photoId, String category, Photo_SaleProduct photo_saleProduct);
    List<Photo_SaleProduct> findByPostIdAndCategory(long postId, String category);
    Optional<Photo_SaleProduct> findByPostUuId(String uuid);
    void deleteByPostIdAndCategory(long postId, String category);
}
