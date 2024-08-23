package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.request.ReviewDto;
import uz.com.onlineshop.model.dto.response.ReviewForFront;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.ReviewService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {



    private final ReviewService reviewService;





    @PostMapping("/save-review")
    public StandardResponse<ReviewForFront> save(
            @RequestBody ReviewDto reviewDto,
            Principal principal
            ){
        return reviewService.save(reviewDto, principal);
    }






    @GetMapping("/get-all-reviews-asc")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public Page<ReviewForFront> getAllReviewsByRatingAsc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getAllRatingByAsc(pageable);
    }








    @DeleteMapping("/{id}/delete-review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
            ){
      return  reviewService.delete(id, principal);
    }







    @GetMapping("/get-all-reviews-desc")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public Page<ReviewForFront> getAllReviewsByRatingDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getAllRatingByDesc(pageable);
    }





    @GetMapping("/get-all-reviews")
    public Page<ReviewForFront> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getAllReviews(pageable);
    }


}

