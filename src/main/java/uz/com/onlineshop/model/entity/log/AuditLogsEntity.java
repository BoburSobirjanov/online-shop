package uz.com.onlineshop.model.entity.log;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import uz.com.onlineshop.model.entity.user.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "audit_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AuditLogsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(nullable = false)
    String httpMethod;

    @Column(nullable = false)
    String url;

    @Column(nullable = false, columnDefinition = "TEXT")
    String request;

    @Column(nullable = false, columnDefinition = "TEXT",length = 450)
    String response;

    @ManyToOne
    UserEntity user;

    String fromIpAddress;
}
