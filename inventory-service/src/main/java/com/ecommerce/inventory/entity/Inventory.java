package com.ecommerce.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "inventory")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Inventory {
    @Id private Long productId;
    @Column(nullable = false) private Integer stock;
    @Column(nullable = false) private Integer reserved;
}
