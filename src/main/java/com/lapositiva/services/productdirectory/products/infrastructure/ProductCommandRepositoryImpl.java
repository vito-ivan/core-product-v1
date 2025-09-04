package com.lapositiva.services.productdirectory.products.infrastructure;

import com.lapositiva.services.productdirectory.products.domain.model.ProductDto;
import com.lapositiva.services.productdirectory.products.domain.model.ProductEntity;
import com.lapositiva.services.productdirectory.products.domain.model.ProductPostgresqlEntity;
import com.lapositiva.services.productdirectory.products.domain.model.drools.RepositoryTypeDto;
import com.lapositiva.services.productdirectory.products.domain.repository.ProductCommandRepository;
import com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.ProductMongoDdRepository;
import com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.ProductPostgresqlRepository;
import com.lapositiva.services.productdirectory.products.infrastructure.outbound.drools.DroolsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.mongobd.utils.exception.CustomApiException.MB4091;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.mongobd.utils.exception.CustomApiException.MB5001;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.mongobd.utils.exception.ExceptionUtils.buildApiExceptionFromMongoDbThrowable;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.postgresql.utils.exception.CustomApiException.C4091;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.postgresql.utils.exception.CustomApiException.C5001;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.postgresql.utils.exception.ExceptionUtils.buildApiExceptionFromPostgresqlThrowable;

