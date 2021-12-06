package com.swecor.aerotek.rest.exceptions;

import java.util.function.Supplier;

public class LogicalException extends RuntimeException implements Supplier<LogicalException> {
    public LogicalException(String message){
        super(message);
    }

    @Override
    public LogicalException get() {
        return new LogicalException(getMessage());
    }
}
