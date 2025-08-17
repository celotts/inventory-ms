package com.celotts.productservice.domain.port.input.category;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.domain.model.category.CategoryStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test para CategoryUseCase usando una implementación en memoria.
 * Nota: Esto no "cubre" la interfaz (no tiene bytecode ejecutable), pero sí
 * valida el contrato invocando todas las firmas del API.
 */
class CategoryUseCaseTest {

    private CategoryUseCase useCase;

    private UUID id1;
    private UUID id2;
    private CategoryModel c1;
    private CategoryModel c2;

    @BeforeEach
    void setUp() {
        useCase = new InMemoryCategoryUseCase();

        id1 = UUID.randomUUID();
        id2 = UUID.randomUUID();

        c1 = CategoryModel.builder()
                .id(id1)
                .name("Bebidas")
                .description("Todo tipo de bebidas")
                .active(true)
                .createdBy("u1")
                .updatedBy("u1")
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .deleted(false)
                .build();

        c2 = CategoryModel.builder()
                .id(id2)
                .name("Botanas")
                .description("Snacks y botanas")
                .active(false)
                .createdBy("u1")
                .updatedBy("u1")
                .createdAt(LocalDateTime.now().minusDays(3))
                .updatedAt(LocalDateTime.now().minusDays(2))
                .deleted(false)
                .build();

        useCase.save(c1);
        useCase.save(c2);
    }

    @Test
    void save_and_findById() {
        var found = useCase.findById(id1);
        assertTrue(found.isPresent());
        assertEquals("Bebidas", found.get().getName());
    }

    @Test
    void findByName() {
        var found = useCase.findByName("Botanas");
        assertTrue(found.isPresent());
        assertEquals(id2, found.get().getId());
    }

    @Test
    void findAll_and_findAllById() {
        var all = useCase.findAll();
        assertEquals(2, all.size());

        var subset = useCase.findAllById(List.of(id1));
        assertEquals(1, subset.size());
        assertEquals(id1, subset.get(0).getId());
    }

    @Test
    void findByNameContaining_and_searchByNameOrDescription() {
        var byName = useCase.findByNameContaining("bo");
        assertEquals(1, byName.size());
        assertEquals("Botanas", byName.get(0).getName());

        var search = useCase.searchByNameOrDescription("beb", 10);
        assertEquals(1, search.size());
        assertEquals("Bebidas", search.get(0).getName());
    }

    @Test
    void existsById_and_existsByName() {
        assertTrue(useCase.existsById(id1));
        assertFalse(useCase.existsById(UUID.randomUUID()));
        assertTrue(useCase.existsByName("Bebidas"));
        assertFalse(useCase.existsByName("Inexistente"));
    }

    @Test
    void pageable_queries() {
        Pageable p = PageRequest.of(0, 10, Sort.by("name").ascending());

        Page<CategoryModel> all = useCase.findAll(p);
        assertEquals(2, all.getTotalElements());

        Page<CategoryModel> byName = useCase.findByNameContaining("ta", p);
        assertEquals(1, byName.getTotalElements());
        assertEquals("Botanas", byName.getContent().get(0).getName());

        Page<CategoryModel> activeTrue = useCase.findByActive(true, p);
        assertEquals(1, activeTrue.getTotalElements());
        assertTrue(activeTrue.getContent().get(0).getActive());

        Page<CategoryModel> nameAndActive = useCase.findByNameContainingAndActive("bo", false, p);
        assertEquals(1, nameAndActive.getTotalElements());
        assertEquals("Botanas", nameAndActive.getContent().get(0).getName());

        Page<CategoryModel> allPaginated = useCase.findAllPaginated("bo", null, p);
        assertEquals(1, allPaginated.getTotalElements());
    }

    @Test
    void updateStatus_restore_permanentDelete_and_deleteById() {
        // updateStatus
        var updated = useCase.updateStatus(id2, true);
        assertTrue(updated.getActive());

        // restore (marcamos deleted para validar)
        var before = useCase.findById(id2).orElseThrow();
        before.setDeleted(true);
        useCase.save(before);
        var restored = useCase.restore(id2);
        assertFalse(restored.isDeleted());

        // permanentDelete (existe)
        useCase.permanentDelete(id1);
        assertFalse(useCase.existsById(id1));

        // deleteById (simple)
        useCase.deleteById(id2);
        assertFalse(useCase.existsById(id2));
    }

    @Test
    void statistics_and_countByActive() {
        // Usar una instancia LIMPIA solo para este test
        CategoryUseCase clean = new InMemoryCategoryUseCase();

        var a = CategoryModel.builder().id(UUID.randomUUID()).name("A").active(true).build();
        var b = CategoryModel.builder().id(UUID.randomUUID()).name("B").active(false).build();
        clean.save(a);
        clean.save(b);

        CategoryStats stats = clean.getCategoryStatistics();
        assertEquals(2, stats.getTotalCategories());
        assertEquals(1, stats.getActiveCategories());
        assertEquals(1, stats.getInactiveCategories());

        assertEquals(1, clean.countByActive(true));
        assertEquals(1, clean.countByActive(false));
    }

    // ————————————————————————————————————————————————————————————————
    // Implementación en memoria del contrato para pruebas de interacción
    // ————————————————————————————————————————————————————————————————
    static class InMemoryCategoryUseCase implements CategoryUseCase {
        private final Map<UUID, CategoryModel> store = new ConcurrentHashMap<>();

        @Override
        public CategoryModel save(CategoryModel category) {
            Objects.requireNonNull(category.getId(), "id required");
            store.put(category.getId(), clone(category));
            return clone(category);
        }

        @Override
        public Optional<CategoryModel> findById(UUID id) {
            return Optional.ofNullable(store.get(id)).map(this::clone);
        }

