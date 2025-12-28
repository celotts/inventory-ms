package com.celotts.purchaseservice.infrastructure.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;

public final class ApiErrors {
    private ApiErrors() {}

    public static ProblemDetail problem(HttpStatus status, ErrorCode code, String title, String detail, Map<String,Object> extras) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create("urn:celotts:error:" + code.name().toLowerCase()));
        pd.setProperty("errorCode", code.name());
        pd.setProperty("timestamp", OffsetDateTime.now());
        if (extras != null) extras.forEach(pd::setProperty);
        return pd;
    }
}
