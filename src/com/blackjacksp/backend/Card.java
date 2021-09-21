package com.blackjacksp.backend;

public class Card {
    private String suit;
    private String value;
    private boolean isHidden;

    public Card(String suit, String value, boolean isHidden) {
        this.suit = suit;
        this.value = value;
        this.isHidden = isHidden;
    }

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
