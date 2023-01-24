package ru.rozhdestvenskiy.polyclinic.web.exception;

public class PatientNotFoundException extends NotFoundException{
    public PatientNotFoundException(String message) {
        super(message);
    }
}
