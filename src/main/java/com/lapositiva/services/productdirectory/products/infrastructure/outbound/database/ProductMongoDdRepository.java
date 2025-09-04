package com.lapositiva.services.productdirectory.products.infrastructure.outbound.database;

import com.lapositiva.services.productdirectory.products.domain.model.ProductEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Product MongoDb Repository.
 * This interface is used to interact with the MongoDb database.
 * It extends the ReactiveMongoRepository interface.
 * @see ReactiveMongoRepository
 * @see ProductEntity
 * @see ProductMongoDdRepository
 */
@Repository
public interface ProductMongoDdRepository extends ReactiveMongoRepository<ProductEntity, String> {

}