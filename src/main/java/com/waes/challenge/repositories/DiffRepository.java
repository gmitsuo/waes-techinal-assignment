package com.waes.challenge.repositories;

import java.util.Optional;

public interface DiffRepository {

    /**
     * Save/update left diff
     * @param id
     * @param content
     * @return true when successfully save/update record
     */
    boolean saveLeftDiff(Integer id, byte[] content);

    /**
     * Save/update right diff
     * @param id
     * @param content
     * @return true when successfully save/update record
     */
    boolean saveRightDiff(Integer id, byte[] content);

    /**
     * Find left diff by its ID
     * @param id
     * @return Optional left diff if found, empty optional otherwise
     */
    Optional<byte[]> getLeftDiffById(Integer id);

    /**
     * Find right diff by its ID
     * @param id
     * @return Optional right diff if found, empty optional otherwise
     */
    Optional<byte[]> getRightDiffById(Integer id);
}
