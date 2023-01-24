package ru.rozhdestvenskiy.polyclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.polyclinic.soap.CreateScheduleRequest;
import ru.rozhdestvenskiy.polyclinic.dto.TicketDto;
import ru.rozhdestvenskiy.polyclinic.model.Doctor;
import ru.rozhdestvenskiy.polyclinic.model.Patient;
import ru.rozhdestvenskiy.polyclinic.model.Ticket;
import ru.rozhdestvenskiy.polyclinic.repository.DoctorRepository;
import ru.rozhdestvenskiy.polyclinic.repository.PatientRepository;
import ru.rozhdestvenskiy.polyclinic.repository.TicketRepository;
import ru.rozhdestvenskiy.polyclinic.web.rest.request.FreeTicketsRequest;
import ru.rozhdestvenskiy.polyclinic.web.rest.response.MessageResponse;
import ru.rozhdestvenskiy.polyclinic.web.rest.response.TicketResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PolyclinicServiceTest {

    @Mock
    TicketRepository ticketRepository;
    @Mock
    DoctorRepository doctorRepository;
    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PolyclinicService polyclinicService;

    @Test
    void getFreeTicketsBy_ReturnValidTicketsResponse() {
        // given
        FreeTicketsRequest freeTicketsRequest = new FreeTicketsRequest();
        freeTicketsRequest.setDoctorId(1L);
        freeTicketsRequest.setDate(LocalDate.of(2023, 1, 23));

        Doctor doctor1 = new Doctor();
        doctor1.setId(freeTicketsRequest.getDoctorId());
        doctor1.setFirstName("Ivan");
        doctor1.setLastName("Ivanov");
        doctor1.setPatronymic("Ivanovich");

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setLastName("Gomozov");
        patient.setFirstName("Andrey");
        patient.setDateBirthday(LocalDate.of(1994, 5, 17));

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setPatient(null);
        ticket1.setDoctor(doctor1);
        ticket1.setTimeAppointment(LocalDateTime.of(2023, 1, 23, 9, 0));

        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        ticket2.setPatient(patient);
        ticket2.setDoctor(doctor1);
        ticket2.setTimeAppointment(LocalDateTime.of(2023, 1, 23, 8, 0));

        TicketDto ticketDto1 = TicketDto.builder()
                .doctor(ticket1.getDoctor().getFIO())
                .patient("free")
                .time(ticket1.getTimeAppointment().toString())
                .build();

        TicketDto ticketDto2 = TicketDto.builder()
                .doctor(ticket2.getDoctor().getFIO())
                .patient(ticket2.getPatient().getFIO())
                .time(ticket2.getTimeAppointment().toString())
                .build();

        List<Ticket> tickets = List.of(ticket1, ticket2);
        List<TicketDto> ticketsDto = List.of(ticketDto1, ticketDto2);

        doReturn(Optional.of(doctor1)).when(doctorRepository).findById(freeTicketsRequest.getDoctorId());
        doReturn(tickets).when(ticketRepository).findAllFreeTicketsBy(doctor1.getId(), freeTicketsRequest.getDate());

        //when
        TicketResponse resultResponse = polyclinicService.getFreeTicketsBy(freeTicketsRequest);

        //then
        assertNotNull(resultResponse);
        assertEquals(ticketsDto.size(), resultResponse.getTickets().size());
        assertEquals(ticketsDto.get(0), resultResponse.getTickets().get(0));
    }

    @Test
    void getFreeTicketsBy_TicketIsFree_ReturnValidMessageResponse() {
        // given
        long id = 1;

        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setFirstName("Ivan");
        doctor1.setLastName("Ivanov");
        doctor1.setPatronymic("Ivanovich");

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setLastName("Gomozov");
        patient.setFirstName("Andrey");
        patient.setDateBirthday(LocalDate.of(1994, 5, 17));

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setPatient(null);
        ticket1.setDoctor(doctor1);
        ticket1.setTimeAppointment(LocalDateTime.of(2023, 1, 23, 9, 0));

        doReturn(Optional.of(ticket1)).when(ticketRepository).findById(id);
        //when
        MessageResponse resultResponse = polyclinicService.getTicket(id);
        //then
        assertNotNull(resultResponse);
        assertEquals("Ticket is free", resultResponse.getMessage());
    }

    @Test
    void getFreeTicketsBy_TicketIsBusy_ReturnValidMessageResponse() {
        // given
        long id = 1;

        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setFirstName("Ivan");
        doctor1.setLastName("Ivanov");
        doctor1.setPatronymic("Ivanovich");

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setLastName("Gomozov");
        patient.setFirstName("Andrey");
        patient.setDateBirthday(LocalDate.of(1994, 5, 17));

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setPatient(patient);
        ticket1.setDoctor(doctor1);
        ticket1.setTimeAppointment(LocalDateTime.of(2023, 1, 23, 9, 0));

        doReturn(Optional.of(ticket1)).when(ticketRepository).findById(id);
        //when
        MessageResponse resultResponse = polyclinicService.getTicket(id);
        //then
        assertNotNull(resultResponse);
        assertEquals("Ticket is occupied by: " + ticket1.getPatient().getFIO(),
                resultResponse.getMessage());
    }

    @Test
    void getOccupiedTicketsBy_ReturnValidTicketsResponse() {
        // given
        long patientId = 1L;

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Ivan");
        doctor.setLastName("Ivanov");
        doctor.setPatronymic("Ivanovich");

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setLastName("Gomozov");
        patient.setFirstName("Andrey");
        patient.setDateBirthday(LocalDate.of(1994, 5, 17));

        Ticket ticket = new Ticket();
        ticket.setId(2L);
        ticket.setPatient(patient);
        ticket.setDoctor(doctor);
        ticket.setTimeAppointment(LocalDateTime.of(2023, 1, 23, 8, 0));

        TicketDto ticketDto = TicketDto.builder()
                .doctor(ticket.getDoctor().getFIO())
                .patient(ticket.getPatient().getFIO())
                .time(ticket.getTimeAppointment().toString())
                .build();

        List<Ticket> tickets = List.of(ticket);
        List<TicketDto> ticketsDto = List.of(ticketDto);

        doReturn(Optional.of(patient)).when(patientRepository).findById(patientId);
        doReturn(tickets).when(ticketRepository).findAllByPatient(patient);

        //when
        TicketResponse resultResponse = polyclinicService.getOccupiedTicketsByPatientId(1L);

        //then
        assertNotNull(resultResponse);
        assertEquals(ticketsDto.size(), resultResponse.getTickets().size());
        assertEquals(ticketsDto.get(0), resultResponse.getTickets().get(0));
    }

    @Test
    void createSchedule_SuccessSaveFreeTickets() throws DatatypeConfigurationException {
        //given
//        LocalDateTime date = LocalDateTime.of(2023, 1, 25, 8, 0);
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest();
        createScheduleRequest.setDoctorId(1L);
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        xmlGregorianCalendar.setYear(2023);
        xmlGregorianCalendar.setMonth(1);
        xmlGregorianCalendar.setDay(26);
        xmlGregorianCalendar.setHour(8);
        xmlGregorianCalendar.setMinute(0);
        createScheduleRequest.setTimeStartSchedule(xmlGregorianCalendar);
        createScheduleRequest.setDurationMin(30);
        createScheduleRequest.setCount(2);

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Ivan");
        doctor.setLastName("Ivanov");
        doctor.setPatronymic("Ivanovich");

        Ticket ticket1 = new Ticket();
        ticket1.setTimeAppointment(LocalDateTime.of(2023, 1, 26, 8, 0));
        ticket1.setPatient(null);
        ticket1.setDoctor(doctor);

        Ticket ticket2 = new Ticket();
        ticket2.setTimeAppointment(LocalDateTime.of(2023, 1, 26, 8, 30));
        ticket2.setPatient(null);
        ticket2.setDoctor(doctor);

        doReturn(Optional.of(doctor)).when(doctorRepository).findById(createScheduleRequest.getDoctorId());

        polyclinicService.createSchedule(createScheduleRequest);

        Mockito.verify(ticketRepository).save(ticket1);
        Mockito.verify(ticketRepository).save(ticket2);
    }
}