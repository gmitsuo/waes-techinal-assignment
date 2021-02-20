package com.waes.challenge.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryDiffRepositoryTest {

    protected DiffRepository diffRepository;

    @BeforeEach
    public void setup() {
        this.diffRepository = new InMemoryDiffRepository();
    }

    @Test
    void shouldCreateLeftDiff() {

        var id = 1;
        var content = "content".getBytes();

        diffRepository.saveLeftDiff(id, content);

        assertThat(diffRepository.getLeftDiffById(1))
        .isNotEmpty()
        .contains(content);
    }

    @Test
    void shouldCreateRightDiff() {

        var id = 1;
        var content = "content".getBytes();

        diffRepository.saveRightDiff(id, content);

        assertThat(diffRepository.getRightDiffById(1))
        .isNotEmpty()
        .contains(content);
    }

    @Test
    void shouldReturnEmptyIfLeftDiffNotFound() {
        assertThat(diffRepository.getLeftDiffById(1))
        .isEmpty();
    }

    @Test
    void shouldReturnEmptyIfRightDiffNotFound() {
        assertThat(diffRepository.getRightDiffById(1))
        .isEmpty();
    }
}
