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

    // Launches the game (combines gameplay functions to create gameflow)
    public void enterGameFlow() {
        Scanner scan = new Scanner(System.in);
        setupGame(scan);
        for (int i = 0; i < numHands; i++) {
            playHand(scan);
        }
        announceFinalTally();
        scan.close();
        return;
    }

    public void setupGame(Scanner scan) {

        // Get user input for desired number of decks
        System.out.println("How many decks will you use?");
        int numDecks = scan.nextInt();

        // Create card pool, populate, and shuffle deck
        cardPool.populateDecks(numDecks);
        cardPool.shuffleDeck();

        // Get user input for desired number of hands
        System.out.println("How many hands will you play?");
        int numHands = scan.nextInt();
        scan.nextLine();
        this.numHands = numHands;

        // Add dealer to players list (will always be index 0)
        playerList.add(new Player("Dealer", true));

        // Get user input for their player name
        System.out.println("What is your name?");
        String playerName = scan.nextLine();

        // Add user to players list
        playerList.add(new Player(playerName, false));

        initializeHandTally();
    }

    // All the steps in a hand from beginning to end
    public void playHand(Scanner scan) {
        dealStartingHands();
        // Initial display of both players starting hands
        for (Player player : playerList) {
            player.printHand();
        }
        if (naturalBlackjack()) {
            // Reveals the dealers facedown card in the event of a natural
            playerList.get(0).revealHand();
            playerList.get(0).printHand();
            System.out.println("Round over. Advancing to next hand.");
        }

        // Round progresses if there is no natural
        else {

            // User's turn
            hitOrStickCycle(playerList.get(1), scan);
            // Check if the user went over 21
            if (playerList.get(1).handValue() > 21) {
                handTally.put("Lost", handTally.get("Lost") + 1);
                playerList.get(0).revealHand();
                playerList.get(0).printHand();
                return;
            }

            // Dealer's turn
            playerList.get(0).revealHand();
            playerList.get(0).printHand();
            try {
                playerList.get(0).dealerLogic(cardPool);
            }
            catch (Exception DealerException) {
                System.out.println("Dealer was not passed in properly");
            }
            scoreHand();
            emptyHands();
        }
    }

    // Will disperse the initial two cards
    public void dealStartingHands() {
        // Always deals to non-dealers first
        for (int i = 1; i >= 0; i--) {
            for (int j = 0; j < 2; j++){
                cardPool.dealCard(playerList.get(i));
            }

            // Dealer starts with second card hidden
            if (playerList.get(i).isDealer()) {
                playerList.get(i).getHand().get(1).setHidden(true);
            }
        }
    }

    // Check if dealer or player(s) has natural blackjack
    public boolean naturalBlackjack() {
        if (playerList.get(0).handValue() == 21 && playerList.get(1).handValue() == 21) {
            handTally.put("Tied", handTally.get("Tied") + 1);
            System.out.println("Both players have blackjack! This hand is a tie.");
            return true;
        }
        else if (playerList.get(0).handValue() == 21) {
            handTally.put("Lost", handTally.get("Lost") + 1);
            System.out.println("The dealer has blackjack. You lose this hand.");
            return true;
        }
        else if (playerList.get(1).handValue() == 21) {
            handTally.put("Won", handTally.get("Won") + 1);
            System.out.println("You have blackjack! You win this hand.");
            return true;
        }
        else {
            return false;
        }
    }

    // Main gameplay for the user each round (hit or stick)
    public void hitOrStickCycle(Player player, Scanner scan) {
        while (!player.isSticking()) {
            System.out.println("Would you like to hit or stick?");
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
                    System.out.println("Your hand now consists of:");
                    for (Card card : player.getHand()) {
                        System.out.printf("%s of %ss\n", card.getTitle(), card.getSuit());
                    }
                }
            }
            // Repeats loop for invalid entries
            else {
                System.out.println("Please enter Stick or Hit");
            }
        }
    }

    public void scoreHand() {
        int dealerScore = playerList.get(0).handValue();
        int playerScore = playerList.get(1).handValue();
        // Check if dealer busted
        if (dealerScore > 21) {
            System.out.println("Dealer busted, round over.");
            handTally.put("Won", handTally.get("Won") + 1);
        }
        else if (dealerScore > playerScore) {
            System.out.printf("Dealer scored %d\n", dealerScore);
            System.out.printf("You scored %d\n", playerScore);
            System.out.println("The dealer wins. Moving to next round.\n");
            handTally.put("Lost", handTally.get("Lost") + 1);
        }
        else if (dealerScore == playerScore) {
            System.out.printf("Both players scored %d\n", dealerScore);
            System.out.println("Round results in a tie. Moving to next round.");
            handTally.put("Tied", handTally.get("Tied") + 1);
        }
        else {
            System.out.printf("Dealer scored %d\n", dealerScore);
            System.out.printf("You scored %d\n", playerScore);
            System.out.println("You win! Moving to next round.");
            handTally.put("Won", handTally.get("Won") + 1);
        }
    }

    // Sends hands to discard pile at the end of the round
    public void emptyHands() {
        for (Player player: playerList) {
            // Append player's entire hand to the discard pile
            CardPool.getDiscardPile().addAll(player.getHand());
            // Clear hand for next round
            player.clearHand();
        }
    }

    public void announceFinalTally() {
        System.out.printf("In %d hands you won %d, lost %d, and tied %d.\nThank you for playing",
                numHands, handTally.get("Won"), handTally.get("Lost"), handTally.get("Tied"));
    }
}
