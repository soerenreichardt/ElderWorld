package org.elder.engine.script;

import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.ecs.api.GameSystem;
import org.elder.engine.ecs.api.UpdatableSystem;

import java.util.ArrayList;

@GameSystem
public class ScriptSystem implements UpdatableSystem {

    private Iterable<Script> scriptComponents;

    public ScriptSystem() {
        this.scriptComponents = new ArrayList<>();
    }

    @Override
    public void start() {
        startScripts();
    }

    @Override
    public void stop() {
        for (var script : scriptComponents) {
            script.customScript.stop();
        }
    }

    @Override
    public void update(float delta) {
        for (var script : scriptComponents) {
            script.customScript.update(delta);
        }
    }

    @Override
    public void onSceneChanged(BasicScene scene) {
        this.scriptComponents = scene.componentManager().componentListIterable(Script.class);
        startScripts();
    }

    private void startScripts() {
        for (var script : scriptComponents) {
            script.customScript.initialize();
        }
    }
}
