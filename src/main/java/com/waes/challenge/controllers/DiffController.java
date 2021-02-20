package com.waes.challenge.controllers;

import com.waes.challenge.controllers.models.ErrorResponse;
import com.waes.challenge.domain.DiffSide;
import com.waes.challenge.services.DiffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/diff")
public class DiffController {

    private final DiffService diffService;

    public DiffController(DiffService diffService) {
        this.diffService = diffService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiffById(@PathVariable("id") Integer id) {
        return ResponseEntity.of(diffService.getDiffById(id));
    }

    @PostMapping("/{id}/{side}")
    public ResponseEntity<?> createDiffLeft(
            @PathVariable("id") Integer id,
            @PathVariable("side")DiffSide diffSide,
            @RequestBody byte[] content) {

        try {
            diffService.createDiff(id, diffSide, content);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch (Exception e) {
            return ResponseEntity
            .badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
