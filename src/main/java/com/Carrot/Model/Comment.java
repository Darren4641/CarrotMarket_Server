package com.Carrot.Model;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Long commId;
    private Long postId;
    private String id;
    private String comm_Id;
    private String content;
    private Date createDate;
    private int love;
}
