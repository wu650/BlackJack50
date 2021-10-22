package com.blackjacksp.frontend;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public Game game;

    public KeyHandler(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'p' || e.getKeyChar() == 'P' && !game.isPaused) {
            game.pauseGame();
            System.out.println("P");
        }
        System.out.println("Pressed");
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
