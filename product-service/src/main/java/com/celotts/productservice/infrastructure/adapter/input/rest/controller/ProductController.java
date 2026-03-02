package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.domain.port.input.product.ProductUseCase;
import com.celotts.productservice.domain.port.input.stock.ReceiveStockUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.ListResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.PageResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.stock.StockReceptionDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductMapper;
import com.celotts.productservice.infrastructure.common.ApiResult;
import com.celotts.productservice.infrastructure.config.PaginationProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
@Slf4j
@Tag(name = "${swagger.product.api.name}", description = "${swagger.product.api.desc}")
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ReceiveStockUseCase receiveStockUseCase;
    private final ProductMapper productMapper;
    private final PaginationProperties paginationProperties;
    private final MessageSource messageSource;

    private String msg(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    @GetMapping("/test")
    @Operation(summary = "${swagger.product.test.summary}", description = "${swagger.product.test.desc}")
    public ResponseEntity<ApiResult<String>> testEndpoint() {
        return ApiResult.success(msg("app.status.ok"), "OK");
    }

    @PostMapping
    @Operation(summary = "${swagger.product.create.summary}", description = "${swagger.product.create.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "409", description = "Product code already exists", content = @Content)
    })
    public ResponseEntity<ApiResult<ProductResponseDto>> create(@RequestBody @Valid ProductCreateDto createDto) {
        log.info("Creating new product with code: {}", createDto.getCode());
        ProductModel created = productUseCase.createProduct(productMapper.toModel(createDto));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ApiResult.created(productMapper.toResponse(created), msg("product.created"), location);
    }

    @PostMapping("/receive-stock")
    @Operation(summary = "${swagger.product.receive-stock.summary}", description = "${swagger.product.receive-stock.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock received successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    public ResponseEntity<ApiResult<Map<String, Object>>> receiveStock(@RequestBody @Valid StockReceptionDto receptionDto) {
        log.info("Receiving stock for product: {}", receptionDto.getProductId());
        LotModel lot = receiveStockUseCase.receiveStock(receptionDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("lotId", lot.getId());
        response.put("lotCode", lot.getLotCode());
        
        return ApiResult.success(response, msg("product.stock.received"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "${swagger.product.update.summary}", description = "${swagger.product.update.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    public ResponseEntity<ApiResult<ProductResponseDto>> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid ProductUpdateDto updateDto) {
        log.info("Updating product with id: {}", id);
        ProductModel updated = productUseCase.updateProduct(id, productMapper.toModel(updateDto));
        return ApiResult.success(productMapper.toResponse(updated), msg("product.updated"));
    }

    @GetMapping
    @Operation(summary = "${swagger.product.list.summary}", description = "${swagger.product.list.desc}")
    public ResponseEntity<ApiResult<ListResponse<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> response = productMapper.toResponseList(
                productUseCase.getActiveProducts(Pageable.unpaged()).getContent()
        );
        return ApiResult.success(ListResponse.of(response), msg("product.list"));
    }

    @GetMapping("/paginated")
    @Operation(summary = "${swagger.product.paginated.summary}",
            description = "${swagger.product.paginated.desc}")
    public ResponseEntity<ApiResult<PageResponse<ProductResponseDto>>> getAllProductsPaginated(
            @Valid @ModelAttribute ProductRequestDto requestDto
    ) {
        // Fallbacks si el cliente no envía valores
        int page = Optional.ofNullable(requestDto.getPage())
                .filter(p -> p >= 0)
                .orElse(paginationProperties.getDefaultPage());

        int size = Optional.ofNullable(requestDto.getSize())
                .map(s -> Math.max(1, Math.min(s, paginationProperties.getMaxSize())))
                .orElse(paginationProperties.getDefaultSize());

        String sortBy = Optional.ofNullable(requestDto.getSortBy())
                .filter(s -> !s.isBlank())
                .orElse(paginationProperties.getDefaultSort());

        String sortDir = Optional.ofNullable(requestDto.getSortDir())
                .filter(s -> !s.isBlank())
                .orElse(paginationProperties.getDefaultDirection());

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        String code = requestDto.getCode();
        String name = requestDto.getName();
        String description = requestDto.getDescription();

        Page<ProductModel> products = (code != null || name != null || description != null)
                ? productUseCase.getAllProductsWithFilters(pageable, code, name, description)
                : productUseCase.getAllProducts(pageable);

        return ApiResult.success(PageResponse.from(products, productMapper::toResponse), msg("product.list"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "${swagger.product.get-by-id.summary}", description = "${swagger.product.get-by-id.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    public ResponseEntity<ApiResult<ProductResponseDto>> getProductById(@PathVariable UUID id) {
        ProductModel product = productUseCase.getProductById(id);
        return ApiResult.success(productMapper.toResponse(product), msg("product.found"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "${swagger.product.delete.summary}", description = "${swagger.product.delete.desc}")
    public ResponseEntity<ApiResult<Void>> deleteProduct(@PathVariable UUID id) {
        productUseCase.disableProduct(id);
        return ApiResult.success(null, msg("product.deleted"));
    }

    @DeleteMapping("/{id}/hard")
    @Operation(summary = "${swagger.product.hard-delete.summary}", description = "${swagger.product.hard-delete.desc}")
    public ResponseEntity<ApiResult<Void>> hardDeleteProduct(@PathVariable UUID id) {
        productUseCase.hardDeleteProduct(id);
        return ApiResult.success(null, msg("product.deleted"));
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "${swagger.product.enable.summary}", description = "${swagger.product.enable.desc}")
    public ResponseEntity<ApiResult<ProductResponseDto>> enableProduct(@PathVariable UUID id) {
        ProductModel enabledProduct = productUseCase.enableProduct(id);
        return ApiResult.success(productMapper.toResponse(enabledProduct), msg("product.updated"));
    }

    @GetMapping("/inactive")
    @Operation(summary = "${swagger.product.inactive.summary}", description = "${swagger.product.inactive.desc}")
    public ResponseEntity<ApiResult<ListResponse<ProductResponseDto>>> getInactiveProducts() {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getInactiveProducts());
        return ApiResult.success(ListResponse.of(response), msg("product.list"));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "${swagger.product.by-category.summary}", description = "${swagger.product.by-category.desc}")
    public ResponseEntity<ApiResult<ListResponse<ProductResponseDto>>> getProductsByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getProductsByCategory(categoryId));
        return ApiResult.success(ListResponse.of(response), msg("product.list"));
    }

    @GetMapping("/category/{categoryId}/low-stock")
    @Operation(summary = "${swagger.product.low-stock-category.summary}", description = "${swagger.product.low-stock-category.desc}")
    public ResponseEntity<ApiResult<ListResponse<ProductResponseDto>>> getLowStockByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getLowStockByCategory(categoryId));
        return ApiResult.success(ListResponse.of(response), msg("product.list"));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "${swagger.product.low-stock.summary}", description = "${swagger.product.low-stock.desc}")
    public ResponseEntity<ApiResult<ListResponse<ProductResponseDto>>> getLowStockProducts() {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getLowStockProducts());
        return ApiResult.success(ListResponse.of(response), msg("product.list"));
    }

    @GetMapping("/brand/{brandId}")
    @Operation(summary = "${swagger.product.by-brand.summary}", description = "${swagger.product.by-brand.desc}")
    public ResponseEntity<ApiResult<ListResponse<ProductResponseDto>>> getProductsByBrand(@PathVariable UUID brandId) {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getProductsByBrand(brandId));
        return ApiResult.success(ListResponse.of(response), msg("product.list"));
    }

    @GetMapping("/count")
    @Operation(summary = "${swagger.product.count.summary}", description = "${swagger.product.count.desc}")
    public ResponseEntity<ApiResult<Long>> countProducts() {
        return ApiResult.success(productUseCase.countProducts(), "OK");
    }

    @GetMapping("/count/active")
    @Operation(summary = "${swagger.product.count-active.summary}", description = "${swagger.product.count-active.desc}")
    public ResponseEntity<ApiResult<Long>> countActiveProducts() {
        return ApiResult.success(productUseCase.countActiveProducts(), "OK");
    }

    @GetMapping("/validate-unit/{code}")
    @Operation(summary = "${swagger.product.validate-unit.summary}", description = "${swagger.product.validate-unit.desc}")
    public ResponseEntity<ApiResult<Map<String, Object>>> validateUnit(@PathVariable String code) {
        Optional<String> name = productUseCase.validateUnitCode(code);
        Map<String, Object> response = new HashMap<>();
        response.put("valid", name.isPresent());
        response.put("unitName", name.orElse(null));
        return ApiResult.success(response, "OK");
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "${swagger.product.update-stock.summary}", description = "${swagger.product.update-stock.desc}")
    public ResponseEntity<ApiResult<ProductResponseDto>> updateStock(@PathVariable UUID id, @RequestBody @Valid UpdateStockDto stockDto) {
        ProductModel updated = productUseCase.updateStock(id, stockDto.getStock());
        return ApiResult.success(productMapper.toResponse(updated), msg("product.stock.updated"));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "${swagger.product.get-by-code.summary}", description = "${swagger.product.get-by-code.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    public ResponseEntity<ApiResult<ProductResponseDto>> getProductByCode(@PathVariable String code) {
        if (code == null || code.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        ProductModel product = productUseCase.getProductByCode(code);
        return (product == null)
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ApiResult.success(productMapper.toResponse(product), msg("product.found"));
    }
}
