package com.Carrot.CR_Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Like {
    private int likeId;
    private String id;
    private String category;
    private int postId;
    private int isLike;
}
