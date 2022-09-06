package com.Carrot.Repository.PhotoRepository;

import com.Carrot.CR_Model.Photo_SaleProduct;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository {
    long save(Photo_SaleProduct photo_saleProduct);
    int update(int photoId, Photo_SaleProduct photo_saleProduct);
    List<Photo_SaleProduct> findByPostId(long postId);
    Optional<Photo_SaleProduct> findByPostUuId(String uuid);

}
