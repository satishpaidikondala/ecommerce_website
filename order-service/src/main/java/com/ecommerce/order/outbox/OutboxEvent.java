package com.ecommerce.order.outbox;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "outbox_events")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OutboxEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String aggregateType;
    private Long aggregateId;
    private String eventType;
    @Column(columnDefinition = "TEXT") private String payload;
    private boolean published;
    private LocalDateTime createdAt;
    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); published = false; }
}
