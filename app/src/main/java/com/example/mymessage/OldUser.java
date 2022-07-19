package com.example.mymessage;

public class OldUser {
    private String Name;
    private String Email;
    private String Phone;
    private int Joining;
    private int Graduate;

    public OldUser(){}

    public OldUser(String Email, String Name, String Phone, int Joining, int Graduate) {
        this.Email = Email;
        this.Name = Name;
        this.Phone = Phone;
        this.Joining = Joining;
        this.Graduate = Graduate;
    }

    public String getEmail(){
        return Email;
    }
    public String getPhone(){
        return Phone;
    }
    public String getName(){
        return Name;
    }
    public int getJoining(){
        return Joining;
    }
    public int getGraduate(){
        return Graduate;
    }
}
