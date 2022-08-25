package com.Carrot.Model;

import lombok.Data;

import java.util.Date;

@Data
public class ChatLog {
    private Long chatId;
    private String sender;
    private String receiver;
    private String conversation;
    private Date senDate;
}
