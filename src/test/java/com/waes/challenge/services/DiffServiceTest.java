package com.waes.challenge.services;

import com.waes.challenge.domain.DiffContent;
import com.waes.challenge.repositories.DiffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static com.waes.challenge.domain.DiffComparison.EQUALS;
import static com.waes.challenge.domain.DiffSide.LEFT;
import static com.waes.challenge.domain.DiffSide.RIGHT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class DiffServiceTest {

    @MockBean
    DiffRepository diffRepository;

    @MockBean
    DiffContentService diffContentService;

    @MockBean
    DiffContentValidator diffContentValidator;

    private DiffService diffService;

    @BeforeEach
    void setup() {
        this.diffService = new DiffService(
            diffRepository,
            diffContentService,
            diffContentValidator
        );
    }

    @Test
    void shouldSaveLeftDiff() {

        var id = 1;
        var content = "content".getBytes();

        when(diffContentValidator.validate(content))
        .thenReturn(content);

        when(diffRepository.saveLeftDiff(id, content))
        .thenReturn(true);

        assertThat(diffService.createDiff(id, LEFT, content))
        .isTrue();

        verify(diffRepository, times(0)).saveRightDiff(id, content);
    }

    @Test
    void shouldSaveRightDiff() {

        var id = 1;
        var content = "content".getBytes();

        when(diffContentValidator.validate(content))
        .thenReturn(content);

        when(diffRepository.saveRightDiff(id, content))
        .thenReturn(true);

        assertThat(diffService.createDiff(id, RIGHT, content))
        .isTrue();

        verify(diffRepository, times(0)).saveLeftDiff(id, content);
    }

    @Test
    void shouldThrowExceptionIfNoSideIsProvided() {

        var id = 1;
        var content = "content".getBytes();

        when(diffContentValidator.validate(content))
        .thenReturn(content);

        when(diffRepository.saveRightDiff(id, content))
        .thenReturn(true);

        assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> diffService.createDiff(id, null, content))
        .withMessage("Diff side is mandatory");
    }

    @Test
    void shouldReturnEmptyIfNoLeftDiffIsFound() {

        var id = 1;

        when(diffRepository.getLeftDiffById(1))
        .thenReturn(Optional.empty());

        assertThat(diffService.getDiffById(id))
        .isEmpty();
    }

    @Test
    void shouldReturnEmptyIfNoRightDiffIsFound() {

        var id = 1;
        var content = "content".getBytes();

        when(diffRepository.getLeftDiffById(1))
        .thenReturn(Optional.of(content));

        when(diffRepository.getRightDiffById(1))
        .thenReturn(Optional.empty());

        assertThat(diffService.getDiffById(id))
        .isEmpty();
    }

    @Test
    void shouldReturnDiffContentIfBothSidesExists() {

        var id = 1;
        var content = "content".getBytes();

        when(diffRepository.getLeftDiffById(1))
        .thenReturn(Optional.of(content));

        when(diffRepository.getRightDiffById(1))
        .thenReturn(Optional.of(content));

        var diffContent = new DiffContent(EQUALS, Collections.emptyList());
        when(diffContentService.getDiffContent(content, content))
        .thenReturn(diffContent);

        assertThat(diffService.getDiffById(id))
        .isNotEmpty()
        .contains(diffContent);
    }
}
