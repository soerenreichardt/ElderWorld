package org.elder.core.ecs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdManagerTest {

    @Test
    void shouldReassignRemovedIds() {
        var idManager = new IdManager();

        assertThat(idManager.getNewId()).isEqualTo(0);
        assertThat(idManager.getNewId()).isEqualTo(1);
        assertThat(idManager.getNewId()).isEqualTo(2);

        idManager.removeId(1);
        assertThat(idManager.getNewId()).isEqualTo(1);
        assertThat(idManager.getNewId()).isEqualTo(3);
    }

}