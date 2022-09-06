package com.Carrot.CR_Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class SaleProduct {
    private int postId;
    private String id;
    private String title;
    private String category;
    private int price;
    private String content;
    private String status;
    private Timestamp createDate;
    private Timestamp updateDate;
    private int love;
    private String filePath;
    private String fileDownloadPath;
    private List<Photo_SaleProduct> file;

    public SaleProduct(int postId, String id, String title, String category, int price, String content, String status, Timestamp createDate, Timestamp updateDate, int love, String filePath, String fileDownloadPath) {
        this.postId = postId;
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
        this.content = content;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.love = love;
        this.filePath = filePath;
        this.fileDownloadPath = fileDownloadPath;
    }
}
