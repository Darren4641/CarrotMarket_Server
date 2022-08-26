package com.Carrot.CR_Model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Comment {
    private int commId;
    private int postId;
    private String id;
    private String comm_Id;
    private String content;
    private Timestamp createDate;
    private int love;
}
