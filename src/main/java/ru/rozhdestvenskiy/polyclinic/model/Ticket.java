package ru.rozhdestvenskiy.polyclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Patient patient;
    private LocalDateTime timeAppointment;
}
