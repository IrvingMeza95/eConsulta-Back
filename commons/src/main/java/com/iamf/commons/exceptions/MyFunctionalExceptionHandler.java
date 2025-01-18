package com.iamf.commons.exceptions;

import java.util.Optional;
import java.util.function.Consumer;

public class MyFunctionalExceptionHandler {
    public static void handleException(Runnable runnable, Consumer<Exception> exceptionHandler) {
        Optional<Exception> exception = Optional.empty();
        try {
            runnable.run();
        } catch (Exception ex) {
            exception = Optional.of(ex);
        }
        exception.ifPresent(exceptionHandler);
    }
}
