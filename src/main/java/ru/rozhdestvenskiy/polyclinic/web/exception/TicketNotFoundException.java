package ru.rozhdestvenskiy.polyclinic.web.exception;

public class TicketNotFoundException extends NotFoundException {
    public TicketNotFoundException(String message) {
        super(message);
    }
}
