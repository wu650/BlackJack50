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

        // Get user input for their player name
        System.out.println("What is your name?");
        String playerName = scan.nextLine();

        // Add user to players list
        playerList.add(new Player(playerName, false));

        // Add dealer to players list
        playerList.add(new Player("Dealer", true));

        this.initializeHandTally();

        scan.close();
    }

    // Main gameplay for the user each round (hit or stick)
    public void hitOrStickCycle(Player player) {
        Scanner scan = new Scanner(System.in);
        while (!player.isSticking()) {
            System.out.printf("Would you like to hit or stick?");
            String choice = scan.nextLine();
            if (choice.trim().equalsIgnoreCase("Stick") || choice.trim().equalsIgnoreCase("S")) {
                player.setSticking(true);
            }
            else if (choice.trim().equalsIgnoreCase("Hit") || choice.trim().equalsIgnoreCase("H")) {
                cardPool.dealCard(player);
                // Tells the player what card they were dealt
                System.out.printf("You were dealt a %s of %ss", player.getHand().get(player.getHand().size() - 1).getTitle(), player.getHand().get(player.getHand().size() - 1).getSuit());
                if (player.handValue() > 21) {
                    System.out.println("You exceeded 21! Hand over");
                    break;
                }
                else {
                    System.out.printf("Your hand now consists of:");
                    for (Card card : player.getHand()) {
                        System.out.printf("%s of %ss", card.getTitle(), card.getSuit());
                    }
                }
            }
            else {
                System.out.println("Please enter Stick or Hit");
            }
        }
    }
}
