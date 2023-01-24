package ru.rozhdestvenskiy.polyclinic.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tickets"})
@ToString(exclude = {"tickets"})
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private LocalDate dateBirthday;

    @OneToMany(mappedBy = "patient")
    private List<Ticket> tickets = new ArrayList<>();

    public String getFIO(){
        return lastName + " " + firstName + " " + patronymic ;
    }
}
