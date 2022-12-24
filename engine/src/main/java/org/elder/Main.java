package org.elder;

import org.elder.engine.client.ClientGameEngineRunner;

public class Main {

    public static void main(String[] args) {
        var game = new ClientGameEngineRunner(800, 600);
        try {
            game.start(new TestGame());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
