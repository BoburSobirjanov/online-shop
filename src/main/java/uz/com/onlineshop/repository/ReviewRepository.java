package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.review.ReviewsEntity;

import java.util.UUID;
@Repository
public interface ReviewRepository extends JpaRepository<ReviewsEntity,UUID> {
}
