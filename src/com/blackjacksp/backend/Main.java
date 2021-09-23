package com.blackjacksp.backend;

import java.util.Arrays;

public class Main {
    // Testing functionality of shuffling and dealing methods (temporary)
    public static void main(String[] args) {
        CardPool decktest = new CardPool();

        decktest.populateDecks(1);

        decktest.shuffleDeck();

        for (Card card : CardPool.getDeck()) {
            System.out.println(card.getValue() + " of " + card.getSuit() + "s");
        }

        Player user = new Player("willis", false);
        decktest.dealCard(user);

        System.out.println("CARD IS DEALT");

        for (Card card : CardPool.getDeck()) {
            System.out.println(card.getValue() + " of " + card.getSuit() + "s");
        }

        System.out.println("The user holds " + user.getHand().get(0).getValue() + " of " + user.getHand().get(0).getSuit());
    }
}
