package com.waes.challenge.services;

import com.waes.challenge.domain.Diff;
import com.waes.challenge.domain.DiffContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.waes.challenge.domain.DiffComparison.*;
import static org.assertj.core.api.Assertions.assertThat;

class DiffContentServiceTest {

    private DiffContentService diffContentService;

    @BeforeEach
    void setup() {
        this.diffContentService = new DiffContentService();
    }

    @Test
    void shouldReturnDifferentLengthContent() {

        var content1 = "First content";
        var content2 = "Second content";

        assertThat(diffContentService.getDiffContent(content1.getBytes(), content2.getBytes()))
        .isEqualTo(new DiffContent(DIFFERENT_LENGTHS, null));
    }

    @Test
    void shouldReturnEqualsContent() {

        var content1 = "First content";
        var content2 = "First content";

        assertThat(diffContentService.getDiffContent(content1.getBytes(), content2.getBytes()))
        .isEqualTo(new DiffContent(EQUALS, Collections.emptyList()));
    }

    @Test
    void shouldReturnDiffInWholeContent() {

        var content1 = "AAABBBCCCDDDEEE";
        var content2 = "FFFGGGHHHIIIJJJ";

        assertThat(diffContentService.getDiffContent(content1.getBytes(), content2.getBytes()))
        .isEqualTo(new DiffContent(NOT_EQUALS, List.of(new Diff(0, 15))));
    }

    @Test
    void shouldFindSingleDiffInFirstThreeBytes() {
        var content1 = "AAABBBCCCDDDEEE";
        var content2 = "FFFBBBCCCDDDEEE";

        assertThat(diffContentService.getDiffContent(content1.getBytes(), content2.getBytes()))
        .isEqualTo(new DiffContent(NOT_EQUALS, List.of(new Diff(0, 3))));
    }

    @Test
    void shouldFindSingleDiffInLastThreeBytes() {
        var content1 = "AAABBBCCCDDDEEE";
        var content2 = "AAABBBCCCDDDFFF";

        assertThat(diffContentService.getDiffContent(content1.getBytes(), content2.getBytes()))
        .isEqualTo(new DiffContent(NOT_EQUALS, List.of(new Diff(12, 3))));
    }

    @Test
    void shouldFindSingleDiffAtOffsetSixWithLengthOfThreeBytes() {
        var content1 = "AAABBBCCCDDDEEE";
        var content2 = "AAABBBFFFDDDEEE";

        assertThat(diffContentService.getDiffContent(content1.getBytes(), content2.getBytes()))
        .isEqualTo(new DiffContent(NOT_EQUALS, List.of(new Diff(6, 3))));
    }

    @Test
    void shouldFindThreeDiffAtStartMiddleAndMiddleOfArray() {
        var content1 = "AAABBBCCCDDDEEE";
        var content2 = "FFFBBBFFFDDDFFF";

        assertThat(diffContentService.getDiffContent(content1.getBytes(), content2.getBytes()))
        .isEqualTo(new DiffContent(NOT_EQUALS, List.of(
            new Diff(0, 3),
            new Diff(6, 3),
            new Diff(12, 3)
        )));
    }

    @Test
    void shouldFindTwoDiffInMiddleOfArray() {
        var content1 = "AAABBBCCCDDDEEE";
        var content2 = "AAAFFFCCCFFFEEE";

        assertThat(diffContentService.getDiffContent(content1.getBytes(), content2.getBytes()))
        .isEqualTo(new DiffContent(NOT_EQUALS, List.of(
            new Diff(3, 3),
            new Diff(9, 3)
        )));
    }
}