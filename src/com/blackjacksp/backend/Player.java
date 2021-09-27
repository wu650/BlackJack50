package com.blackjacksp.backend;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private boolean isDealer;
    private boolean isSticking;
    private List<Card> hand;

    public Player(String playerName, boolean isDealer) {
        this.playerName = playerName;
        this.isDealer = isDealer;
        this.isSticking = false;
        this.hand = new ArrayList<Card>();
    }

    // Used to aid in the scoring of hands with aces
    private int countAces() {
        int aceCounter = 0;
        for (Card card : hand) {
            if (card.getTitle().equals("Ace")) {
                aceCounter++;
            }
        }
        return aceCounter;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public boolean isSticking() {
        return isSticking;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setSticking(boolean isSticking) { this.isSticking = isSticking; }

    public void addCard(Card drawnCard) {
        hand.add(drawnCard);
    }


    // Returns a hand value with the optimal usage of aces (if applicable)
    public int handValue() {
        int handTotal = 0;
        int aceAdjustments = 0;
        for (Card card : hand)
            handTotal += card.getValue();

        // Adjusts hand total to score aces as 1 instead of 11 where appropriate
        while (handTotal > 21) {
            if (countAces() > aceAdjustments) {
                handTotal -= 10;
                aceAdjustments++;
            }
        }
        return handTotal;
    }


    public void dealerLogic(CardPool cardPool) throws DealerException {
        // Ensures that dealer logic is only run on a dealer
        if(!isDealer) {
            throw new DealerException("\n" + "Attempting to use dealer logic on a non-dealer");
        }

        // Dealers will stick if total >= 17 and continue taking cards until that point
        while(!isSticking) {
            if(handValue() >= 17) {
                setSticking(true);
            }
            else {
                cardPool.dealCard(this);
            }
        }
    }
}
