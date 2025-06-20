package com.celotts.productservice.applications.service;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductBrandEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductBrandRepository;
import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductBrandService {

    private final ProductBrandRepository productBrandRepository;
    private final ProductBrandDtoMapper dtoMapper;
    private final ProductBrandEntityMapper entityMapper;

    /**
     * Crea una nueva marca de producto
     * @param createDto DTO con los datos de la marca a crear
     * @return DTO con los datos de la marca creada
     */
    public ProductBrandResponseDto create(ProductBrandCreateDto createDto) {
        log.info("Creating new ProductBrand with name: {}", createDto.getName());

        // Validar que no exista una marca con el mismo nombre
        if (productBrandRepository.existsByName(createDto.getName())) {
            throw new IllegalArgumentException("ProductBrand with name '" + createDto.getName() + "' already exists");
        }

        // DTO -> Model -> Entity -> Save -> Entity -> Model -> DTO
        ProductBrandModel model = dtoMapper.toModel(createDto);
        ProductBrandEntity entity = entityMapper.toEntity(model);
        ProductBrandEntity savedEntity = productBrandRepository.save(entity);
        ProductBrandModel savedModel = entityMapper.toModel(savedEntity);
        ProductBrandResponseDto response = dtoMapper.toResponseDto(savedModel);

        log.info("ProductBrand created successfully with id: {}", response.getId());
        return response;
    }

    /**
     * Obtiene todas las marcas de producto
     * @return Lista de DTOs con todas las marcas
     */
    @Transactional(readOnly = true)
    public List<ProductBrandResponseDto> findAll() {
        log.info("Fetching all ProductBrands");

        List<ProductBrandEntity> entities = productBrandRepository.findAll();
        List<ProductBrandResponseDto> response = entities.stream()
                .map(entityMapper::toModel)
                .map(dtoMapper::toResponseDto)
                .collect(Collectors.toList());

        log.info("Found {} ProductBrands", response.size());
        return response;
    }

    /**
     * Busca una marca de producto por su ID
     * @param id ID de la marca a buscar
     * @return DTO con los datos de la marca encontrada
     */
    @Transactional(readOnly = true)
    public ProductBrandResponseDto findById(UUID id) {
        log.info("Fetching ProductBrand with id: {}", id);

        ProductBrandEntity entity = productBrandRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ProductBrand not found with id: {}", id);
                    return new RuntimeException("ProductBrand not found with id: " + id);
                });

        ProductBrandModel model = entityMapper.toModel(entity);
        ProductBrandResponseDto response = dtoMapper.toResponseDto(model);

        log.info("ProductBrand found: {}", response.getName());
        return response;
    }

    /**
     * Actualiza una marca de producto existente
     * @param id ID de la marca a actualizar
     * @param updateDto DTO con los nuevos datos
     * @return DTO con los datos actualizados
     */
    public ProductBrandResponseDto update(UUID id, ProductBrandUpdateDto dto) {
        log.info("Updating ProductBrand with id: {}", id);

        // 1. Buscar la marca existente
        ProductBrandEntity entity = productBrandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBrand not found with id: " + id));

        // 2. Verificar nombre duplicado (excepto la misma marca)
        productBrandRepository.findByName(dto.getName())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> {
                    throw new IllegalArgumentException("ProductBrand with name '" + dto.getName() + "' already exists");
                });

        // 3. Actualizar campos permitidos
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setEnabled(dto.getEnabled());
        entity.setUpdatedBy(dto.getUpdatedBy());     // ← obligatorio en el DTO
        entity.setUpdatedAt(LocalDateTime.now());

        // 4. Guardar y devolver
        ProductBrandEntity saved = productBrandRepository.save(entity);
        return dtoMapper.toResponseDto(entityMapper.toModel(saved));
    }

    /**
     * Elimina una marca de producto
     * @param id ID de la marca a eliminar
     */
    public void delete(UUID id) {
        log.info("Deleting ProductBrand with id: {}", id);

        if (!productBrandRepository.existsById(id)) {
            log.error("ProductBrand not found with id: {}", id);
            throw new RuntimeException("ProductBrand not found with id: " + id);
        }

        productBrandRepository.deleteById(id);
        log.info("ProductBrand deleted successfully with id: {}", id);
    }

    /**
     * Busca una marca de producto por su nombre
     * @param name Nombre de la marca a buscar
     * @return DTO con los datos de la marca encontrada
     */
    @Transactional(readOnly = true)
    public ProductBrandResponseDto findByName(String name) {
        log.info("Fetching ProductBrand with name: {}", name);

        ProductBrandEntity entity = productBrandRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("ProductBrand not found with name: {}", name);
                    return new RuntimeException("ProductBrand not found with name: " + name);
                });

        ProductBrandModel model = entityMapper.toModel(entity);
        ProductBrandResponseDto response = dtoMapper.toResponseDto(model);

        log.info("ProductBrand found with name: {}", name);
        return response;
    }

    /**
     * Verifica si existe una marca con el nombre dado
     * @param name Nombre a verificar
     * @return true si existe, false si no
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        log.debug("Checking if ProductBrand exists with name: {}", name);
        return productBrandRepository.existsByName(name);
    }
}