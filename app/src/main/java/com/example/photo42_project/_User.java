package com.example.photo42_project;

public class _User {
    private String name, secondName, email, login, phone;
    private String sex;

    private int year, month, day, exp, stage;

    private Boolean isnice = false;

    public _User(){}
    public _User(int year, int month, int day, String sex, int exp, int stage,
                 String name, String secondName, String email, String login, String phone){
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.login = login;
        this.phone = phone;
        this.year = year;
        this.month = month;
        this.day = day;
        this.exp = exp;
        this.sex = sex;
        this.stage = stage;
    }

    public int getStage(){
        return stage;
    }
    public void setStage(int stage){
        this.stage = stage;
    }
    public int getYear(){
        return year;
    }
    public void setYear(int year){
        this.year = year;
    }
    public int getMonth(){
        return month;
    }
    public void setMonth(int month){
        this.month = month;
    }
    public int getDay(){
        return day;
    }
    public void setDay(int day){
        this.day = day;
    }
    public String getSex(){
        return sex;
    }
    public void setSex(String sex){ this.sex = sex; }
    public int getExp(){
        return exp;
    }
    public void setExp(int exp){
        this.exp = exp;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getSecondName(){
        return secondName;
    }
    public void setSecondName(String secondName){
        this.secondName = secondName;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getLogin(){
        return login;
    }
    public void setLogin(String login){
        this.login = login;
    }
}
