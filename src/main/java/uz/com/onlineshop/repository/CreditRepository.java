package uz.com.onlineshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.credit.Credit;
import uz.com.onlineshop.model.entity.order.OrderEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface CreditRepository extends JpaRepository<Credit, UUID> {


    @Query("select c from credit as c where  c.isDeleted=false and c.id=?1")
    Optional<Credit> findCreditById(UUID id);



    @Query("select c from credit as c where c.isDeleted=false and c.order=?1")
    Optional<Credit> findCreditByOrder(OrderEntity order);


    @Query("select c from credit as c where c.isDeleted=false")
    Page<Credit> findAllCredits(Pageable pageable);



    @Query("select c from credit as c where c.isDeleted=false and c.user=?1")
    Page<Credit> findAllByUser(UserEntity user, Pageable pageable);
}
