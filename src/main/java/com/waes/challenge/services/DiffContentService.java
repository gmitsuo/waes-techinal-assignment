package com.waes.challenge.services;

import com.waes.challenge.domain.Diff;
import com.waes.challenge.domain.DiffContent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.waes.challenge.domain.DiffComparison.*;

@Service
public class DiffContentService {

    public DiffContent getDiffContent(byte[] left, byte[] right) {

        if (right.length !=  left.length) {
            return new DiffContent(DIFFERENT_LENGTHS, null);
        }

        final var diffs = getDiffs(left, right);
        final var diffComparison = diffs.isEmpty() ? EQUALS : NOT_EQUALS;

        return new DiffContent(diffComparison, diffs);
    }


    private  List<Diff> getDiffs(byte[] left, byte[] right) {

        final var diffs = new ArrayList<Diff>();

        for(int offset = 0; offset < left.length; offset++) {

            if (left[offset] == right[offset])
                continue;

            final var diff = getDiffLength(offset, left, right);

            offset += diff.getLength();
            diffs.add(diff);
        }

        return diffs;
    }

    private Diff getDiffLength(int offset, byte[] left, byte[] right) {

        int diffEnd = offset;

        while(diffEnd < left.length && left[diffEnd] != right[diffEnd])
            diffEnd++;

        return new Diff(offset, diffEnd-offset);
    }
}
