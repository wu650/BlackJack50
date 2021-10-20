package com.blackjacksp.frontend;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionHandler implements ActionListener {

    Game game;

    public ActionHandler(Game game) {
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        switch(command) {
            case "enterGame":
                if (game.gamePlay.getNumHands() > 0 && game.gamePlay.getNumHands() > 0 && !(game.ui.playerName.getText().isEmpty())) {
                    game.renderMainGame();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please fill out all fields", "Unfinished Setup", JOptionPane.PLAIN_MESSAGE);
                }
                break;
            case "continue":
                if (game.getSituation().equals("newHand")) {
                    game.openNewHand();
                }
                else if (game.getSituation().equals("nextTurn")) {
                    game.turnTransition();
                }
                else if (game.getSituation().equals("userNatural") || game.getSituation().equals("dealerNatural")) {
                    game.endOfHand();
                }
                else if (game.getSituation().equals("userBust") || game.getSituation().equals("dealerBust")) {
                    game.endOfHand();
                }
                else if (game.getSituation().equals("userSticking") || game.getSituation().equals("dealerTurn")) {
                    game.dealerLogic();
                }
                else if (game.getSituation().equals("dealerDraw")) {
                    game.dealerDraw();
                }
                else if (game.getSituation().equals("dealerSticking")) {
                    game.endOfHand();
                }
                else if (game.getSituation().equals("gameFinished")) {
                    game.endGame();
                }
                else if (game.getSituation().equals("endingMenu")) {
                    game.ui.messagePrompt.setVisible(false);
                    game.ui.menuButtons[0].setEnabled(false);
                    game.pauseGame();
                }
                else if (game.getSituation().equals("userTurn")) {
                    game.userHit();
                    game.ui.controlButtons[0].setEnabled(true);
                    game.ui.controlButtons[1].setEnabled(true);
                    game.ui.controlButtons[2].setEnabled(false);
                }
                else {
                    System.out.print("Attempted to pass invalid situation");
                    System.exit(1);
                }
                break;
            case "hit":
                game.userHit();
                break;
            case "stick":
                game.userStick();
                break;
            case "resume":
                game.resumeGame();
                game.activeButtons.clear();
                break;
            case "newGame":
                game.setupNewGame();
        }

    }

}

