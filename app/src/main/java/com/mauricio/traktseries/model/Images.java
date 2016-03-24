package com.mauricio.traktseries.model;

/**
 * Created by mauricio on 22/03/16.
 */
public class Images {
    private Poster poster;

    public Images(Poster poster) {
        this.poster = poster;
    }

    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    @Override
    public boolean equals(Object o) {
        boolean isImages = o instanceof  Images;

        if (!isImages)
            return false;

        Images other = (Images) o;

        return poster.equals(other.getPoster());
    }
}
