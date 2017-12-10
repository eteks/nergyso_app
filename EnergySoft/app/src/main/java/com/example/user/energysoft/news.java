package com.example.user.energysoft;

/**
 * Created by root on 9/12/17.
 */

public class news {
    public String news_title;
    public String news_description;
    public String news_image;
    public String news_video;
    public String news_document;

//    public news(String news_title, String news_description, String news_image, String news_video, String news_document){
//        this.setTitle(news_title);
//        this.setDescription(news_description);
//        this.setImage(news_image);
//        this.setVideo(news_video);
//        this.setDocument(news_document);
//    }

    public void setTitle(String text){
        news_title = text;
    }

    public void setDescription(String text){
        news_description = text;
    }

    public void setImage(String text){
        news_image = text;
    }

    public void setVideo(String text){
        news_video = text;
    }

    public void setDocument(String text){
        news_document = text;
    }
}

