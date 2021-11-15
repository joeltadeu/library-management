package com.bookservice.commons.validation;

import com.bookservice.commons.exception.DataNotFoundException;
import com.bookservice.commons.exception.BadRequestException;

import java.util.Optional;

public class RestPreConditions {
    public static <T> T checkNotNull(final Optional<T> reference) {
        return checkNotNull(reference, null);
    }

    public static <T> T checkNotNull(final Optional<T> reference, final String message) {
        if (!reference.isPresent()) {
            throw new DataNotFoundException(message);
        }
        return (T) reference.get();
    }

    public static <T> T checkRequestElementNotNull(final T reference) {
        return checkRequestElementNotNull(reference, null);
    }

    public static <T> T checkRequestElementNotNull(final T reference, final String message) {
        if (reference == null) {
            throw new BadRequestException(message);
        }
        return reference;
    }

    public static void checkState(final boolean expression) {
        checkState(expression, null);
    }

    public static void checkState(final boolean expression, final String message, Object... args) {
        if (expression) {
            throw new BadRequestException(String.format(message, args));
        }
    }

    public static void checkState(final boolean expression, final String message) {
        if (expression) {
            throw new BadRequestException(message);
        }
    }

    public static <T> T checkNotNull(final Optional<T> reference, String message, Object... args) {
        if (!reference.isPresent()) {
            throw new DataNotFoundException(String.format(message, args));
        }
        return (T) reference.get();
    }
}
