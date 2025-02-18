package uz.com.onlineshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.review.ReviewsEntity;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewsEntity, UUID> {


    @Query("select r from reviews as r where r.isDeleted=false and r.id=?1")
    ReviewsEntity findReviewsEntityById(UUID id);


    @Query("select r from reviews as r where r.isDeleted=false order by r.rating desc")
    Page<ReviewsEntity> findAllReviewsByRatingDesc(Pageable pageable);


    @Query("select r from reviews as r where r.isDeleted=false order by r.rating asc ")
    Page<ReviewsEntity> findAllReviewsByRatingAsc(Pageable pageable);


    @Query("select r from reviews as r where r.isDeleted=false")
    Page<ReviewsEntity> findAllReviews(Pageable pageable);


    @Query("select r from reviews as r where r.isDeleted=false and r.user=?1")
    Page<ReviewsEntity> findReviewsEntityByUserId(Pageable pageable, UUID userId);
}
