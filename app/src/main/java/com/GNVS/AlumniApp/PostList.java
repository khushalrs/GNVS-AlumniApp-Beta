package com.GNVS.AlumniApp;

import java.util.ArrayList;

public class PostList {

    private String date;
    private String datetime;
    private String description;
    private String image;
    private String name;
    private String time;
    private String userId;
    private String ref1;
    private String ref2;
    private ArrayList<String> like;

    public PostList(){}

    public PostList(String date, String datetime, String time, String image, String description,String userId, String name, String ref1, String ref2){
        this.date = date;
        this.datetime = datetime;
        this.time = time;
        this.image = image;
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.ref1 = ref1;
        this.ref2 = ref2;
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
    public String getRef1(){
        return ref1;
    }
    public String getRef2(){
        return ref2;
    }
    public ArrayList<String> getLikeId(){
        return like;
    }
    public void addLikeId(ArrayList<String> likes){
        like = likes;
    }

}

