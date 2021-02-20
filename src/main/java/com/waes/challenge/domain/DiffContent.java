package com.waes.challenge.domain;

import java.util.List;
import java.util.Objects;

public class DiffContent {

    private final DiffComparison diffComparison;
    private final List<Diff> differences;

    public DiffContent(DiffComparison diffComparison, List<Diff> differences) {
        this.diffComparison = diffComparison;
        this.differences = differences;
    }

    public DiffComparison getDiffComparison() {
        return diffComparison;
    }

    public List<Diff> getDifferences() {
        return differences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DiffContent that = (DiffContent) o;
        return diffComparison == that.diffComparison && Objects.equals(differences, that.differences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diffComparison, differences);
    }

    @Override
    public String toString() {
        return "DiffContent{" +
                "diffComparison=" + diffComparison +
                ", differences=" + differences +
                '}';
    }
}
