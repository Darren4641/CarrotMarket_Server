package com.Carrot.CR_Model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Reply {
    private int replyId;
    private int commId;
    private int postId;
    private String id;
    private String comm_id;
    private String reply_id;
    private String content;
    private Timestamp createDate;
    private int love;
}
