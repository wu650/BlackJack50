package com.blackjacksp.backend;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private boolean isDealer;
    private List<Card> hand;

    public Player(String playerName, boolean isDealer) {
        this.playerName = playerName;
        this.isDealer = isDealer;
        this.hand = new ArrayList<Card>();
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void addCard(Card drawnCard) {
        hand.add(drawnCard);
    }
}
