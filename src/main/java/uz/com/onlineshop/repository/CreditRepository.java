package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.credit.Credit;

import java.util.UUID;
@Repository
public interface CreditRepository extends JpaRepository<Credit, UUID> {
}
