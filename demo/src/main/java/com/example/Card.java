package com.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {
    private String term, definition, imageLink;
    private boolean sorted, known;
    
    public Card(String definition, String term, String imageLink){
        this.term = term;
        this.definition = definition;
        this.imageLink = imageLink;
        this.sorted = false;
        this.known = false;
    }
    

    public boolean isSorted() {
        return sorted;
    }


    public boolean isKnown() {
        return known;
    }

    @JsonCreator
    Card(@JsonProperty("term") String term,
        @JsonProperty("definition") String definition,
        @JsonProperty("imageLink") String imageLink,
        @JsonProperty("sorted") boolean sorted,
        @JsonProperty("known") boolean known){
        this.term = term;
        this.definition = definition;
        this.imageLink = imageLink;
        this.sorted = sorted;
        this.known = known;
    }
    
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }


    public void setKnown(boolean known) {
        this.known = known;
    }


    public String getImageLink() {
        return imageLink;
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    public boolean checkCorrect(String ans){
        if (ans.equals(this.term)) {
            return true;
        }
        return false;
    }
}
