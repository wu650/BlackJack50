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
        return this.isDealer;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void clearHand() { hand.clear(); }

    public void addCard(Card drawnCard) {
        hand.add(drawnCard);
    }

    // Prints a list of cards in the player's hand
    public void printHand() {
        System.out.printf("%s currently holds:\n", getPlayerName());
        for (Card card : hand) {
            // Ensures that no hidden cards are revealed prematurely
            if (card.isHidden()) {
                System.out.println("Unknown (facedown) card");
            }
            else {
                System.out.printf("The %s of %ss\n", card.getTitle(), card.getSuit());
            }
        }
    }

    public void revealHand() {
        for (Card card : hand) {
            if (card.isHidden()) {
                card.setHidden(false);
            }
        }
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
            else {
                return handTotal;
            }
        }
        return handTotal;
    }

}
