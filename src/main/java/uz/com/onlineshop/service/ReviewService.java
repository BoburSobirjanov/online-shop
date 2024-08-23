package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.model.dto.request.ReviewDto;
import uz.com.onlineshop.model.dto.response.ProductForFront;
import uz.com.onlineshop.model.dto.response.ReviewForFront;
import uz.com.onlineshop.model.dto.response.UserForFront;
import uz.com.onlineshop.model.entity.review.ReviewsEntity;
import uz.com.onlineshop.repository.ProductRepository;
import uz.com.onlineshop.repository.ReviewRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;




    public StandardResponse<ReviewForFront> save(ReviewDto reviewDto, Principal principal){
        ReviewsEntity reviewsEntity = modelMapper.map(reviewDto, ReviewsEntity.class);
        reviewsEntity.setComment(reviewDto.getComment());
        reviewsEntity.setRating(reviewDto.getRating());
        reviewsEntity.setUserId(userRepository.findUserEntityByEmail(principal.getName()));
        reviewsEntity.setProductId(productRepository.findProductEntityById(UUID.fromString(reviewDto.getProductId())));
        ReviewsEntity save = reviewRepository.save(reviewsEntity);
        ReviewForFront review = modelMapper.map(save, ReviewForFront.class);

        return StandardResponse.<ReviewForFront>builder()
                .data(review)
                .status(Status.SUCCESS)
                .message("Review saved!")
                .build();
    }








    public StandardResponse<String> delete(UUID id, Principal principal){
        ReviewsEntity reviews = reviewRepository.findReviewsEntityById(id);
        if (reviews==null){
            throw new DataNotFoundException("Review not found!");
        }
        reviews.setDeleted(true);
        reviews.setDeletedTime(LocalDateTime.now());
        reviews.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        reviewRepository.save(reviews);

        return StandardResponse.<String>builder()
                .data("DELETED")
                .status(Status.SUCCESS)
                .message("Review deleted!")
                .build();
    }









    public Page<ReviewForFront> getAllRatingByAsc(Pageable pageable){
        Page<ReviewsEntity> reviewsEntities = reviewRepository.findAllReviewsByRatingAsc(pageable);
        return reviewsEntities.map(reviewsEntity -> new ReviewForFront(modelMapper.map(reviewsEntity.getProductId(), ProductForFront.class),
                modelMapper.map(reviewsEntity.getUserId(), UserForFront.class),
                reviewsEntity.getRating(), reviewsEntity.getComment()));

    }








    public Page<ReviewForFront> getAllRatingByDesc(Pageable pageable){
        Page<ReviewsEntity> reviewsEntities = reviewRepository.findAllReviewsByRatingDesc(pageable);
        return reviewsEntities.map(reviewsEntity -> new ReviewForFront(modelMapper.map(reviewsEntity.getProductId(), ProductForFront.class),
                modelMapper.map(reviewsEntity.getUserId(), UserForFront.class),
                reviewsEntity.getRating(), reviewsEntity.getComment()));

    }







    public Page<ReviewForFront> getAllReviews(Pageable pageable){
        Page<ReviewsEntity> reviewsEntities = reviewRepository.findAllReviews(pageable);
        return reviewsEntities.map(reviewsEntity -> new ReviewForFront(modelMapper.map(reviewsEntity.getProductId(), ProductForFront.class),
                modelMapper.map(reviewsEntity.getUserId(), UserForFront.class),
                reviewsEntity.getRating(), reviewsEntity.getComment()));
    }






    public Page<ReviewForFront> findReviewsByUserId(Pageable pageable, UUID id){
        Page<ReviewsEntity> reviewsEntities = reviewRepository.findReviewsEntityByUserId(pageable,id);
        return reviewsEntities.map(reviewsEntity -> new ReviewForFront(modelMapper.map(reviewsEntity.getProductId(), ProductForFront.class),
                modelMapper.map(reviewsEntity.getUserId(), UserForFront.class),
                reviewsEntity.getRating(), reviewsEntity.getComment()));
    }
}
