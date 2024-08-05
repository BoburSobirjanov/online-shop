package uz.com.onlineshop.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @CreationTimestamp
    protected LocalDateTime createdTime;

    @UpdateTimestamp
    protected LocalDateTime updatedTime;

    protected LocalDateTime deletedTime;

    @Column(columnDefinition = "boolean default false")
    protected boolean isDeleted;

    protected UUID deletedBy;

    protected UUID updatedBy;
}
