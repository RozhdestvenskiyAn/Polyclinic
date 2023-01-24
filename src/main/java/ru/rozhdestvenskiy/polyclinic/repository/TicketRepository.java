package ru.rozhdestvenskiy.polyclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rozhdestvenskiy.polyclinic.model.Patient;
import ru.rozhdestvenskiy.polyclinic.model.Ticket;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "select * from ticket t " +
                   "where t.doctor_id = :doctorId and date(t.time_appointment) = :date and t.patient_id is null ",
            nativeQuery = true)
    List<Ticket> findAllFreeTicketsBy(Long doctorId, LocalDate date);

    List<Ticket> findAllByPatient(Patient patient);

    @Query(value = "select * from ticket t " +
                   "where t.doctor_id = :doctorId and date(t.time_appointment) = :date limit 1",
            nativeQuery = true)
    Optional<Ticket> findTicketByDate(Long doctorId, LocalDate date);
}