        @Override
        public Optional<CategoryModel> findByName(String name) {
            return store.values().stream()
                    .filter(c -> Objects.equals(c.getName(), name))
                    .findFirst().map(this::clone);
        }

        @Override
        public List<CategoryModel> findAll() {
            return store.values().stream().map(this::clone).toList();
        }

        @Override
        public List<CategoryModel> findAllById(List<UUID> ids) {
            return ids.stream().map(store::get).filter(Objects::nonNull).map(this::clone).toList();
        }

        @Override
        public List<CategoryModel> findByNameContaining(String name) {
            String n = name.toLowerCase(Locale.ROOT);
            return store.values().stream()
                    .filter(c -> c.getName() != null && c.getName().toLowerCase(Locale.ROOT).contains(n))
                    .map(this::clone).toList();
        }

        @Override
        public List<CategoryModel> searchByNameOrDescription(String query, int limit) {
            String q = query.toLowerCase(Locale.ROOT);
            return store.values().stream()
                    .filter(c -> (c.getName() != null && c.getName().toLowerCase(Locale.ROOT).contains(q)) ||
                            (c.getDescription() != null && c.getDescription().toLowerCase(Locale.ROOT).contains(q)))
                    .limit(limit)
                    .map(this::clone).toList();
        }

        @Override
        public void deleteById(UUID id) {
            store.remove(id);
        }

        @Override
        public boolean existsById(UUID id) {
            return store.containsKey(id);
        }

        @Override
        public boolean existsByName(String name) {
            return store.values().stream().anyMatch(c -> Objects.equals(c.getName(), name));
        }

        @Override
        public Page<CategoryModel> findAll(Pageable pageable) {
            return page(list(), pageable);
        }

        @Override
        public Page<CategoryModel> findByNameContaining(String name, Pageable pageable) {
            return page(findByNameContaining(name), pageable);
        }

        @Override
        public Page<CategoryModel> findByActive(Boolean active, Pageable pageable) {
            var filtered = list().stream()
                    .filter(c -> active == null || Objects.equals(c.getActive(), active))
                    .toList();
            return page(filtered, pageable);
        }

        @Override
        public Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
            var filtered = findByNameContaining(name).stream()
                    .filter(c -> active == null || Objects.equals(c.getActive(), active))
                    .toList();
            return page(filtered, pageable);
        }

        @Override
        public Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
            var stream = list().stream();
            if (name != null && !name.isBlank()) {
                String q = name.toLowerCase(Locale.ROOT);
                stream = stream.filter(c -> c.getName() != null && c.getName().toLowerCase(Locale.ROOT).contains(q));
            }
            if (active != null) {
                stream = stream.filter(c -> Objects.equals(c.getActive(), active));
            }
            return page(stream.toList(), pageable);
        }

        @Override
        public CategoryModel updateStatus(UUID id, Boolean active) {
            var cat = store.get(id);
            if (cat == null) throw new IllegalArgumentException("Category not found");
            cat.setActive(active);
            cat.setUpdatedAt(LocalDateTime.now());
            return clone(cat);
        }

        @Override
        public CategoryModel restore(UUID id) {
            var cat = store.get(id);
            if (cat == null) throw new IllegalArgumentException("Category not found");
            cat.setDeleted(false);
            cat.setUpdatedAt(LocalDateTime.now());
            return clone(cat);
        }

        @Override
        public void permanentDelete(UUID id) {
            if (!existsById(id)) throw new IllegalArgumentException("Category not found");
            store.remove(id);
        }

        @Override
        public CategoryStats getCategoryStatistics() {
            long total = store.size();
            long active = store.values().stream().filter(c -> Boolean.TRUE.equals(c.getActive())).count();
            long inactive = store.values().stream().filter(c -> Boolean.FALSE.equals(c.getActive())).count();
            return CategoryStats.builder()
                    .totalCategories(total)
                    .activeCategories(active)
                    .inactiveCategories(inactive)
                    .build();
        }

        @Override
        public long countByActive(boolean active) {
            return store.values().stream().filter(c -> Objects.equals(c.getActive(), active)).count();
        }

        // Helpers
        private List<CategoryModel> list() {
            return store.values().stream().map(this::clone).toList();
        }

        private Page<CategoryModel> page(List<CategoryModel> src, Pageable pageable) {
            // Ordenamiento básico por 'name' si aplica
            List<CategoryModel> sorted = new ArrayList<>(src);
            if (pageable.getSort().isSorted()) {
                for (Sort.Order o : pageable.getSort()) {
                    Comparator<CategoryModel> cmp = Comparator.comparing(
                            c -> Objects.toString(safeGet(c, o.getProperty()), "")
                    );
                    if (o.getDirection() == Sort.Direction.DESC) cmp = cmp.reversed();
                    sorted.sort(cmp);
                    break;
                }
            }
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), sorted.size());
            List<CategoryModel> page = start >= sorted.size() ? List.of() : sorted.subList(start, end);
            return new PageImpl<>(page, pageable, sorted.size());
        }

        private Object safeGet(CategoryModel c, String prop) {
            return switch (prop) {
                case "name" -> c.getName();
                case "active" -> c.getActive();
                case "createdAt" -> c.getCreatedAt();
                case "updatedAt" -> c.getUpdatedAt();
                default -> null;
            };
        }

        private CategoryModel clone(CategoryModel c) {
            return CategoryModel.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .description(c.getDescription())
                    .active(c.getActive())
                    .createdBy(c.getCreatedBy())
                    .updatedBy(c.getUpdatedBy())
                    .createdAt(c.getCreatedAt())
                    .updatedAt(c.getUpdatedAt())
                    .deleted(c.isDeleted())
                    .build();
        }
    }
}