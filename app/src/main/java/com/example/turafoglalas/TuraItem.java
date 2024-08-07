package com.example.turafoglalas;

public class TuraItem {
    private String id;
    private String name;
    private String length;
    private String date;
    private String price;


    public TuraItem(){

    }

    public TuraItem(String name, String length, String date, String price) {
        this.name = name;
        this.length = length;
        this.date = date;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getLength() {
        return length;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String _getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
