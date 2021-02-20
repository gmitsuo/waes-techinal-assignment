package com.waes.challenge.services;

import com.waes.challenge.domain.DiffContent;
import com.waes.challenge.domain.DiffSide;
import com.waes.challenge.repositories.DiffRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiffService {

    private final DiffRepository diffRepository;
    private final DiffContentService diffContentService;
    private final DiffContentValidator diffContentValidator;

    public DiffService(
            DiffRepository diffRepository,
            DiffContentService diffContentService,
            DiffContentValidator diffContentValidator) {

        this.diffRepository = diffRepository;
        this.diffContentService = diffContentService;
        this.diffContentValidator = diffContentValidator;
    }

    public boolean createDiff(Integer id, DiffSide diffSide, byte[] content) {

        final byte[] validatedContent = diffContentValidator.validate(content);

        if (diffSide == null)
            throw new IllegalArgumentException("Diff side is mandatory");

        switch (diffSide) {
            case LEFT:
                return diffRepository.saveLeftDiff(id, validatedContent);
            case RIGHT:
                return  diffRepository.saveRightDiff(id, validatedContent);
        }

        return false;
    }

    public Optional<DiffContent> getDiffById(Integer id) {

        return diffRepository
        .getLeftDiffById(id).flatMap(left ->

            diffRepository.getRightDiffById(id)
            .map(right -> diffContentService.getDiffContent(left, right))
        );
    }
}
