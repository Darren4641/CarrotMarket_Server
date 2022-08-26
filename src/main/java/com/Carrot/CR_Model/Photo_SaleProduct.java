package com.Carrot.CR_Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Photo_SaleProduct {
    //private int photoId;
    private String category;
    private int postId;
    private String id;
    private String fileName;
    private String filePath;
    private String fileDownloadPath;
    private long fileSize;


}
