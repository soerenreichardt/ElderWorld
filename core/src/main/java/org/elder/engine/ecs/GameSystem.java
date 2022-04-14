package org.elder.engine.ecs;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GameSystem {
    Class<? extends UpdatableSystem>[] value() default {};
}
