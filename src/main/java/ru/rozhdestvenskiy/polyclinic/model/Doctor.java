package ru.rozhdestvenskiy.polyclinic.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tickets"})
@ToString(exclude = {"tickets"})
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;

    @OneToMany(mappedBy = "doctor")
    private List<Ticket> tickets = new ArrayList<>();

    public String getFIO() {
        return lastName + " " + firstName + " " + patronymic;
    }

}
