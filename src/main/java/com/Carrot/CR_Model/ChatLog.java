package com.Carrot.CR_Model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class ChatLog {
    private int chatId;
    private String sender;
    private String receiver;
    private String conversation;
    private Timestamp senDate;
}
