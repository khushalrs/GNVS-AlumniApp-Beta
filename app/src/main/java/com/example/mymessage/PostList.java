package com.example.mymessage;

public class PostList {

    private String date;
    private String datetime;
    private String description;
    private String image;
    private String name;
    private String time;
    private String userId;

    public PostList(){}

    public PostList(String date, String datetime, String time, String image, String description,String userId, String name){
        this.date = date;
        this.datetime = datetime;
        this.time = time;
        this.image = image;
        this.name = name;
        this.description = description;
        this.userId = userId;

        }

    public String getDate(){return date;}
    public String getDatetime(){
        return datetime;
    }
    public String getImage(){
        return image;
    }
    public String getDescription(){
        return description;
    }
    public String getName(){
        return name;
    }
    public String getUserId(){
        return userId;
    }
    public String getTime(){
        return time;
    }




}
