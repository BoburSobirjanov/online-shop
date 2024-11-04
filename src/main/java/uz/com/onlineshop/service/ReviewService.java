package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.mapper.ReviewMapper;
import uz.com.onlineshop.model.dto.request.ReviewDto;
import uz.com.onlineshop.model.dto.response.ReviewForFrontDto;
import uz.com.onlineshop.model.entity.review.ReviewsEntity;
import uz.com.onlineshop.repository.ProductRepository;
import uz.com.onlineshop.repository.ReviewRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.standard.StandardResponse;

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
    private final ReviewMapper reviewMapper;




    public StandardResponse<ReviewForFrontDto> save(ReviewDto reviewDto, Principal principal){
        ReviewsEntity reviewsEntity = modelMapper.map(reviewDto, ReviewsEntity.class);
        reviewsEntity.setComment(reviewDto.getComment());
        reviewsEntity.setRating(reviewDto.getRating());
        reviewsEntity.setUserId(userRepository.findUserEntityByEmail(principal.getName()));
        reviewsEntity.setProductId(productRepository.findProductEntityById(UUID.fromString(reviewDto.getProductId())));
        ReviewsEntity save = reviewRepository.save(reviewsEntity);
        ReviewForFrontDto review = modelMapper.map(save, ReviewForFrontDto.class);

        return StandardResponse.ok("Review saved",review);
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

        return StandardResponse.ok("Review deleted!","DELETED");
    }









    public Page<ReviewForFrontDto> getAllRatingByAsc(Pageable pageable){
        Page<ReviewsEntity> reviewsEntities = reviewRepository.findAllReviewsByRatingAsc(pageable);
        return reviewsEntities.map(reviewMapper::toDto);

    }








    public Page<ReviewForFrontDto> getAllRatingByDesc(Pageable pageable){
        Page<ReviewsEntity> reviewsEntities = reviewRepository.findAllReviewsByRatingDesc(pageable);
        return reviewsEntities.map(reviewMapper::toDto);

    }







    public Page<ReviewForFrontDto> getAllReviews(Pageable pageable){
        Page<ReviewsEntity> reviewsEntities = reviewRepository.findAllReviews(pageable);
        return reviewsEntities.map(reviewMapper::toDto);
    }






    public Page<ReviewForFrontDto> findReviewsByUserId(Pageable pageable, UUID id){
        Page<ReviewsEntity> reviewsEntities = reviewRepository.findReviewsEntityByUserId(pageable,id);
        return reviewsEntities.map(reviewMapper::toDto);
    }
}
