package com.celotts.taxservice.application.usecase.tax;

import com.celotts.taxservice.domain.model.TaxModel;
import com.celotts.taxservice.domain.port.input.TaxUseCase;
import com.celotts.taxservice.domain.port.output.TaxRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaxUseCaseImpl implements TaxUseCase {

    private final TaxRepositoryPort taxRepository;
    private final MessageSource messageSource;

    // Método de utilidad para simplificar la obtención de mensajes
    private String getLocalizedMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    // Método de utilidad para generar el mensaje de error de Recurso No Encontrado
    private IllegalArgumentException resourceNotFoundException(Object identifier, boolean byId) {
        String resourceName = getLocalizedMessage("log.tax");
        String connectorKey = byId ? "log.with.id" : "log.with.name";

        String finalArgument = resourceName + getLocalizedMessage(connectorKey) + identifier.toString();

        String errorMessage = getLocalizedMessage(
                "error.resource.notfound",
                new Object[]{finalArgument}
        );
        return new IllegalArgumentException(errorMessage);
    }

    // Método de utilidad para generar un log de advertencia de no encontrado
    private String getNotFoundWarnMessage(Object identifier, boolean byId) {
        // En este caso, reutilizaremos la clave de log.tax.notfound
        return getLocalizedMessage("log.tax.notfound", identifier.toString());
    }

    // --- MÉTODOS DE LA INTERFAZ ---

    @Override
    @Transactional
    public TaxModel save(TaxModel tax) {
        // Log de INFO (Ya estaba bien)
        log.info(getLocalizedMessage("log.tax.saving", tax.getCode()));

        if (tax.getId() == null) {
            // Validar que el código no exista
            if (taxRepository.existsByCode(tax.getCode())) {
                String msg = getLocalizedMessage("tax.code.exists", tax.getCode());
                String warnMsg = getLocalizedMessage("log.tax.code.exists", tax.getCode());

                log.warn(warnMsg);
                throw new IllegalArgumentException(msg);
            }
        }

        return taxRepository.save(tax);
    }

    @Override
    public Optional<TaxModel> findById(UUID id) {
        // ANTES: log.debug("Finding tax by id: {}", id);
        log.debug(getLocalizedMessage("log.tax.finding.byid", id));

        return taxRepository.findById(id)
                .or(() -> {
                    // ANTES: log.warn("Tax not found: {}", id);
                    log.warn(getNotFoundWarnMessage(id, true));

                    // Usando el nuevo método de utilidad de excepción
                    throw resourceNotFoundException(id, true);
                });
    }

    @Override
    public Optional<TaxModel> findByName(String name) {
        // ANTES: log.debug("Finding tax by name: {}", name);
        log.debug(getLocalizedMessage("log.tax.finding.byname", name));

        return taxRepository.findByName(name)
                .or(() -> {
                    // ANTES: log.warn("Tax not found by name: {}", name);
                    log.warn(getNotFoundWarnMessage(name, false));

                    // Usando el nuevo método de utilidad de excepción
                    throw resourceNotFoundException(name, false);
                });
    }

    @Override
    public List<TaxModel> findAll() {
        // ANTES: log.debug("Finding all taxes");
        log.debug(getLocalizedMessage("log.tax.finding.all"));
        return taxRepository.findAll();
    }

    @Override
    public List<TaxModel> findAllById(List<UUID> ids) {
        // ANTES: log.debug("Finding taxes by ids: {}", ids);
        log.debug(getLocalizedMessage("log.tax.finding.byids", ids));

        List<TaxModel> models = taxRepository.findById(ids);

        if (models.isEmpty()) {
            // ANTES: log.warn("No taxes found for ids: {}", ids);
            log.warn(getNotFoundWarnMessage(ids, true));

            // Usando el nuevo método de utilidad de excepción
            throw resourceNotFoundException(ids.toString(), true);
        }
        return models;
    }

    @Override
    public List<TaxModel> finjdByNameContaining(String name) {
        // ANTES: log.debug("Finding taxes by name containing: {}", name);
        log.debug(getLocalizedMessage("log.tax.finding.byname.containing", name));

        List<TaxModel> models = taxRepository.findByNameContaining(name);

        if (models.isEmpty()) {
            // ANTES: log.warn("No taxes found containing name: {}", name);
            log.warn(getNotFoundWarnMessage(name, false));

            // Usando el nuevo método de utilidad de excepción
            throw resourceNotFoundException(name, false);
        }
        return models;
    }

    @Override
    public List<TaxModel> searchByNameOrDescriptionm(String query, int limit) {
        // ANTES: log.debug("Searching taxes by name or code: {}", query);
        log.debug(getLocalizedMessage("log.tax.searching", query));
        return taxRepository.findByNameOrDescription(query, limit);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        // Log de INFO (Ya estaba bien)
        log.info(getLocalizedMessage("log.tax.deleting", id));

        if (!taxRepository.existsById(id)) {
            // ANTES: log.warn("Tax not found: {}", id);
            log.warn(getNotFoundWarnMessage(id, true));

            // Usando el nuevo método de utilidad de excepción
            throw resourceNotFoundException(id, true);
        }

        taxRepository.deleteById(id);
    }

    @Override
    public boolean existById(UUID id) {
        // ANTES: log.debug("log.tax.checking.exists.byid: {}", id); // Ya usaba la clave
        log.debug(getLocalizedMessage("log.tax.checking.exists.byid", id));
        return taxRepository.existsById(id);
    }

    @Override
    public boolean existByName(String name) {
        // ANTES: log.debug("Checking if tax exists by name: {}", name);
        log.debug(getLocalizedMessage("log.tax.checking.exists.byname", name));
        return taxRepository.existsByName(name);
    }

    @Override
    public Page<TaxModel> findAll(Pageable pageable) {
        // ANTES: log.debug("Finding all taxes with pagination");
        log.debug(getLocalizedMessage("log.tax.finding.all.paginated"));
        return taxRepository.findAll(pageable);
    }

    @Override
    public Page<TaxModel> findByNameContaining(String name, Pageable pageable) {
        // ANTES: log.debug("Finding taxes by name containing: {}", name);
        log.debug(getLocalizedMessage("log.tax.finding.byname.paginated", name));
        return taxRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<TaxModel> findByActive(Boolean active, Pageable pageable) {
        // ANTES: log.debug("Finding taxes by active status: {}", active);
        log.debug(getLocalizedMessage("log.tax.finding.byactive", active));
        return taxRepository.findByActive(active, pageable);
    }

    @Override
    public Page<TaxModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        // ANTES: log.debug("Finding taxes by name and active status");
        log.debug(getLocalizedMessage("log.tax.finding.byname.andactive"));
        return taxRepository.findByNameContainingAndActive(name, active, pageable);
    }

    @Override
    public Page<TaxModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        // ANTES: log.debug("Finding all taxes paginated with name: {} and active: {}", name, active);
        log.debug(getLocalizedMessage("log.tax.finding.paginated.filtered", name, active));
        return taxRepository.findAllPaginated(name, active, pageable);
    }

    @Override
    @Transactional
    public TaxModel create(TaxModel model) {
        // Log de INFO (Ya estaba bien)
        log.info(getLocalizedMessage("log.tax.creating", model.getCode()));

        // Validaciones con mensajes i18n (Se mantienen igual, ya estaban bien)
        if (taxRepository.existsByCode(model.getCode())) {
            String msg = getLocalizedMessage("tax.code.exists", model.getCode());
            String warnMsg = getLocalizedMessage("log.tax.code.exists", model.getCode());

            log.warn(warnMsg);
            throw new IllegalArgumentException(msg);
        }

        // ... (resto de la lógica) ...

        return taxRepository.save(model);
    }
}