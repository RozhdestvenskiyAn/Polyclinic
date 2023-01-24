package ru.rozhdestvenskiy.polyclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rozhdestvenskiy.polyclinic.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
