package org.elder.engine.ecs.system;

import org.elder.engine.ecs.api.Resource;
import org.elder.engine.ecs.api.UpdatableSystem;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class SystemManagerBuilder {

    private Resource[] resources;

    public ReflectionSystemManagerBuilder fromReflection() {
        return new ReflectionSystemManagerBuilder(this);
    }

    public ListSystemManagerBuilder fromList() {
        return new ListSystemManagerBuilder(this);
    }

    void withResources(Resource[] resources) {
        this.resources = resources;
    }

    public static class ReflectionSystemManagerBuilder {

        private final List<String> packagePrefixes;
        private final SystemManagerBuilder systemManagerBuilder;

        public ReflectionSystemManagerBuilder(SystemManagerBuilder systemManagerBuilder) {
            this.packagePrefixes = new ArrayList<>();
            this.systemManagerBuilder = systemManagerBuilder;
        }

        public ReflectionSystemManagerBuilder withPackagePrefix(String prefix) {
            this.packagePrefixes.add(prefix);
            return this;
        }

        public ReflectionSystemManagerBuilder withResources(Resource[] resources) {
            systemManagerBuilder.withResources(resources);
            return this;
        }

        public SystemManager build() {
            var systemClasses = new HashSet<Class<? extends UpdatableSystem>>();
            packagePrefixes.forEach(packagePrefix -> systemClasses.addAll(
                    new Reflections("org.elder", Scanners.SubTypes.filterResultsBy(__ -> true))
                            .getSubTypesOf(UpdatableSystem.class)
            ));
            return systemManagerBuilder.finalizeSystemManager(systemClasses.stream());
        }
    }

    public static class ListSystemManagerBuilder {

        private final List<Class<? extends UpdatableSystem>> systemClassesList;
        private final SystemManagerBuilder systemManagerBuilder;

        public ListSystemManagerBuilder(SystemManagerBuilder systemManagerBuilder) {
            this.systemManagerBuilder = systemManagerBuilder;
            this.systemClassesList = new ArrayList<>();
        }

        public ListSystemManagerBuilder withSystemClass(Class<? extends UpdatableSystem> systemClass) {
            this.systemClassesList.add(systemClass);
            return this;
        }

        public ListSystemManagerBuilder withResources(Resource[] resources) {
            systemManagerBuilder.withResources(resources);
            return this;
        }

        public SystemManager build() {
            return systemManagerBuilder.finalizeSystemManager(systemClassesList.stream());
        }
    }

    SystemManager finalizeSystemManager(Stream<Class<? extends UpdatableSystem>> systemClassesStream) {
        var systemLoader = new SystemLoader(systemClassesStream, resources);
        return new SystemManager(systemLoader);
    }
}
