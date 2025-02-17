package uz.com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.com.onlineshop.model.entity.log.AuditLogsEntity;

import java.util.UUID;

@Repository
public interface AuditLogsRepository extends JpaRepository<AuditLogsEntity, UUID> {
}
