package com.blackjacksp.frontend;

import com.blackjacksp.backend.Card;
import com.blackjacksp.backend.GamePlay;
import com.blackjacksp.backend.Player;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Game {
    GamePlay gamePlay;
    ActionHandler actionHandler;
    UI ui;

    // Used to assist with card rendering
    public int userCardsDisplayed;
    public int dealerCardsDisplayed;

    // Used to determine appropriate actions during gameplay
    private String situation;

    // Used to determine buttons to re-enable after a pause
    public ArrayList<Integer> activeButtons;

    // Menu component to assist with game pausing
    Action pauseAction;
    public boolean isPaused;

    public Game() {
        this.gamePlay = new GamePlay();
        this.actionHandler = new ActionHandler(this);
        this.ui = new UI(this);
        this.userCardsDisplayed = 0;
        this.dealerCardsDisplayed = 0;
        this.situation = "";
        this.activeButtons = new ArrayList<Integer>();
        this.isPaused = false;
        this.pauseAction = new PauseAction();
    }

    public String getSituation() {
        return situation;
    }

    public void renderSetupUI() {
        ui.setupPanel.setVisible(true);
        ui.start.setVisible(false);
        ui.gamePanel.setVisible(false);
        ui.menu.setVisible(false);
        ui.menuButtons[0].setEnabled(true);
        ui.gamePanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke('p'), "pauseAction");
        ui.gamePanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke('P'), "pauseAction");
        ui.gamePanel.getActionMap().put("pauseAction", pauseAction);

        situation = "";
    }

    // Move from setup to main game and display starting message
    public void renderMainGame() {
        ui.setupPanel.setVisible(false);
        ui.gamePanel.setVisible(true);
        ui.messagePrompt.setText("Dealing first hand.");
        ui.controlButtons[2].setEnabled(true);
        gamePlay.getCardPool().populateDecks(gamePlay.getNumDecks());
        gamePlay.getCardPool().shuffleDeck();
        gamePlay.initializeHandTally();

        // Instantiate dealer
        gamePlay.getPlayerList().add(new Player("Dealer", true));

        // Instantiate user with provided name
        gamePlay.getPlayerList().add(new Player(ui.getPlayerName(), false));

        ui.userNameLabel.setText(gamePlay.getPlayerList().get(1).getPlayerName());
        ui.dealerNameLabel.setText("Dealer");

        // Allows user to access the pause menu immediately (focus has been shifted by name labels)
        ui.gamePanel.requestFocus();

        situation = "newHand";
    }

    public void openNewHand() {
        if (gamePlay.getCardPool().getDeck().size() < 4) {
            ui.messagePrompt.setText("Not enough cards. Reshuffling");
            resetDeck();
            return;
        }
        gamePlay.dealStartingHands();

        // Display starting hands
        for (Player player : gamePlay.getPlayerList()) {
            for (Card card : player.getHand()) {
                if (player.isDealer()) {
                    updateHandDisplay(card, true);
                }
                else {
                    updateHandDisplay(card, false);
                }
            }
        }

        if (gamePlay.userNatural()) {
            ui.messagePrompt.setText("You got a natural!");
            situation = "userNatural";
        }
        else if (gamePlay.dealerNatural()) {
            ui.messagePrompt.setText("Dealer has a natural.");
            situation = "dealerNatural";
        }
        else {
            ui.messagePrompt.setText("Your move. Hit or stick?");
            ui.controlButtons[0].setEnabled(true);
            ui.controlButtons[1].setEnabled(true);
            ui.controlButtons[2].setEnabled(false);
            situation = "userTurn";
        }
    }

    public void userHit() {

        // Ensure that the deck is populated
        if (gamePlay.getCardPool().getDeck().isEmpty()) {
            ui.messagePrompt.setText("Deck is empty. Reshuffling.");
            resetDeck();
            ui.controlButtons[0].setEnabled(false);
            ui.controlButtons[1].setEnabled(false);
            ui.controlButtons[2].setEnabled(true);
            return;
        }
        Card newCard = gamePlay.getCardPool().dealCard();
        gamePlay.getPlayerList().get(1).addCard(newCard);
        updateHandDisplay(newCard, false);

        // Remove deck icon if deck is empty
        if (gamePlay.getCardPool().getDeck().isEmpty()) {
            ui.deckHolder.setIcon(null);
        }

        int userScore = gamePlay.getPlayerList().get(1).handValue();

        if (userScore == 21) {
            ui.messagePrompt.setText("You have blackjack!");
            ui.controlButtons[0].setEnabled(false);
            ui.controlButtons[1].setEnabled(false);
            ui.controlButtons[2].setEnabled(true);
            situation = "userSticking";
            return;
        }
        else if (userScore > 21) {
            ui.messagePrompt.setText("You busted.");
            ui.controlButtons[0].setEnabled(false);
            ui.controlButtons[1].setEnabled(false);
            ui.controlButtons[2].setEnabled(true);
            situation = "userBust";
            return;
        }
        // User turn ends if hand limit is reached
        if (userCardsDisplayed == 5) {
            ui.messagePrompt.setText("Hand limit reached.");
            ui.controlButtons[0].setEnabled(false);
            ui.controlButtons[1].setEnabled(false);
            ui.controlButtons[2].setEnabled(true);
            situation = "userSticking";
        }
    }

    public void userStick() {
        ui.messagePrompt.setText("Let's reveal the dealer's card.");
        ui.controlButtons[0].setEnabled(false);
        ui.controlButtons[1].setEnabled(false);
        ui.controlButtons[2].setEnabled(true);
        situation = "userSticking";
    }

    public void dealerLogic() {
        // Should reveal hidden card if the user's turn has just ended
        if (situation.equals("userSticking")) {
            ui.messagePrompt.setText("Dealer's turn.");
            gamePlay.revealHand(gamePlay.getPlayerList().get(0));
            updateHandDisplay(gamePlay.getPlayerList().get(0).getHand().get(1), true);
            situation = "dealerTurn";
            return;
        }

        if (gamePlay.getPlayerList().get(0).handValue() >= 17) {
            ui.messagePrompt.setText("Dealer is sticking.");
            if (gamePlay.getPlayerList().get(0).handValue() == 21) {
                ui.messagePrompt.setText("Dealer has blackjack.");
            }
            situation = "dealerSticking";
        }
        else {
            ui.messagePrompt.setText("Dealer will draw a card");
            situation = "dealerDraw";
        }
    }

    public void dealerDraw() {

        //Ensure that the deck is populated
        if (gamePlay.getCardPool().getDeck().isEmpty()) {
            ui.messagePrompt.setText("Deck is empty. Reshuffling.");
            resetDeck();
            return;
        }
        Card newCard = gamePlay.getCardPool().dealCard();
        gamePlay.getPlayerList().get(0).addCard(newCard);
        updateHandDisplay(newCard, true);

        // Remove deck icon if deck is empty
        if (gamePlay.getCardPool().getDeck().isEmpty()) {
            ui.deckHolder.setIcon(null);
        }

        int dealerTotal = gamePlay.getPlayerList().get(0).handValue();
        if (dealerTotal > 21) {
            ui.messagePrompt.setText("Dealer busted.");
            situation = "dealerBust";
        }
        else {
            if (dealerCardsDisplayed < 5) {
                ui.messagePrompt.setText("Continue dealer's turn.");
                situation = "dealerTurn";
            }
            // Calculate scores if dealer reaches hand limit
            else {
                ui.messagePrompt.setText("Dealer hand limit reached.");
                situation = "dealSticking";
            }
        }
    }

    public void endOfHand() {
        if (situation.equals("userNatural") || situation.equals("dealerNatural") || situation.equals("userBust")) {
            gamePlay.revealHand(gamePlay.getPlayerList().get(0));
            updateHandDisplay(gamePlay.getPlayerList().get(0).getHand().get(1), true);
        }

        // Check winner to display appropriate message
        String winner = gamePlay.scoreHand();
        if (winner.equals("dealer")) {
            ui.messagePrompt.setText("You lost this hand.");
            ui.gameTallies[2].setText("Lost: " + Integer.toString(gamePlay.accessTally("Lost")));
        }
        else if (winner.equals("user")) {
            ui.messagePrompt.setText("You won this hand!");
            ui.gameTallies[1].setText("Won: " + Integer.toString(gamePlay.accessTally("Won")));
        }
        else {
            ui.messagePrompt.setText("Hand is a draw.");
            ui.gameTallies[3].setText("Tied: " + Integer.toString(gamePlay.accessTally("Tied")));
        }

        // Calculate total hands played
        int totalHands = gamePlay.accessTally("Won") + gamePlay.accessTally("Lost") + gamePlay.accessTally("Tied");
        ui.gameTallies[0].setText("Played: " + Integer.toString(totalHands));

        if (gamePlay.handsPlayed() == gamePlay.getNumHands()) {
            situation = "gameFinished";
        }
        else {
            situation = "nextTurn";
        }
    }

    public void turnTransition() {
        gamePlay.emptyHands();
        updateDiscardPile();
        // Clear card images from hands
        for (JLabel card : ui.userCards) {
            card.setIcon(null);
        }
        for (JLabel card : ui.dealerCards) {
            card.setIcon(null);
        }
        userCardsDisplayed = 0;
        dealerCardsDisplayed = 0;

        ui.messagePrompt.setText("Let's deal the next hand.");

        situation = "newHand";
    }

    public void updateHandDisplay(Card card, boolean isDealer) {
        Image cardImage;
        ImageIcon cardImg;
        if (!card.isHidden()) {
            try {
                cardImage = ImageIO.read(new File(getClass().getResource("res/cardRes/" + card.getFilename()).toURI()));
                cardImage = cardImage.getScaledInstance(ui.cardWidth / 5, ui.cardHeight / 5, Image.SCALE_SMOOTH);
                cardImg = new ImageIcon(cardImage);
                if (isDealer) {
                    ui.dealerCards[dealerCardsDisplayed].setIcon(cardImg);
                    dealerCardsDisplayed++;
                } else {
                    ui.userCards[userCardsDisplayed].setIcon(cardImg);
                    userCardsDisplayed++;
                }

            } catch (IOException | URISyntaxException e) {
                System.out.println("Failed to find file");
                e.printStackTrace();
            }
        }
        else {
            cardImage = ui.icon;
            cardImage = cardImage.getScaledInstance(ui.cardWidth / 5, ui.cardHeight / 5, Image.SCALE_SMOOTH);
            cardImg = new ImageIcon(cardImage);
            if (isDealer) {
                ui.dealerCards[gamePlay.getPlayerList().get(0).getHand().size() - 1].setIcon(cardImg);
            } else {
                ui.userCards[gamePlay.getPlayerList().get(1).getHand().size() - 1].setIcon(cardImg);
            }
        }
    }

    // Changes discard pile to show most recently discarded card
    public void updateDiscardPile() {
        Card dcTop = gamePlay.getCardPool().getDiscardPile().get(gamePlay.getCardPool().getDiscardPile().size() - 1);
        Image discardTop;
        try {
            discardTop = ImageIO.read(new File(getClass().getResource("res/cardRes/" + dcTop.getFilename()).toURI()));
            discardTop = discardTop.getScaledInstance(ui.cardWidth / 5, ui.cardHeight / 5, Image.SCALE_SMOOTH);
            ImageIcon dcPile = new ImageIcon(discardTop);
            ui.discardPile.setIcon(dcPile);
            ui.discardPile.setVisible(true);
        } catch (IOException | URISyntaxException e) {
            System.out.println("Failed to find file");
            e.printStackTrace();
        }
    }

    // Used when deck is empty
    public void resetDeck() {
       gamePlay.getCardPool().repopulateDeck();
       ui.discardPile.setIcon(null);
       ui.deckHolder.setIcon(ui.deck);
    }

    public void endGame() {
        ui.messagePrompt.setText("Game over. Thanks for playing!");
        situation = "endingMenu";
    }

    public void setupNewGame() {
        gamePlay = new GamePlay();
        isPaused = false;
        ui.gameTallies[0].setText("Played: -");
        ui.gameTallies[1].setText("Won: -");
        ui.gameTallies[2].setText("Lost: -");
        ui.gameTallies[3].setText("Tied: -");
        ui.discardPile.setIcon(null);
        ui.messagePrompt.setVisible(true);

        // Clear radio buttons for fresh setup screen
        ui.deckButtonGroup.clearSelection();
        ui.handButtonGroup.clearSelection();

        // Clear card images from both hands
        for (JLabel card : ui.userCards) {
            card.setIcon(null);
        }
        for (JLabel card : ui.dealerCards) {
            card.setIcon(null);
        }
        userCardsDisplayed = 0;
        dealerCardsDisplayed = 0;
        activeButtons.clear();
        renderSetupUI();
    }

    public void resumeGame() {
        isPaused = false;
        if (!activeButtons.isEmpty()) {
            for (Integer index : activeButtons) {
                ui.controlButtons[index].setEnabled(true);
            }
        }
        ui.messagePrompt.setVisible(true);
        ui.menu.setVisible(false);
    }

    // Return list of currently active uiControl index values
    public void updateActiveButtons() {
        for (int i = 0; i < 3; i++) {
            if (ui.controlButtons[i].isEnabled()) {
                activeButtons.add(i);
            }
        }
    }

    public void pauseGame() {
        if (!isPaused) {
            isPaused = true;
            updateActiveButtons();
            for (int i = 0; i < 3; i++) {
                ui.controlButtons[i].setEnabled(false);
            }
            ui.messagePrompt.setVisible(false);
            ui.menu.setVisible(true);
        }
    }

    public class PauseAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!(situation.equals("") || situation.equals("endingMenu"))) {
                pauseGame();
            }
        }
    }

}
