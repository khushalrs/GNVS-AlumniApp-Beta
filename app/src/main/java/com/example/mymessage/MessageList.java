package com.example.mymessage;

public class MessageList {
    private String messageText;
    private String name;
    private String time;

    public MessageList(){}

    public MessageList(String messageText, String name, String time){
        this.messageText = messageText;
        this.name = name;
        this.time = time;
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
}
