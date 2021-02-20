package com.waes.challenge.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository to hold a record in memory for every diff created.
 * Using HashMap as data structure to hold data for O(1) for
 * search, insertion and deletion operations.
 */
public class InMemoryDiffRepository implements DiffRepository {

    private final Map<Integer, byte[]> left;
    private final Map<Integer, byte[]> right;

    public InMemoryDiffRepository() {
        this.left = new HashMap<>();
        this.right = new HashMap<>();
    }

    public boolean saveLeftDiff(Integer id, byte[] content) {
        left.put(id, content);
        return true;
    }

    public boolean saveRightDiff(Integer id, byte[] content) {
        right.put(id, content);
        return true;
    }

    public Optional<byte[]> getLeftDiffById(Integer id) {
        return Optional.ofNullable(left.getOrDefault(id, null));
    }

    public Optional<byte[]> getRightDiffById(Integer id) {
        return Optional.ofNullable(right.getOrDefault(id, null));
    }
}
