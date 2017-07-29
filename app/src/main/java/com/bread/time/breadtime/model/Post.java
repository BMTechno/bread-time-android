package com.bread.time.breadtime.model;


public class Post {

    private String title;
    private String date;
    private String category;
    private String author;
    private String content;
    private int views;
    private String thumbnail;

    public Post(){

    }

    public Post(String title, String date, String category, String author, String content,
                int views, String thumbnail) {
        this.title = title;
        this.date = date;
        this.category = category;
        this.author = author;
        this.content = content;
        this.views = views;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}