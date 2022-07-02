package com.example.mymessage;

public class User {
    private final String email;
    private final String password;
    private final String name;
    private final String job;
    private final String company;
    private final String batch;

    public User(String email, String password, String name, String job, String company, String batch) {
        this.email = email;
        this.password = password;
        this.batch = batch;
        this.name = name;
        this.job = job;
        this.company = company;
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

}
