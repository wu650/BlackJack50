package com.blackjacksp.backend;

public enum CARDVALS {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6",6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("Jack", 10),
    QUEEN("Queen", 10),
    KING("King", 10),
    ACE("Ace", 11);

    private int val;
    private String displayedName;

    CARDVALS(String displayedName, int val) {
        this.displayedName = displayedName;
        this.val = val;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    public int getVal() {
        return val;
    }
}
