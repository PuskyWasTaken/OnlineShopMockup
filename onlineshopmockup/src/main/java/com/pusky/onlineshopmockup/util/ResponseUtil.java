package com.pusky.onlineshopmockup.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface ResponseUtil {
    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrap(maybeResponse, null, HttpStatus.NOT_FOUND, "");
    }

    static <X> ResponseEntity<X> wrapOrInvalid(Optional<X> maybeResponse) {
        return wrap(maybeResponse, null, HttpStatus.UNPROCESSABLE_ENTITY, "The requested product is invalid!");
        /*
        *           Why use UNPROCESSABLE_ENTITY.
        *           https://tools.ietf.org/html/rfc4918#section-11.2
        *
        */
    }


    static <X> ResponseEntity<X> wrap(final Optional<X> maybeResponse, final HttpHeaders header, final HttpStatus httpStatus, final String message) {
        return maybeResponse.map((response) -> (
                ResponseEntity.ok().headers(header)).body(response)).orElseThrow(
                () -> new ResponseStatusException(httpStatus, message));
    }
}