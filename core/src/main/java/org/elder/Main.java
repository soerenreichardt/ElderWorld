package org.elder;

import org.elder.core.GameRunner;

public class Main {

    public static void main(String[] args) {
        var game = new GameRunner(800, 600);
        try {
            game.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
