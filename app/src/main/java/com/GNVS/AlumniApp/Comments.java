package com.GNVS.AlumniApp;

public class Comments {
    private String userId;
    private String name;
    private String commentText;
    private String date;
    private String time;
    private String datetime;

    public Comments(){}

    public Comments(String userId, String name, String commentText, String date, String time, String datetime){
        this.userId = userId;
        this.name = name;
        this.commentText = commentText;
        this.date = date;
        this.time = time;
        this.datetime = datetime;
    }

    public String getUserId(){
        return userId;
    }

    public String getName(){
        return name;
    }

    public String getCommentText(){
        return commentText;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public String getDatetime(){
        return datetime;
    }

}
