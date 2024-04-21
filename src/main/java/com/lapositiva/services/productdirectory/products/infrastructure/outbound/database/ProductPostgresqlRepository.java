package com.lapositiva.services.productdirectory.products.infrastructure.outbound.database;

import com.lapositiva.services.productdirectory.products.domain.model.ProductPostgresqlEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Product Postgresql Repository.
 * This interface is used to interact with the Postgresql database.
 * It extends the ReactiveCrudRepository interface.
 * @see ReactiveCrudRepository
 * @see ProductPostgresqlEntity
 * @see ProductPostgresqlRepository
 */
public interface ProductPostgresqlRepository extends ReactiveCrudRepository<ProductPostgresqlEntity, String> {

}
