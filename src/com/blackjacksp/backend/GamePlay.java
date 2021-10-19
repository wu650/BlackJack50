package com.blackjacksp.backend;

import java.util.*;

// Facilitates the gamestate and game progression
public class GamePlay {
    private ArrayList<Player> playerList;
    private HashMap<String, Integer> handTally;
    private CardPool cardPool;
    private int numDecks;
    private int numHands;

    public GamePlay() {
        this.playerList = new ArrayList<Player>();
        this.handTally = new HashMap<String, Integer>();
        this.cardPool = new CardPool();
        this.numHands = 0;
        this.numDecks = 0;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public int accessTally(String result) {
        return handTally.get(result);
    }

    public CardPool getCardPool() {
        return cardPool;
    }

    public int getNumDecks() {
        return numDecks;
    }

    public int getNumHands() {
        return numHands;
    }

    public void setNumDecks(int numDecks) {
        this.numDecks = numDecks;
    }

    public void setNumHands(int numHands) {
        this.numHands = numHands;
    }

    // Set hand tally to track user hand outcomes
    public void initializeHandTally() {
        handTally.put("Won", 0);
        handTally.put("Lost", 0);
        handTally.put("Tied", 0);
    }

    // Will disperse the initial two cards
    public void dealStartingHands() {
        // Always deals to non-dealers first
        for (int i = 1; i >= 0; i--) {
            for (int j = 0; j < 2; j++){
                playerList.get(i).addCard(cardPool.dealCard());
            }

            // Dealer starts with second card hidden
            if (playerList.get(i).isDealer()) {
                playerList.get(i).getHand().get(1).setHidden(true);
            }
        }
    }

    // Check if user has a natural blackjack
    public boolean userNatural() {
        if (this.playerList.get(1).handValue() == 21) {
            return true;
        }
        return false;
    }

    // Check if dealer has a natural blackjack
    public boolean dealerNatural() {
        if (this.playerList.get(0).handValue() == 21) {
            return true;
        }
        return false;
    }

    // Flip a player's hidden cards
    public void revealHand(Player player) {
        for (Card card : player.getHand()) {
            card.setHidden(false);
        }
    }

    public String scoreHand() {
        int dealerScore = playerList.get(0).handValue();
        int playerScore = playerList.get(1).handValue();

        String winner;

        // Check if player busted
        if (playerScore > 21) {
            handTally.put("Lost", handTally.get("Lost") + 1);
            winner = "dealer";
        }
        // Check if dealer busted
        else if (dealerScore > 21) {
            handTally.put("Won", handTally.get("Won") + 1);
            winner = "user";
        }
        else if (dealerScore > playerScore) {
            handTally.put("Lost", handTally.get("Lost") + 1);
            winner = "dealer";
        }
        else if (dealerScore == playerScore) {
            handTally.put("Tied", handTally.get("Tied") + 1);
            winner = "draw";
        }
        else {
            handTally.put("Won", handTally.get("Won") + 1);
            winner = "user";
        }

        return winner;
    }

    public int handsPlayed() {
        int total = 0;
        for (int result : handTally.values()) {
            total += result;
        }
        return total;
    }

    // Sends hands to discard pile at the end of the round
    public void emptyHands() {
        for (Player player: playerList) {
            // Append player's entire hand to the discard pile
            cardPool.getDiscardPile().addAll(player.getHand());
            // Clear hand for next round
            player.clearHand();
        }
    }
}

