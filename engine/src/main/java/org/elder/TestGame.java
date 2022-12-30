package org.elder;

import org.elder.engine.Scene;
import org.elder.engine.api.GameEngineApi;
import org.elder.engine.api.GameExecutable;
import org.elder.geometry.Square;
import org.joml.Vector2f;

public class TestGame implements GameExecutable<Scene> {

    @Override
    public void execute(GameEngineApi<Scene> api) {
        var scene = new Scene("Debug Scene");
        for (int i = 0; i < 10; i++) {
            var square = new Square("Square" + i);
            scene.addGameObject(square);
            square.transform().scale(0.1f);
            var xPos = ((float) Math.random() * 2) - 1.0f;
            var yPos = ((float) Math.random() * 2) - 1.0f;
            square.transform().position.set(xPos, yPos);
            square.velocity().rotation = new Vector2f((float) Math.random(), (float) Math.random());
        }
        api.setScene(scene);
    }
}
