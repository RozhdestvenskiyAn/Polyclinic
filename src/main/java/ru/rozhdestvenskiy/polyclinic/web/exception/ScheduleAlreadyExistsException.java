package ru.rozhdestvenskiy.polyclinic.web.exception;

public class ScheduleAlreadyExistsException extends RuntimeException {

    public ScheduleAlreadyExistsException(String message) {
        super(message);
    }
}
