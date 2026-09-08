package com.ecommerce.product.search;

import java.util.List;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {

    @Query("{\"bool\": {\"should\": [{\"match\": {\"name\": \"?0\"}}, {\"match\": {\"description\": \"?1\"}}]}}")
    List<ProductDocument> findByNameContainingOrDescriptionContaining(String name, String description);

    List<ProductDocument> findByActiveTrue();
}
