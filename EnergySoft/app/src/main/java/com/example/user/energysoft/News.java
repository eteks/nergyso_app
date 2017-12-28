package com.example.user.energysoft;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by root on 9/12/17.
 */

public class News {
    String news_title;
    String news_description;
    String news_image;
    String news_video;
    String news_document;
    int id;

    public News() {
    }

    public News(String title) {
        this.news_title = title;
    }

    public String getTitle() {
        System.out.println("inside getTitle"+news_title);
        return news_title;
    }

    public int getId() {
        System.out.println("inside getTitle"+id);
        return id;
    }



    public String getNews_description() {
        return news_description;
    }

    public String getNews_image(){
        return news_image;
    }

//    public news(String news_title, String news_description, String news_image, String news_video, String news_document){
//        this.setTitle(news_title);
//        this.setDescription(news_description);
//        this.setImage(news_image);
//        this.setVideo(news_video);
//        this.setDocument(news_document);
//    }

    public void setTitle(String text){
        System.out.println("inside setTitle"+news_title);
        this.news_title = text;
    }

    public void setId(int id){
        System.out.println("inside setTitle"+id);
        this.id = id;
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

//    public static List<News> createMovies(int itemCount) {
//        List<News> newsList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            News news = new News();
//            news.news_image = "";
//            news.news_title = "Title "+ (itemCount == 0 ?
//                    (itemCount + 1 + i) : (itemCount + i));
//            news.news_description = "Description" + i;
//            newsList.add(news);
//        }
//        return newsList;
//    }

}

