package com.Carrot.CR_Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Builder
@AllArgsConstructor
@Data
public class SaleProduct {
    private Double postId;
    private String id;
    private String title;
    private String category;
    private int price;
    private String content;
    private String image;
    private String status;
    private Timestamp createDate;
    private Timestamp updateDate;
    private int love;
}
