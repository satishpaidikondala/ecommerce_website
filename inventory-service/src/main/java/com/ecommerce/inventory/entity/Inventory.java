package com.ecommerce.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "inventory")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Inventory {
    @Id @Column(name = "product_id") private Long productId;
    @Builder.Default @Column(nullable = false) private Integer stock = 0;
    @Builder.Default @Column(nullable = false) private Integer reserved = 0;
    @Version private Long version;
}
