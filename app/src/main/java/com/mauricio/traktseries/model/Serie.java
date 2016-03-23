package com.mauricio.traktseries.model;

/**
 * Created by mauricio on 22/03/16.
 */
public class Serie {
    private String title;
    private int year;
    private Images images;

    public Serie(String title, int year, Images images) {
        this.title = title;
        this.year = year;
        this.images = images;
    }

    public String getTitle() {
        return  title;
    }

    public int getYear() {
        return year;
    }

    public Images getImages() {
        return images;
    }

}
