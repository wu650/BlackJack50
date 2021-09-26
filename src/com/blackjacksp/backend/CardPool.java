package com.blackjacksp.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardPool {
    private static List<Card> deck;
    private static List<Card> discardPile;

    public CardPool() {
        this.deck = new ArrayList<Card>();
        this.discardPile = new ArrayList<Card>();
    }

    public static List<Card> getDeck() {
        return deck;
    }

    public static List<Card> getDiscardPile() {
        return discardPile;
    }

    // Populates deck pool, allows user to select number of decks to play with
    public void populateDecks(int numDecks) {
        for (int i = 0; i < numDecks; i++) {
            for (CARDVALS cardVal : CARDVALS.values()) {
                for (SUITS suit : SUITS.values()) {
                    deck.add(new Card(cardVal, suit.getSuit(), true));
                }
            }
        }
    }

    // Implement shuffle in case future games require access to the deck
    public void shuffleDeck() {

        // Sets up random number for shuffle
        Random rnd = new Random();

        // Create temporary card to hold card being shuffled
        Card tmpCard = new Card();
        for (int i = 0; i < deck.size(); i++) {
            int cardPos = rnd.nextInt(deck.size());
            tmpCard = deck.get(i);
            deck.set(i, deck.get(cardPos));
            deck.set(cardPos, tmpCard);
        }
    }

    // Pass top card of the deck to the player requesting a 'hit'
    public void dealCard(Player player) {
        Card dealtCard = deck.remove(0);
        player.addCard(dealtCard);
    }
}


