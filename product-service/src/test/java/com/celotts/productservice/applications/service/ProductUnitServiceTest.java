/*package com.celotts.productservice.applications.service;

import com.celotts.productservice.applications.usecase.ProductUnitUseCaseImpl;
import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUnitServiceTest {

    @Mock
    private ProductUnitRepositoryPort repo;

    // SUT
    private ProductUnitUseCaseImpl useCase;

    private UUID id;
    private ProductUnitModel model;

    @BeforeEach
    void setUp() {
        useCase = new ProductUnitUseCaseImpl(repo);

        id = UUID.randomUUID();
        model = ProductUnitModel.builder()
                .id(id)
                .code("KG")
                .name("Kilogramo")
                .description("Unidad de peso")
                .symbol("kg")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .build();
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void save_shouldCallRepoAndReturnModel() {
        when(repo.save(model)).thenReturn(model);

        ProductUnitModel result = useCase.save(model);

        assertNotNull(result);
        assertEquals("KG", result.getCode());
        verify(repo).save(model);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findAll_shouldReturnListOfModels() {
        when(repo.findAll()).thenReturn(List.of(model));

        List<ProductUnitModel> result = useCase.findAll();

        assertEquals(1, result.size());
        assertEquals("KG", result.get(0).getCode());
        verify(repo).findAll();
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findById_shouldReturnModel() {
        when(repo.findById(id)).thenReturn(Optional.of(model));

        Optional<ProductUnitModel> result = useCase.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(repo).findById(id);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findById_shouldReturnEmpty_whenNotFound() {
        when(repo.findById(id)).thenReturn(Optional.empty());

        Optional<ProductUnitModel> result = useCase.findById(id);

        assertTrue(result.isEmpty());
        verify(repo).findById(id);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void update_shouldMergeAndSave() {
        // existing in repo
        ProductUnitModel existing = model.toBuilder().name("Old").build();
        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any(ProductUnitModel.class))).thenAnswer(inv -> inv.getArgument(0));

        // changes
        ProductUnitModel changes = ProductUnitModel.builder()
                .name("New")
                .description("Peso actualizado")
                .symbol("kg")
                .enabled(false)
                .updatedBy("admin")
                .build();

        ProductUnitModel updated = useCase.update(id, changes);

        assertEquals("New", updated.getName());
        assertEquals("Peso actualizado", updated.getDescription());
        assertFalse(updated.getEnabled());

        ArgumentCaptor<ProductUnitModel> cap = ArgumentCaptor.forClass(ProductUnitModel.class);
        verify(repo).save(cap.capture());
        ProductUnitModel sent = cap.getValue();
        assertEquals("New", sent.getName());
        assertNotNull(sent.getUpdatedAt());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void deleteById_shouldDelegateToRepo() {
        useCase.deleteById(id);
        verify(repo).deleteById(id);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void existsById_shouldDelegateToRepo() {
        when(repo.existsById(id)).thenReturn(true);
        assertTrue(useCase.existsById(id));
        verify(repo).existsById(id);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void existsByCode_shouldDelegateToRepo() {
        when(repo.existsByCode("KG")).thenReturn(true);
        assertTrue(useCase.existsByCode("KG"));
        verify(repo).existsByCode("KG");
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findNameByCode_shouldDelegateToRepo() {
        when(repo.findNameByCode("KG")).thenReturn(Optional.of("Kilogramo"));
        assertEquals(Optional.of("Kilogramo"), useCase.findNameByCode("KG"));
        verify(repo).findNameByCode("KG");
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findAllCodes_shouldDelegateToRepo() {
        when(repo.findAllCodes()).thenReturn(List.of("KG", "G"));
        assertEquals(List.of("KG", "G"), useCase.findAllCodes());
        verify(repo).findAllCodes();
    }
}*/