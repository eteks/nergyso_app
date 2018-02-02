package com.vdart.apps.app;

/**
 * Created by root on 9/12/17.
 */

public class News {
    String news_title;
    String news_description;
    String news_image;
    String news_video;
    String news_document;
    String news_date;

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

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

    public String getNews_video() {
        return news_video;
    }

    public void setNews_video(String news_video) {
        this.news_video = news_video;
    }

    public String getNews_document() {
        return news_document;
    }

    public void setNews_document(String news_document) {
        this.news_document = news_document;
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

