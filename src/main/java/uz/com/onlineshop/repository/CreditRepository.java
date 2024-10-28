package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.credit.Credit;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface CreditRepository extends JpaRepository<Credit, UUID> {

    @Query("select c from credit as c where  c.isDeleted=false and c.id=?1")
    Optional<Credit> findCreditById(UUID id);
}
