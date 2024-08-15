package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.apicount.ApiRequestCount;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiRequestCountRepository extends JpaRepository<ApiRequestCount, UUID> {
    Optional<ApiRequestCount> findByApiEndpoint(String apiEndpoint);
}
