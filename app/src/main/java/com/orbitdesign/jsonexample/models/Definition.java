package com.orbitdesign.jsonexample.models;

/**
 * Created by sdros_000 on 6/14/2015.
 */
public class Definition {

    private String text, attribution;

    public Definition(String text, String attribution){
        this.text = text;
        this.attribution = attribution;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }
}
