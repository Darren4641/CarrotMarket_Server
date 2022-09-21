package com.Carrot.Repository.Like;

import com.Carrot.CR_Model.Like;
import com.Carrot.CR_Model.SaleProduct;

import java.util.List;

public interface LikeRepository {
    int save(Like like);
    int update(Like like);
    List<Like> findByPostIdAndCategory(int postId, String category);
    int countOfLike(int postId, String category);
}
