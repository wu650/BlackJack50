package com.blackjacksp.backend;

public enum SUITS {
    SPADE("Spade"),
    HEART("Heart"),
    DIAMIND("Diamond"),
    CLUB("Club");

    private String suit;

    SUITS(String suit) {
        this.suit = suit;
    }

    public String getSuit() {
        return suit;
    }
}
