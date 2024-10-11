package com.ticketservice.exceptions;

public class NoTicketsFoundException extends RuntimeException {
    public NoTicketsFoundException(String message) {
        super(message);
    }
}
