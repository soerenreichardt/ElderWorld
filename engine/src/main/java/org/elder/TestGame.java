package org.elder;

import org.elder.engine.Scene;
import org.elder.engine.api.GameEngineApi;
import org.elder.engine.api.GameExecutable;
import org.elder.engine.script.ScriptableGameObject;
import org.elder.geometry.Square;
import org.joml.Vector2f;

public class TestGame implements GameExecutable<Scene> {

    @Override
    public void execute(GameEngineApi<Scene> api) {
        var scene = new Scene("Debug Scene");
        api.setScene(scene);
        api.addGameObject(new ScriptableGameObject("foo", api) {
            @Override
            public void initialize() {
                for (int i = 0; i < 10; i++) {
                    var square = new Square("Square" + i);
                    api.addGameObject(square);
                    square.transform().scale(0.1f);
                    var xPos = ((float) Math.random() * 2) - 1.0f;
                    var yPos = ((float) Math.random() * 2) - 1.0f;
                    square.transform().position.set(xPos, yPos);
                    square.velocity().rotation = new Vector2f((float) Math.random(), (float) Math.random());
                }
            }

            @Override
            public void update(float delta) {

            }
        });
    }
}
