package com.blackjacksp.backend;

import java.util.Arrays;

public class Main {
    // Testing functionality of shuffling and dealing methods (temporary)
    public static void main(String[] args) {
        CardPool decktest = new CardPool();

        decktest.populateDecks(1);

        decktest.shuffleDeck();

        for (Card card : CardPool.getDeck()) {
            System.out.println(card.getTitle() + " of " + card.getSuit() + "s");
        }

        Player user = new Player("willis", false);
        decktest.dealCard(user);

        System.out.println("CARD IS DEALT");

        for (Card card : CardPool.getDeck()) {
            System.out.println(card.getTitle() + " of " + card.getSuit() + "s");
        }

        System.out.println("The user holds the " + user.getHand().get(0).getTitle() + " of " + user.getHand().get(0).getSuit() + "s");
        System.out.println("Hand score: " + user.handValue());
    }
}
