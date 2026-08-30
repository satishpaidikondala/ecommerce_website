package com.ecommerce.product.search;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {

    List<ProductDocument> findByNameContainingOrDescriptionContaining(String name, String description);

    List<ProductDocument> findByActiveTrue();
}
