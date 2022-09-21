package com.Carrot.CR_Service;

import com.Carrot.CR_Model.SaleProduct;
import com.Carrot.ErrorHandler.ApiResponse;
import com.Carrot.Repository.Like.LikeRepository;
import com.Carrot.Repository.Like.LikeRepositoryImpl;
import com.Carrot.Repository.PhotoRepository.PhotoRepository;
import com.Carrot.Repository.PhotoRepository.PhotoRepositoryImpl;
import com.Carrot.Repository.SaleProduct.SaleProductRepository;
import com.Carrot.Repository.SaleProduct.SaleProductRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaleProductService {

    private final SaleProductRepository saleProductRepository;
    private final PhotoRepository photoRepository;
    private final LikeRepository likeRepository;
    @Autowired
    public SaleProductService(SaleProductRepositoryImpl saleProductRepository, PhotoRepositoryImpl photoRepository, LikeRepositoryImpl likeRepository) {
        this.saleProductRepository = saleProductRepository;
        this.photoRepository = photoRepository;
        this.likeRepository = likeRepository;
    }

    public SaleProduct write(SaleProduct saleProduct) {
        int status = saleProductRepository.save(saleProduct);
        return saleProductRepository.findById(status).get();
    }

    public SaleProduct update(SaleProduct saleProduct) {
        int status = saleProductRepository.update(saleProduct.getPostId(), saleProduct);
        boolean result = (status != 0);
        return result ? saleProductRepository.findById(status).get() : null;
    }

    public ApiResponse findByIdWithFileAndLike(int postId) {
        ApiResponse.ResponseMap result = new ApiResponse.ResponseMap();
        Optional<SaleProduct> Op_saleProduct = saleProductRepository.findById(postId);
        if(Op_saleProduct.isPresent()) {
            SaleProduct saleProduct = Op_saleProduct.get();
            saleProduct.setFile(photoRepository.findByPostIdAndCategory(postId, "saleProduct"));
            saleProduct.setLike(likeRepository.countOfLike(postId, "saleProduct"));
            result.setResult(saleProduct);
        }
        return result;
    }

    public ApiResponse pushLike(int postId, String id) {
        ApiResponse.ResponseMap result = new ApiResponse.ResponseMap();
        boolean validate = (validateLike(postId, id) != 0);
        if(validate) {
            //delete
        }else {
            //insert
        }
        return result;
    }

    public int validateLike(int postId, String id) {
        return likeRepository.countOfLikeById(postId, "saleProduct", id);
    }



    public SaleProduct findSaleProduct(int postId) {
        Optional<SaleProduct> saleProduct = saleProductRepository.findById(postId);
        return saleProduct.orElse(null);
    }

    public ApiResponse getPage(int limit, int offset, int pageNum) {
        ApiResponse.ResponseMap result = new ApiResponse.ResponseMap();
        result.setResponseData("page", pageNum);
        result.setResponseData("saleProductList", saleProductRepository.findPageCount(limit, offset));
        return result;
    }

}
