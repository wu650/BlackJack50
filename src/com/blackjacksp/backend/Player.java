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

    public void clearHand() { hand.clear(); }

    public void setSticking(boolean isSticking) { this.isSticking = isSticking; }

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
                addCard(cardPool.dealCard());
                System.out.printf("Dealer drew a(n) %s of %ss.\n", hand.get(hand.size() - 1).getTitle(), hand.get(hand.size() - 1).getSuit());
                printHand();
            }
        }
    }
}
