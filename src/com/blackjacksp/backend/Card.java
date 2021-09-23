package com.blackjacksp.backend;

public class Card {
    private String value;
    private String suit;
    private boolean isHidden;

    public Card(String value, String suit, boolean isHidden) {
        this.value = value;
        this.suit = suit;
        this.isHidden = isHidden;
    }

    public Card() {}

    public String getSuit() {
        return suit;
    }

    public String getValue() {
        return value;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setSuit(String newSuit) {
        this.suit = newSuit;
    }

    public void setValue(String newValue) {
        this.value = newValue;
    }

    public void setHidden(boolean hiddenStatus) {
        this.isHidden = hiddenStatus;
    }
}
