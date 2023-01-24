package ru.rozhdestvenskiy.polyclinic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polyclinic.soap.CreateScheduleRequest;
import ru.rozhdestvenskiy.polyclinic.dto.TicketDto;
import ru.rozhdestvenskiy.polyclinic.model.Doctor;
import ru.rozhdestvenskiy.polyclinic.model.Patient;
import ru.rozhdestvenskiy.polyclinic.model.Ticket;
import ru.rozhdestvenskiy.polyclinic.repository.DoctorRepository;
import ru.rozhdestvenskiy.polyclinic.repository.PatientRepository;
import ru.rozhdestvenskiy.polyclinic.repository.TicketRepository;
import ru.rozhdestvenskiy.polyclinic.web.exception.DoctorNotFoundException;
import ru.rozhdestvenskiy.polyclinic.web.exception.PatientNotFoundException;
import ru.rozhdestvenskiy.polyclinic.web.exception.ScheduleAlreadyExistsException;
import ru.rozhdestvenskiy.polyclinic.web.exception.TicketNotFoundException;
import ru.rozhdestvenskiy.polyclinic.web.rest.request.FreeTicketsRequest;
import ru.rozhdestvenskiy.polyclinic.web.rest.response.MessageResponse;
import ru.rozhdestvenskiy.polyclinic.web.rest.response.TicketResponse;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PolyclinicService {

    private final TicketRepository ticketRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public PolyclinicService(TicketRepository ticketRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.ticketRepository = ticketRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public TicketResponse getFreeTicketsBy(FreeTicketsRequest req) {

        log.info("Checking doctor's id: {} ...", req.getDoctorId());
        Doctor doctor = getDoctorById(req.getDoctorId());
        log.info("Got doctor: {}", doctor);

        log.info("Getting list of free tickets by doctor: {} and date: {} ...", doctor, req.getDate());
        List<Ticket> tickets = ticketRepository.findAllFreeTicketsBy(doctor.getId(), req.getDate());
        log.info("Got list of free tickets in the amount of {}", tickets.size());

        log.info("Mapping to ticket response");
        return TicketResponse.builder()
                .tickets(tickets.stream()
                        .map(ticket -> TicketDto.builder()
                                .doctor(ticket.getDoctor().getFIO())
                                .patient("free")
                                .time(ticket.getTimeAppointment().toString())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found by id: " + id));
    }

    @Transactional
    public MessageResponse getTicket(Long id) {

        log.info("Getting ticket by id: {} ......", id);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found by id: " + id));

        MessageResponse messageResponse = new MessageResponse();
        if (ticket.getPatient() == null) {
            log.info("Ticket is free");
            messageResponse.setMessage("Ticket is free");
        } else {
            log.info("Ticket is occupied by: {}", ticket.getPatient().getFIO());
            messageResponse.setMessage("Ticket is occupied by: " + ticket.getPatient().getFIO());
        }
        return messageResponse;
    }

    @Transactional
    public TicketResponse getOccupiedTicketsByPatientId(Long id) {

        log.info("Getting patient by id: {} ......", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found by id: " + id));

        log.info("Getting list of occupied tickets by patient: {} ...", patient);
        List<Ticket> tickets = ticketRepository.findAllByPatient(patient);
        log.info("Got list of occupied tickets by: {} in the amount of {}", patient, tickets.size());

        log.info("Mapping to ticket response");
        return TicketResponse.builder()
                .tickets(tickets.stream()
                        .map(ticket -> TicketDto.builder()
                                .doctor(ticket.getDoctor().getFIO())
                                .patient(ticket.getPatient().getFIO())
                                .time(ticket.getTimeAppointment().toString())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void createSchedule(CreateScheduleRequest request) {

        log.info("Getting doctor by id: {} ...", request.getDoctorId());
        Doctor doctor = getDoctorById(request.getDoctorId());

        LocalDateTime startTimeSchedule = request.getTimeStartSchedule().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
        LocalDate date = startTimeSchedule.toLocalDate();
        log.info("Checking doctor's schedule on: {} ...", startTimeSchedule.toLocalDate());
        ticketRepository.findTicketByDate(doctor.getId(), date)
                .ifPresent((ticket) -> {
                    throw new ScheduleAlreadyExistsException("Doctor's schedule on date " + date + "is already exists");
                });

        Duration duration = Duration.ofMinutes(request.getDurationMin());
        for (
                int i = 0; i < request.getCount(); i++) {
            Ticket ticket = new Ticket();
            ticket.setDoctor(doctor);
            ticket.setTimeAppointment(startTimeSchedule);
            Ticket savedTicket = ticketRepository.save(ticket);
            log.info("Saved ticket: {}", savedTicket);
            startTimeSchedule = startTimeSchedule.plus(duration);
        }
    }
}
