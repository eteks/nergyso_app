package com.vdart.apps.app;

/**
 * Created by root on 9/12/17.
 */

public class Event {
    String events_title;
    String events_description;
    String events_image;
    String events_video;
    String events_document;
    String events_venue;
    String events_location;
    String type;
    int id;
    String events_date;

    public String getEvents_video() {
        return events_video;
    }

    public void setEvents_video(String events_video) {
        this.events_video = events_video;
    }

    public String getEvents_document() {
        return events_document;
    }

    public void setEvents_document(String events_document) {
        this.events_document = events_document;
    }

    public String getEvents_date() {
        return events_date;
    }

    public void setEvents_date(String events_date) {
        this.events_date = events_date;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getTitle() {
        System.out.println("inside getTitle"+events_title);
        return events_title;
    }

    public String getEvents_description() {
        System.out.println("inside getTitle"+events_description);
        return events_description;
    }

    public int getId() {
        System.out.println("inside getTitle"+id);
        return id;
    }

    public String getEvents_image() {
        System.out.println("inside getTitle"+events_image);
        return events_image;
    }


//    public news(String news_title, String news_description, String news_image, String news_video, String news_document){
//        this.setTitle(news_title);
//        this.setDescription(news_description);
//        this.setImage(news_image);
//        this.setVideo(news_video);
//        this.setDocument(news_document);
//    }

    public void setTitle(String text){
        System.out.println("inside setTitle"+events_title);
        this.events_title = text;
    }

    public void setId(int id){
        System.out.println("inside setTitle"+id);
        this.id = id;
    }


    public void setDescription(String text){
        events_description = text;
    }

    public void setImage(String text){
        events_image = text;
    }

    public void setVideo(String text){
        events_video = text;
    }

    public void setDocument(String text){
        events_document = text;
    }
}

