package com.lapositiva.services.productdirectory.products.infrastructure;

import com.lapositiva.services.productdirectory.products.domain.model.ProductDto;
import com.lapositiva.services.productdirectory.products.domain.model.ProductEntity;
import com.lapositiva.services.productdirectory.products.domain.model.ProductPostgresqlEntity;
import com.lapositiva.services.productdirectory.products.domain.model.drools.RepositoryTypeDto;
import com.lapositiva.services.productdirectory.products.domain.repository.ProductQueryRepository;
import com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.ProductMongoDdRepository;
import com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.ProductPostgresqlRepository;
import com.lapositiva.services.productdirectory.products.infrastructure.outbound.drools.DroolsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.mongobd.utils.exception.CustomApiException.MB5001;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.mongobd.utils.exception.ExceptionUtils.buildApiExceptionFromMongoDbThrowable;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.postgresql.utils.exception.CustomApiException.C5001;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.postgresql.utils.exception.ExceptionUtils.buildApiExceptionFromPostgresqlThrowable;

/**
 * Product Query Repository Implementation.
 * @author Vito
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

  public static final String DEFAULT_REPOSITORY_TYPE_CODE = "mongodb";
  public static final String MONGODB = "mongodb";
  @NonNull
  private final ProductMongoDdRepository productMongoDdRepository;
  @NonNull
  private final ProductPostgresqlRepository productPostgreSqlRepository;
  @NonNull
  private final DroolsService droolsService;

  @Override
  public Mono<ProductDto> findById(final ProductDto productDto) {

    var repositoryTypeDto = buildRepositoryTypeDto(productDto);

    return Mono.just(droolsService.executeRules(repositoryTypeDto))
        .flatMap(repositoryTypeDto1 -> {
          if (MONGODB.equals(repositoryTypeDto1.getOutboundCode())) {
            return findByIdMongo(productDto);
          }
          return findByIdPostgresql(productDto);
        });
  }

  private static RepositoryTypeDto buildRepositoryTypeDto(final ProductDto productDto) {

    return RepositoryTypeDto
        .builder()
        .outboundCode(DEFAULT_REPOSITORY_TYPE_CODE)
        .inboundCode(productDto.getRepositoryTypeCode())
        .build();
  }

  @CircuitBreaker(name = "product", fallbackMethod = "fallbackForFindById")
  @Retry(name = "product", fallbackMethod = "fallbackForFindById")
  private Mono<ProductDto> findByIdMongo(final ProductDto productDto) {

    return productMongoDdRepository
        .findById(productDto.getId())
        .doOnError((Throwable ex) -> log.error("Error obtaining product from mongo", ex))
        .onErrorResume((Throwable ex) ->
            Mono.error(buildApiExceptionFromMongoDbThrowable(MB5001, ex)))
        .map(ProductQueryRepositoryImpl::buildProductDto);
  }

  public Mono<ProductDto> fallbackForFindById(final ProductDto productDto, final Throwable e) {
    // Aquí puedes manejar cómo se debe comportar el método cuando se produce un error.
    // Por ejemplo, puedes devolver un Mono vacío.
    return Mono.empty();
  }

  private Mono<ProductDto> findByIdPostgresql(final ProductDto productDto) {

    return productPostgreSqlRepository
        .findById(productDto.getId())
        .doOnError((Throwable ex) -> log.error("Error obtaining product from postgresql", ex))
        .onErrorResume((Throwable ex) ->
            Mono.error(buildApiExceptionFromPostgresqlThrowable(C5001, ex)))
        .map(ProductQueryRepositoryImpl::buildProductDto);
  }

  private static ProductDto buildProductDto(final ProductEntity entity) {

    return ProductDto.builder()
        .id(entity.getId())
        .code(entity.getCode())
        .name(entity.getName())
        .build();
  }

  private static ProductDto buildProductDto(final ProductPostgresqlEntity entity) {

    return ProductDto.builder()
        .id(entity.getId())
        .code(entity.getCode())
        .name(entity.getName())
        .build();
  }

  @Override
  public Flux<ProductDto> findAll(final ProductDto productDto) {

    var repositoryTypeDto = buildRepositoryTypeDto(productDto);

    return Flux.just(droolsService.executeRules(repositoryTypeDto))
        .flatMap(repositoryTypeDto1 -> {
          if (MONGODB.equals(repositoryTypeDto1.getOutboundCode())) {
            return findAllMongo();
          }
          return findAllPostgresql();
        });
  }

  private Flux<ProductDto> findAllMongo() {

    return productMongoDdRepository
        .findAll()
        .doOnError((Throwable ex) -> log.error("Error obtaining all products from mongo", ex))
        .onErrorResume((Throwable ex) ->
            Mono.error(buildApiExceptionFromMongoDbThrowable(MB5001, ex)))
        .map(ProductQueryRepositoryImpl::buildProductDto);
  }

  private Flux<ProductDto> findAllPostgresql() {

    return productPostgreSqlRepository
        .findAll()
        .doOnError((Throwable ex) -> log.error("Error obtaining all products from postgresql", ex))
        .onErrorResume((Throwable ex) ->
            Mono.error(buildApiExceptionFromPostgresqlThrowable(C5001, ex)))
        .map(ProductQueryRepositoryImpl::buildProductDto);
  }
}