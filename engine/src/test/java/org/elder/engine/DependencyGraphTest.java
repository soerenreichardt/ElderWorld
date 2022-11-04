package org.elder.engine;

import org.elder.engine.ecs.DependencyGraph;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DependencyGraphTest {

    @Test
    void shouldSortIntoCorrectTiers() {
        var dependencyGraph = DependencyGraph.fromClassesWithDependencies(Map.of(
                "A", new String[0],
                "B", new String[]{ "A" },
                "C", new String[]{ "B" },
                "D", new String[0],
                "E", new String[]{ "B", "D" }
        ));

        var sortedStrings = dependencyGraph.topologicalSort();
        assertThat(sortedStrings).size().isEqualTo(3);
        assertThat(sortedStrings.get(0)).containsExactlyInAnyOrder("A", "D");
        assertThat(sortedStrings.get(1)).containsExactlyInAnyOrder("B");
        assertThat(sortedStrings.get(2)).containsExactlyInAnyOrder("C", "E");
    }

    @Test
    void shouldDetectCycles() {
        var dependencyGraph = DependencyGraph.fromClassesWithDependencies(Map.of(
                "A", new String[] { "C" },
                "B", new String[] { "A" },
                "C", new String[] { "B" }
        ));

        assertThatThrownBy(dependencyGraph::topologicalSort)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cyclic dependencies detected");
    }

    @Test
    void shouldCheckForValidDependencies() {
        assertThatThrownBy(() -> DependencyGraph.fromClassesWithDependencies(Map.of(
                "A", new String[] { "B" },
                "B", new String[] { "Missing" }
        )))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Element `Missing` was not found");
    }

}