/**
 * Product Command Repository Implementation.
 *
 * @author Vito
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductCommandRepositoryImpl implements ProductCommandRepository {

  public static final String DEFAULT_REPOSITORY_TYPE_CODE = "mongodb";
  public static final String MONGODB = "mongodb";
  @NonNull
  private final ProductMongoDdRepository productMongoDdRepository;
  @NonNull
  private final ProductPostgresqlRepository productPostgreSqlRepository;
  @NonNull
  private final R2dbcEntityTemplate entityTemplate;
  @NonNull
  private final DroolsService droolsService;

  /**
   * Create a product.
   *
   * @param productDto the product to create.
   * @return the created product.
   */
  @Override
  public Mono<ProductDto> create(final ProductDto productDto) {

    var repositoryTypeDto = buildRepositoryTypeDto(productDto);

    return Mono.just(droolsService.executeRules(repositoryTypeDto))
        .flatMap(repositoryTypeDto1 -> {
          if (MONGODB.equals(repositoryTypeDto1.getOutboundCode())) {
            return createMongo(productDto);
          }
          return createPostgresql(productDto);
        });
  }

  private static RepositoryTypeDto buildRepositoryTypeDto(final ProductDto productDto) {

    return RepositoryTypeDto
        .builder()
        .outboundCode(DEFAULT_REPOSITORY_TYPE_CODE)
        .inboundCode(productDto.getRepositoryTypeCode())
        .build();
  }

  private Mono<ProductDto> createPostgresql(final ProductDto productDto) {

    return entityTemplate
        .insert(ProductPostgresqlEntity.class)
        .using(buildProductPostgresqlEntity(UUID.randomUUID().toString(), productDto))
        .doOnError((Throwable ex) ->
            log.error("Error creating product in postgresql: {}", ex.getMessage()))
        .onErrorResume((Throwable ex) ->
            Mono.error(buildApiExceptionFromPostgresqlThrowable(C5001, ex)))
        .map(productEntity -> productDto.toBuilder().id(productEntity.getId()).build());
  }

  private static ProductEntity buildProductEntity(final String id, final ProductDto productDto) {

    return ProductEntity.builder()
        .id(id)
        .code(productDto.getCode())
        .name(productDto.getName())
        .build();
  }

  private static ProductPostgresqlEntity buildProductPostgresqlEntity(final String id, final ProductDto productDto) {

    return ProductPostgresqlEntity.builder()
        .id(id)
        .code(productDto.getCode())
        .name(productDto.getName())
        .build();
  }

  private Mono<ProductDto> createMongo(final ProductDto productDto) {

    return productMongoDdRepository
        .save(buildProductEntity(UUID.randomUUID().toString(), productDto))
        .doOnError((Throwable ex) ->
            log.error("Error creating product in mongo: {}", ex.getMessage()))
        .onErrorResume((Throwable ex) ->
            Mono.error(buildApiExceptionFromMongoDbThrowable(MB5001, ex)))
        .map(productEntity -> productDto.toBuilder().id(productEntity.getId()).build());
  }

  /**
   * Update a product.
   *
   * @param productDto the product to update.
   * @return the updated product.
   */
  @Override
  public Mono<ProductDto> update(final ProductDto productDto) {

    var repositoryTypeDto = buildRepositoryTypeDto(productDto);

    return Mono.just(droolsService.executeRules(repositoryTypeDto))
        .flatMap(repositoryTypeDto1 -> {
          if (MONGODB.equals(repositoryTypeDto1.getOutboundCode())) {
            return updateMongo(productDto);
          }
          return updatePostgresql(productDto);
        });
  }

  private Mono<ProductDto> updateMongo(final ProductDto productDto) {

    return productMongoDdRepository
        .findById(productDto.getId())
        .doOnError((Throwable ex) ->
            log.error("Error obtaining product from mongo: {}", ex.getMessage()))
        .onErrorResume((Throwable ex) ->
            Mono.error(buildApiExceptionFromMongoDbThrowable(MB5001, ex)))
        .switchIfEmpty(Mono.defer(() -> Mono.error(MB4091.getException())))
        .flatMap(productEntity -> productMongoDdRepository
            .save(buildProductEntity(productDto.getId(), productDto))
            .doOnError((Throwable ex) ->
                log.error("Error updating product in mongo: {}", ex.getMessage()))
            .onErrorResume((Throwable ex) ->
                Mono.error(buildApiExceptionFromMongoDbThrowable(MB5001, ex))))
        .map(productEntity -> productDto);
  }

  private Mono<ProductDto> updatePostgresql(final ProductDto productDto) {

    return productPostgreSqlRepository
        .findById(productDto.getId())
        .doOnError((Throwable ex) ->
            log.error("Error obtaining product from postgresql: {}", ex.getMessage()))
        .onErrorResume((Throwable ex) ->
            Mono.error(buildApiExceptionFromPostgresqlThrowable(C5001, ex)))
        .switchIfEmpty(Mono.defer(() -> Mono.error(C4091.getException())))
        .flatMap(productEntity -> productPostgreSqlRepository
            .save(buildProductPostgresqlEntity(productDto.getId(), productDto))
            .doOnError((Throwable ex) ->
                log.error("Error updating product in postgresql: {}", ex.getMessage()))
            .onErrorResume((Throwable ex) ->
                Mono.error(buildApiExceptionFromPostgresqlThrowable(C5001, ex))))
        .map(productEntity -> productDto);
  }

  /**
   * Delete a product.
   *
   * @param productDto the product to delete.
   * @return the deleted product.
   */
  @Override
  public Mono<Void> delete(final ProductDto productDto) {

    var repositoryTypeDto = buildRepositoryTypeDto(productDto);

    return Mono.just(droolsService.executeRules(repositoryTypeDto))
        .flatMap(repositoryTypeDto1 -> {
          if (MONGODB.equals(repositoryTypeDto1.getOutboundCode())) {
            return deleteMongo(productDto.getId());
          }
          return deletePostgresql(productDto.getId());
        });
  }


  private Mono<Void> deleteMongo(final String id) {

    return productMongoDdRepository
        .findById(id)
        .doOnError((Throwable ex) ->
            log.error("Error obtaining product from mongo: {}", ex.getMessage()))
        .onErrorResume((Throwable ex) ->
            Mono.error(buildApiExceptionFromMongoDbThrowable(MB5001, ex)))
        .switchIfEmpty(Mono.defer(() -> Mono.error(MB4091.getException())))
        .flatMap(productEntity -> productMongoDdRepository
            .deleteById(id)
            .doOnError((Throwable ex) ->
                log.error("Error deleting product in mongo: {}", ex.getMessage()))
            .onErrorResume((Throwable ex) ->
                Mono.error(buildApiExceptionFromMongoDbThrowable(MB5001, ex))));
  }

  private Mono<Void> deletePostgresql(final String id) {

    return productPostgreSqlRepository
        .findById(id)
        .doOnError((Throwable ex) ->
            log.error("Error deleting product in postgresql: {}", ex.getMessage()))
        .onErrorResume((Throwable ex) ->
            Mono.error(buildApiExceptionFromPostgresqlThrowable(C5001, ex)))
        .switchIfEmpty(Mono.defer(() -> Mono.error(C4091.getException())))
        .flatMap(productEntity -> productPostgreSqlRepository
            .deleteById(id)
            .doOnError((Throwable ex) ->
                log.error("Error deleting product in postgresql: {}", ex.getMessage()))
            .onErrorResume((Throwable ex) ->
                Mono.error(buildApiExceptionFromPostgresqlThrowable(C5001, ex))));
  }
}
