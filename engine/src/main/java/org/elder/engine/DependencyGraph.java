package org.elder.engine;

import java.util.*;

public class DependencyGraph<T> {

    private final Map<T, Set<T>> adjacencyList;

    public static <T> DependencyGraph<T> fromClassesWithDependencies(Map<T, T[]> classesWithDependencies) {
        var adjacencyList = new HashMap<T, Set<T>>();
        classesWithDependencies.forEach((clazz, dependencies) -> {
            adjacencyList.put(clazz, new HashSet<>(Arrays.asList(dependencies)));
        });
        return new DependencyGraph<>(adjacencyList);
    }

    private DependencyGraph(Map<T, Set<T>> adjacencyList) {
        validate(adjacencyList);
        this.adjacencyList = adjacencyList;
    }

    public List<Set<T>> topologicalSort() {
        var adjacencyListCopy = new HashMap<>(adjacencyList);

        var topologicallySortedList = new ArrayList<Set<T>>();

        var toRemove = new HashSet<T>();
        int lastIterationAdjacencyListSize = adjacencyListCopy.size();
        while (!adjacencyListCopy.isEmpty()) {
            var currentTierSet = new HashSet<T>();
            topologicallySortedList.add(currentTierSet);

            adjacencyListCopy.forEach((clazz, dependencies) -> {
                if (dependencies.isEmpty()) {
                    currentTierSet.add(clazz);
                    toRemove.add(clazz);
                }
            });

            adjacencyListCopy.forEach((clazz, dependencies) -> toRemove.forEach(dependencies::remove));

            toRemove.forEach(adjacencyListCopy::remove);
            if (lastIterationAdjacencyListSize == adjacencyListCopy.size()) {
                throw new IllegalStateException("Cyclic dependencies detected");
            }
            lastIterationAdjacencyListSize = adjacencyListCopy.size();
        }

        return topologicallySortedList;
    }

    private static <T> void validate(Map<T, Set<T>> adjacencyList) {
        adjacencyList.values().forEach(set -> set.forEach(dependency -> {
            if (!adjacencyList.containsKey(dependency)) {
                throw new NoSuchElementException(String.format("Element `%s` was not found", dependency));
            }
        }));
    }
}
