package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productserviceOld.domain.exception.ResourceAlreadyExistsException;
import com.celotts.productserviceOld.domain.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/_test/exceptions")
class ExceptionTestController {

    @GetMapping("/not-found/{id}")
    public void notFound(@PathVariable UUID id) {
        throw new ResourceNotFoundException("Product", id);
    }

    @GetMapping("/already-exists")
    public void alreadyExists() {
        throw new ResourceAlreadyExistsException("Product", "Ya existe un producto con el código");
    }
}