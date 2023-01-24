package ru.rozhdestvenskiy.polyclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rozhdestvenskiy.polyclinic.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
