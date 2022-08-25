package com.Carrot.CR_Model;

import lombok.Data;

import java.util.Date;

@Data
public class SaleProduct {
    private Long postId;
    private String id;
    private String title;
    private String category;
    private int price;
    private String content;
    private String text;
    private String status;
    private Date createDate;
    private Date updateDate;
    private int love;
}
