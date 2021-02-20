package com.waes.challenge.domain;

import java.util.Objects;

public class Diff {

    private final Integer offset;
    private final Integer length;

    public Diff(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Diff diff = (Diff) o;
        return offset == diff.offset && length == diff.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, length);
    }

    @Override
    public String toString() {
        return "Diff{" +
                "offset=" + offset +
                ", length=" + length +
                '}';
    }
}
