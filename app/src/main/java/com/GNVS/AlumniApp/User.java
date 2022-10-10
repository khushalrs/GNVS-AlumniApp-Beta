package com.GNVS.AlumniApp;

public class User {
    private String email;
    private String password;
    private String name;
    private String job;
    private String company;
    private String batch;
    private String phone;
    private String propic;

    public User(){

    }

    public User(String email, String password, String name, String job, String company, String batch, String phone, String propic) {
        this.email = email;
        this.password = password;
        this.batch = batch;
        this.name = name;
        this.job = job;
        this.company = company;
        this.phone = phone;
        this.propic = propic;
    }



    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getName(){
        return name;
    }
    public String getJob(){
        return job;
    }
    public String getCompany(){
        return company;
    }
    public String getBatch(){
        return batch;
    }
    public String getPhone(){
        return phone;
    }
    public String getPropic(){
        return propic;
    }

}
