package com.example.mymessage;

public class MessageList {
    private String messageText;
    private String name;
    private String time;
    private int type;

    public MessageList(){}

    public MessageList(String messageText, String name, String time, int type){
        this.messageText = messageText;
        this.name = name;
        this.time = time;
        this.type = type;
    }

    public String getMessageText(){
        return messageText;
    }
    public String getName(){
        return name;
    }
    public String getTime(){
        return time;
    }
    public int getType(){
        return type;
    }
}
