package com.celotts.productservice.applications.service;

import com.celotts.productserviceOld.applications.service.ProductBrandService;
import com.celotts.productserviceOld.domain.model.ProductBrandModel;
import com.celotts.productserviceOld.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductBrandServiceTest {

    @Mock private ProductBrandUseCase productBrandUseCase;
    @Mock private ProductBrandDtoMapper dtoMapper;

    @InjectMocks
    private ProductBrandService service;

    private ProductBrandCreateDto createDto(String name) {
        ProductBrandCreateDto dto = new ProductBrandCreateDto();
        dto.setName(name);
        dto.setDescription("desc");
        dto.setEnabled(true);
        dto.setCreatedBy("tester");
        return dto;
    }

    private ProductBrandUpdateDto updateDto(String name) {
        ProductBrandUpdateDto dto = new ProductBrandUpdateDto();
        dto.setName(name);
        dto.setDescription("new-desc");
        dto.setEnabled(false);
        dto.setUpdatedBy("upd");
        return dto;
    }

    private ProductBrandModel model(UUID id, String name) {
        ProductBrandModel m = new ProductBrandModel();
        m.setId(id);
        m.setName(name);
        m.setEnabled(true);
        return m;
    }

    // -------- create --------
    @Test
    @DisplayName("create: guarda y retorna response cuando el nombre no existe")
    void create_ok() {
        ProductBrandCreateDto dto = createDto("BrandX");

        when(productBrandUseCase.existsByName("BrandX")).thenReturn(false);

        ProductBrandModel mapped = new ProductBrandModel();
        when(dtoMapper.toModel(dto)).thenReturn(mapped);

        ProductBrandModel saved = model(UUID.randomUUID(), "BrandX");
        when(productBrandUseCase.save(mapped)).thenReturn(saved);

        ProductBrandResponseDto response = mock(ProductBrandResponseDto.class);
        when(dtoMapper.toResponseDto(saved)).thenReturn(response);

        ProductBrandResponseDto result = service.create(dto);

        assertSame(response, result);

        // capturamos lo que se envió a save para verificar createdBy/createdAt seteados
        ArgumentCaptor<ProductBrandModel> cap = ArgumentCaptor.forClass(ProductBrandModel.class);
        verify(productBrandUseCase).save(cap.capture());
        ProductBrandModel sent = cap.getValue();
        assertEquals("tester", sent.getCreatedBy());
        assertNotNull(sent.getCreatedAt(), "createdAt debe ser seteado");
    }

    @Test
    @DisplayName("create: lanza IllegalArgumentException cuando el nombre ya existe")
    void create_nameExists_throws() {
        ProductBrandCreateDto dto = createDto("BrandX");
        when(productBrandUseCase.existsByName("BrandX")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        verify(productBrandUseCase, never()).save(any());
    }

    // -------- findAll / findById --------
    @Test
    void findAll_mapsList() {
        ProductBrandModel m1 = model(UUID.randomUUID(), "A");
        ProductBrandModel m2 = model(UUID.randomUUID(), "B");
        when(productBrandUseCase.findAll()).thenReturn(List.of(m1, m2));
        when(dtoMapper.toResponseDto(m1)).thenReturn(mock(ProductBrandResponseDto.class));
        when(dtoMapper.toResponseDto(m2)).thenReturn(mock(ProductBrandResponseDto.class));

        List<ProductBrandResponseDto> list = service.findAll();
        assertEquals(2, list.size());
    }

    @Test
    void findById_found_returnsDto() {
        UUID id = UUID.randomUUID();
        ProductBrandModel m = model(id, "C");
        when(productBrandUseCase.findById(id)).thenReturn(Optional.of(m));
        ProductBrandResponseDto resp = mock(ProductBrandResponseDto.class);
        when(dtoMapper.toResponseDto(m)).thenReturn(resp);

        assertSame(resp, service.findById(id));
    }

    @Test
    void findById_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(productBrandUseCase.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.findById(id));
    }

    // -------- update --------
    @Test
    void update_idNotFound_throws() {
        UUID id = UUID.randomUUID();
        when(productBrandUseCase.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.update(id, updateDto("X")));
    }

    @Test
    void update_duplicateName_throws() {
        UUID id = UUID.randomUUID();
        ProductBrandModel existing = model(id, "Old");
        when(productBrandUseCase.findById(id)).thenReturn(Optional.of(existing));

        // devuelve otra marca distinta con el mismo nombre nuevo
        ProductBrandModel other = model(UUID.randomUUID(), "New");
        when(productBrandUseCase.findByName("New")).thenReturn(Optional.of(other));

        assertThrows(IllegalArgumentException.class, () -> service.update(id, updateDto("New")));
        verify(productBrandUseCase, never()).save(any());
    }

    @Test
    void update_ok_savesAndReturnsDto() {
        UUID id = UUID.randomUUID();
        ProductBrandModel existing = model(id, "Old");
        when(productBrandUseCase.findById(id)).thenReturn(Optional.of(existing));
        when(productBrandUseCase.findByName("New")).thenReturn(Optional.empty());

        ProductBrandModel saved = model(id, "New");
        when(productBrandUseCase.save(existing)).thenReturn(saved);
        ProductBrandResponseDto resp = mock(ProductBrandResponseDto.class);
        when(dtoMapper.toResponseDto(saved)).thenReturn(resp);

        ProductBrandResponseDto result = service.update(id, updateDto("New"));

        assertSame(resp, result);
        // se actualizaron campos en existing antes de salvar
        assertEquals("New", existing.getName());
        assertEquals("new-desc", existing.getDescription());
        assertFalse(existing.getEnabled());
        assertEquals("upd", existing.getUpdatedBy());
        assertNotNull(existing.getUpdatedAt());
        verify(productBrandUseCase).save(existing);
    }

    // -------- delete --------
    @Test
    void delete_notExists_throws() {
        UUID id = UUID.randomUUID();
        when(productBrandUseCase.existsById(id)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.delete(id));
        verify(productBrandUseCase, never()).deleteById(any());
    }

    @Test
    void delete_ok_callsPort() {
        UUID id = UUID.randomUUID();
        when(productBrandUseCase.existsById(id)).thenReturn(true);
        service.delete(id);
        verify(productBrandUseCase).deleteById(id);
    }

    // -------- findByName / exists --------
    @Test
    void findByName_found_returnsDto() {
        ProductBrandModel m = model(UUID.randomUUID(), "Nike");
        when(productBrandUseCase.findByName("Nike")).thenReturn(Optional.of(m));
        ProductBrandResponseDto resp = mock(ProductBrandResponseDto.class);
        when(dtoMapper.toResponseDto(m)).thenReturn(resp);

        assertSame(resp, service.findByName("Nike"));
    }

    @Test
    void findByName_notFound_throws() {
        when(productBrandUseCase.findByName("Nope")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.findByName("Nope"));
    }

    @Test
    void existsByName_delegates() {
        when(productBrandUseCase.existsByName("ACME")).thenReturn(true);
        assertTrue(service.existsByName("ACME"));
    }

    @Test
    void existsById_delegates() {
        UUID id = UUID.randomUUID();
        when(productBrandUseCase.existsById(id)).thenReturn(true);
        assertTrue(service.existsById(id));
    }

    @Test
    void findNameById_delegates() {
        UUID id = UUID.randomUUID();
        when(productBrandUseCase.findNameById(id)).thenReturn(Optional.of("Brand"));
        assertEquals(Optional.of("Brand"), service.findNameById(id));
    }

    @Test
    void findAllIds_delegates() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        when(productBrandUseCase.findAllIds()).thenReturn(List.of(a, b));
        assertEquals(List.of(a, b), service.findAllIds());
    }

    // -------- enable / disable --------
    @Test
    void enableBrand_mapsResponse() {
        UUID id = UUID.randomUUID();
        ProductBrandModel enabled = model(id, "Z");
        when(productBrandUseCase.enableBrand(id)).thenReturn(enabled);
        ProductBrandResponseDto resp = mock(ProductBrandResponseDto.class);
        when(dtoMapper.toResponseDto(enabled)).thenReturn(resp);

        assertSame(resp, service.enableBrand(id));
    }

    @Test
    void disableBrand_mapsResponse() {
        UUID id = UUID.randomUUID();
        ProductBrandModel disabled = model(id, "Z");
        when(productBrandUseCase.disableBrand(id)).thenReturn(disabled);
        ProductBrandResponseDto resp = mock(ProductBrandResponseDto.class);
        when(dtoMapper.toResponseDto(disabled)).thenReturn(resp);

        assertSame(resp, service.disableBrand(id));
    }
}