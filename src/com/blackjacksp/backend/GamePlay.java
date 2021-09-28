package com.blackjacksp.backend;

import java.util.*;

// Facilitates the gamestate and game progression
public class GamePlay {
    private List<Player> playerList;
    private HashMap<String, Integer> handTally;
    private CardPool cardPool;
    private int numHands;

    public GamePlay() {
        this.playerList = new ArrayList<Player>();
        this.handTally = new HashMap<String, Integer>();
        this.cardPool = new CardPool();
        this.numHands = 0;
    }

    // Set hand tally to track user hand outcomes
    private void initializeHandTally() {
        handTally.put("Won", 0);
        handTally.put("Lost", 0);
        handTally.put("Tied", 0);
    }

    public void setupGame() {
        Scanner scan = new Scanner(System.in);

        // Get user input for desired number of decks
        System.out.println("How many decks will you use?");
        int numDecks = scan.nextInt();

        // Create card pool, populate, and shuffle deck
        cardPool.populateDecks(numDecks);
        cardPool.shuffleDeck();

        // Get user input for desired number of hands
        System.out.println("How many hands will you play?");
        int numHands = scan.nextInt();
        this.numHands = numHands;

        // Add dealer to players list (will always be index 0)
        playerList.add(new Player("Dealer", true));

        // Get user input for their player name
        System.out.println("What is your name?");
        String playerName = scan.nextLine();

        // Add user to players list
        playerList.add(new Player(playerName, false));

        initializeHandTally();

        scan.close();
    }

    public void playHand() {
        // TODO
    }

    // Will disperse the initial two cards
    public void dealStartingHands() {
        for (Player player : playerList) {

            // Always deals to non-dealers first
            if (!player.isDealer()) {
                for (int i = 0; i < 2; i++) {
                    cardPool.dealCard(player);
                }
            }
            else if (player.isDealer()) {
                for (int i = 0; i < 2; i++) {
                    cardPool.dealCard(player);
                }
            }
        }
    }

    // Check if dealer or player(s) has natural blackjack
    public boolean naturalBlackjack() {
        if (playerList.get(0).handValue() == 21 && playerList.get(1).handValue() == 21) {
            handTally.put("Tied", handTally.get("Tied") + 1);
            return true;
        }
        else if (playerList.get(0).handValue() == 21) {
            handTally.put("Lost", handTally.get("Lost") + 1);
            return true;
        }
        else if (playerList.get(1).handValue() == 21) {
            handTally.put("Won", handTally.get("Won") + 1);
            return true;
        }
        else {
            return false;
        }
    }

    // Main gameplay for the user each round (hit or stick)
    public void hitOrStickCycle(Player player) {
        Scanner scan = new Scanner(System.in);
        while (!player.isSticking()) {
            System.out.printf("Would you like to hit or stick?\n");
            String choice = scan.nextLine();
            if (choice.trim().equalsIgnoreCase("Stick") || choice.trim().equalsIgnoreCase("S")) {
                player.setSticking(true);
            }
            else if (choice.trim().equalsIgnoreCase("Hit") || choice.trim().equalsIgnoreCase("H")) {
                cardPool.dealCard(player);

                // Tells the player what card they were dealt
                System.out.printf("You were dealt a %s of %ss\n", player.getHand().get(player.getHand().size() - 1).getTitle(), player.getHand().get(player.getHand().size() - 1).getSuit());

                // Player will automatically stick if hand totals 21
                if (player.handValue() == 21) {
                    System.out.println("You have BlackJack! It is now the dealer's turn");
                    player.setSticking(true);
                }
                else if (player.handValue() > 21) {
                    System.out.println("You exceeded 21! Hand over");
                    break;
                }
                else {
                    System.out.printf("Your hand now consists of:\n");
                    for (Card card : player.getHand()) {
                        System.out.printf("%s of %ss", card.getTitle(), card.getSuit());
                    }
                }
            }
            // Repeats loop for invalid entries
            else {
                System.out.println("Please enter Stick or Hit");
            }
        }
    }
}
