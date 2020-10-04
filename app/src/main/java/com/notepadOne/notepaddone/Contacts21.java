package com.notepadOne.notepaddone;

public class Contacts21 {
    int _id;
    String title;
    byte[] img;
    String date;
    public Contacts21(){

    }
    public Contacts21(int id, String title , byte[] img, String date){
        this._id = id;
        this.title = title;
        this.img=img;
        this.date = date;
    }

    public int getID(){
        return this._id;
    }
    public void setID(int id){
        this._id = id;
    }

    public String getWordName(){
        return this.title;
    }
    public void setWordName(String title){
        this.title = title;
    }

    public byte[] getMean(){
        return this.img;
    }
    public void setMean(byte[] mean){
        this.img = img;
    }

    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }

}

