package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.user.VerificationEntity;

import java.util.UUID;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, UUID> {


    @Query("select u from verification as u where u.userId=?1 and u.code=?2 ")
    VerificationEntity findByUserEmailAndCode(UUID id, String code);




    @Query("select u from verification as u where u.userId=?1")
    VerificationEntity findVerificationEntityByUserId(UUID id);

}
