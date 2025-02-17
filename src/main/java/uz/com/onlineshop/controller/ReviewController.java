package uz.com.onlineshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.exception.RequestValidationException;
import uz.com.onlineshop.model.dto.request.ReviewDto;
import uz.com.onlineshop.model.dto.response.ReviewForFrontDto;
import uz.com.onlineshop.standard.StandardResponse;
import uz.com.onlineshop.service.ReviewService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
@CrossOrigin
public class ReviewController {


    private final ReviewService reviewService;


    @PostMapping("/save-review")
    public ResponseEntity<StandardResponse<ReviewForFrontDto>> save(
            @Valid
            @RequestBody ReviewDto reviewDto,
            Principal principal, BindingResult bindingResult
    ) throws RequestValidationException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }
        return ResponseEntity.ok(reviewService.save(reviewDto, principal));
    }


    @GetMapping("/get-all-reviews-asc")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public Page<ReviewForFrontDto> getAllReviewsByRatingAsc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getAllRatingByAsc(pageable);
    }


    @DeleteMapping("/{id}/delete-review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
    ) {
        return reviewService.delete(id, principal);
    }


    @GetMapping("/get-all-reviews-desc")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public Page<ReviewForFrontDto> getAllReviewsByRatingDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getAllRatingByDesc(pageable);
    }


    @GetMapping("/get-all-reviews")
    public Page<ReviewForFrontDto> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getAllReviews(pageable);
    }


}

