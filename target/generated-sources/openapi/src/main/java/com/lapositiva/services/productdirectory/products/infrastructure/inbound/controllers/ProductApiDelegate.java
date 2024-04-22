package com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers;

import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.exception.ApiException;
import com.lapositiva.services.productdirectory.products.domain.model.PostProductResponse;
import com.lapositiva.services.productdirectory.products.domain.model.ProductRequest;
import com.lapositiva.services.productdirectory.products.domain.model.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.codec.multipart.Part;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

/**
 * A delegate to be called by the {@link ProductApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-04-20T19:34:17.755954300-05:00[America/Lima]")
public interface ProductApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /products : Allows you to create a product
     *
     * @param repositoryTypeCode Repository type code (required)
     * @param productRequest Product object (optional)
     * @return The resource was send successfully. (status code 201)
     *         or Invalid data supplied (status code 400)
     *         or Internal Server Error (status code 500)
     * @see ProductApi#create
     */
    default Mono<ResponseEntity<PostProductResponse>> create(String repositoryTypeCode,
        Mono<ProductRequest> productRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"id\" : \"07c796f9-da9f-43aa-a8ae-fbd9ca24bc24\" }";
                result = ApiUtil.getExampleResponse(exchange, mediaType, exampleString);
                break;
            }
        }
        return result.then(productRequest).then(Mono.empty());

    }

    /**
     * DELETE /products/{id} : Allows you to delete a product
     *
     * @param repositoryTypeCode Repository type code (required)
     * @param id ID of the product to update (required)
     * @return The resource was send successfully. (status code 204)
     *         or Invalid data supplied (status code 400)
     *         or Invalid data supplied (status code 409)
     *         or Internal Server Error (status code 500)
     * @see ProductApi#delete
     */
    default Mono<ResponseEntity<Void>> delete(String repositoryTypeCode,
        String id,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        return result.then(Mono.empty());

    }

    /**
     * GET /products : Allows you to obtain all products
     *
     * @param repositoryTypeCode Repository type code (required)
     * @return The resource was send successfully. (status code 204)
     *         or The resource was send successfully. (status code 200)
     *         or Invalid data supplied (status code 400)
     *         or Internal Server Error (status code 500)
     * @see ProductApi#findAll
     */
    default Mono<ResponseEntity<Flux<ProductResponse>>> findAll(String repositoryTypeCode,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "[ { \"code\" : \"P001\", \"name\" : \"Seguro de Vida\", \"id\" : \"07c796f9-da9f-43aa-a8ae-fbd9ca24bc24\" }, { \"code\" : \"P001\", \"name\" : \"Seguro de Vida\", \"id\" : \"07c796f9-da9f-43aa-a8ae-fbd9ca24bc24\" } ]";
                result = ApiUtil.getExampleResponse(exchange, mediaType, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * GET /products/{id} : Allows you to obtain a product
     *
     * @param repositoryTypeCode Repository type code (required)
     * @param id ID of the product to update (required)
     * @return The resource was send successfully. (status code 204)
     *         or The resource was send successfully. (status code 200)
     *         or Invalid data supplied (status code 400)
     *         or Internal Server Error (status code 500)
     * @see ProductApi#findById
     */
    default Mono<ResponseEntity<ProductResponse>> findById(String repositoryTypeCode,
        String id,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"code\" : \"P001\", \"name\" : \"Seguro de Vida\", \"id\" : \"07c796f9-da9f-43aa-a8ae-fbd9ca24bc24\" }";
                result = ApiUtil.getExampleResponse(exchange, mediaType, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * PUT /products/{id} : Allows you to update a product
     *
     * @param repositoryTypeCode Repository type code (required)
     * @param id ID of the product to update (required)
     * @param productRequest Product object (optional)
     * @return The resource was send successfully. (status code 204)
     *         or Invalid data supplied (status code 400)
     *         or Invalid data supplied (status code 409)
     *         or Internal Server Error (status code 500)
     * @see ProductApi#update
     */
    default Mono<ResponseEntity<Void>> update(String repositoryTypeCode,
        String id,
        Mono<ProductRequest> productRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        return result.then(productRequest).then(Mono.empty());

    }

}
