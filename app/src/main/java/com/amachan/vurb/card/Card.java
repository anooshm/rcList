package com.amachan.vurb.card;

/**
 * Created by anoosh on 3/10/15.
 */
public abstract class Card {

    public void Card(){

    }
    private String type;
    private String title;
    private String extras;
    private String imageUrl;

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
