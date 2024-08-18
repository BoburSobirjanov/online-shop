package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.card.CardEntity;

import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {

    @Query("select c from cards as c where c.isDeleted=false and c.cardNumber=?1")
    CardEntity findCardEntityByCardNumber(String number);



}
