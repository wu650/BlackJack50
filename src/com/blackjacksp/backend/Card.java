package com.blackjacksp.backend;

import java.util.Locale;

public class Card {
    private CARDVALS value;
    private String suit;
    private boolean isHidden;
    private String filename;

    public Card(CARDVALS value, String suit, boolean isHidden) {
        this.value = value;
        this.suit = suit;
        this.isHidden = isHidden;
        this.filename = value.getDisplayedName().toLowerCase(Locale.ROOT) + suit.toLowerCase(Locale.ROOT) + "s.png";
    }

    public Card() {}

    public String getSuit() {
        return suit;
    }

    public String getFilename() {
        return filename;
    }

    public String getTitle() {return value.getDisplayedName();}

    public int getValue() {
        return value.getVal();
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setSuit(String newSuit) {
        this.suit = newSuit;
    }

    public void setValue(CARDVALS newValue) {
        this.value = newValue;
    }

    public void setHidden(boolean hiddenStatus) {
        this.isHidden = hiddenStatus;
    }
}
