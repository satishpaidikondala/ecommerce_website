package com.ecommerce.order.outbox;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "outbox_events")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OutboxEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @EqualsAndHashCode.Include private Long id;
    private String aggregateType;
    private Long aggregateId;
    private String eventType;
    @Column(columnDefinition = "TEXT") private String payload;
    @Builder.Default private boolean published = false;
    private LocalDateTime createdAt;
    @PrePersist void onCreate() { if (createdAt==null) createdAt = LocalDateTime.now(); }
}
