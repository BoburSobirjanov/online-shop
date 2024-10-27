package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.payment.Payment;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {


    @Query("select p from payment as p where p.isDeleted=false and p.id=?1")
    Payment findPaymentById(UUID id);

    @Query("select p from payment as p where p.isDeleted=false and p.id in ?1")
    List<Payment> findAllById(List<UUID> id);
}
