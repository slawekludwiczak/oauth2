package com.ludigi.oauth2_resource_server.product;

import org.springframework.data.repository.ListCrudRepository;

interface ProductRepository extends ListCrudRepository<Product, Long> {
}
