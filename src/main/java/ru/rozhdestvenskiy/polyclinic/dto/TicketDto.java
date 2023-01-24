package ru.rozhdestvenskiy.polyclinic.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketDto {
    private String doctor;
    private String patient;
    private String time;
}
