package org.elder;

import org.elder.engine.GameEngineRunner;

public class Main {

    public static void main(String[] args) {
        var game = new GameEngineRunner(800, 600);
        try {
            game.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
