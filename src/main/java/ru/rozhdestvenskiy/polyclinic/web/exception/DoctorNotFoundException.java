package ru.rozhdestvenskiy.polyclinic.web.exception;

public class DoctorNotFoundException extends NotFoundException {
    public DoctorNotFoundException(String message) {
        super(message);
    }
}
