package com.celotts.taxservice.application.usecase.tax;

import com.celotts.taxservice.domain.exception.ResourceNotFoundException;
import com.celotts.taxservice.domain.model.tax.TaxModel;
import com.celotts.taxservice.domain.port.input.tax.TaxUseCase;
import com.celotts.taxservice.domain.port.output.tax.TaxRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaxUseCaseImpl implements TaxUseCase {

    private final TaxRepositoryPort taxRepository;
    private final MessageSource messageSource;

    private String getLocalizedMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    public TaxModel save(TaxModel tax) {
        log.info(getLocalizedMessage("log.tax.saving", tax.getCode()));
        return taxRepository.save(tax);
    }

    @Override
    public TaxModel findById(UUID id) {
        log.debug(getLocalizedMessage("log.tax.finding.byid", id));
        return taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getLocalizedMessage("tax.id.not-found")));
    }

    @Override
    public TaxModel findByName(String name) {
        log.debug(getLocalizedMessage("log.tax.finding.byname", name));
        return taxRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(getLocalizedMessage("error.resource.notfound", name)));
    }

    @Override
    public List<TaxModel> findAll() {
        log.debug(getLocalizedMessage("log.tax.finding.all"));
        return taxRepository.findAll();
    }

    @Override
    public List<TaxModel> findAllById(List<UUID> ids) {
        log.debug(getLocalizedMessage("log.tax.finding.byids", ids));
        return taxRepository.findAllById(ids); // Corregido
    }

    @Override
    public List<TaxModel> findByNameContaining(String name) {
        log.debug(getLocalizedMessage("log.tax.finding.byname.containing", name));
        return taxRepository.findByNameContaining(name);
    }

    @Override
    public List<TaxModel> findByDescriptionContaining(String name) {
        log.debug(getLocalizedMessage("log.tax.finding.bydescription.containing", name));
        return taxRepository.findByDescriptionContaining(name);
    }

    @Override
    public List<TaxModel> searchByNameOrDescription(String query, int limit) {
        log.debug(getLocalizedMessage("log.tax.searching", query));
        return taxRepository.findByNameOrDescription(query, limit);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        log.info(getLocalizedMessage("log.tax.deleting", id));
        if (!taxRepository.existsById(id)) {
            throw new ResourceNotFoundException(getLocalizedMessage("tax.id.not-found"));
        }
        taxRepository.deleteById(id);
    }

    @Override
    public boolean existById(UUID id) {
        log.debug(getLocalizedMessage("log.tax.checking.exists.byid", id));
        return taxRepository.existsById(id);
    }

    @Override
    public boolean existByName(String name) {
        log.debug(getLocalizedMessage("log.tax.checking.exists.byname", name));
        return taxRepository.existsByName(name);
    }

    @Override
    public Page<TaxModel> findAll(Pageable pageable) {
        log.debug(getLocalizedMessage("log.tax.finding.all.paginated"));
        return taxRepository.findAll(pageable);
    }

    @Override
    public Page<TaxModel> findByNameContaining(String name, Pageable pageable) {
        log.debug(getLocalizedMessage("log.tax.finding.byname.paginated", name));
        return taxRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<TaxModel> findByActive(Boolean active, Pageable pageable) {
        log.debug(getLocalizedMessage("log.tax.finding.byactive", active));
        return taxRepository.findByActive(active, pageable);
    }

    @Override
    public Page<TaxModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        log.debug(getLocalizedMessage("log.tax.finding.byname.andactive"));
        return taxRepository.findByNameContainingAndActive(name, active, pageable);
    }

    @Override
    public Page<TaxModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        log.debug(getLocalizedMessage("log.tax.finding.paginated.filtered", name, active));
        return taxRepository.findAllPaginated(name, active, pageable);
    }

    @Override
    @Transactional
    public TaxModel create(TaxModel model) {
        log.info(getLocalizedMessage("log.tax.creating", model.getCode()));
        if (taxRepository.existsByCode(model.getCode())) {
            throw new IllegalArgumentException(getLocalizedMessage("tax.code.exists", model.getCode()));
        }
        return taxRepository.save(model);
    }
}
