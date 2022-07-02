package com.example.mymessage;

public class MessageList {
    private String messageText;
    private String email;
    private String time;
    private String date;
    private String dateTime;
    private int read;

    public MessageList(){}

    public MessageList(String messageText, String email, String time, String date, String dateTime, int read){
        this.messageText = messageText;
        this.email = email;
        this.time = time;
        this.date = date;
        this.read = read;
        this.dateTime = dateTime;
    }

    public String getMessageText(){
        return messageText;
    }
    public String getEmail(){
        return email;
    }
    public String getTime(){
        return time;
    }
    public String getDate() {
        return date;
    }
    public int getRead(){
        return read;
    }
    public String getDateTime(){
        return dateTime;
    }
}
