package org.elder;

import org.elder.core.GameRunner;

public class Main {

    public static void main(String[] args) {
        var game = new GameRunner();
        try {
            game.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
