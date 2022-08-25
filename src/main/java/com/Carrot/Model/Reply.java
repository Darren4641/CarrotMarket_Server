package com.Carrot.Model;

import lombok.Data;

import java.util.Date;

@Data
public class Reply {
    private Long replyId;
    private Long commId;
    private Long postId;
    private String id;
    private String comm_id;
    private String reply_id;
    private String content;
    private Date createDate;
    private int love;
}
