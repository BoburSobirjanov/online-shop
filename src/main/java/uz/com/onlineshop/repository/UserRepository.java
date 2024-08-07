package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.user.UserEntity;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,UUID> {
    @Query("select u from users as u where u.isDeleted=false and u.email=?1")
    UserEntity findUserEntityByEmail(String email);
    @Query("select u from users as u where u.isDeleted=false and u.phoneNumber=?1")
    UserEntity findUserEntityByPhoneNumber(String phoneNumber);
    @Query("select u from users as u where u.isDeleted=false and u.id=?1")
    UserEntity findUserEntityById(UUID id);
}
