package com.ecommerce.order.outbox;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByPublishedFalse();
    List<OutboxEvent> findByPublishedFalse(org.springframework.data.domain.Pageable pageable);
}
