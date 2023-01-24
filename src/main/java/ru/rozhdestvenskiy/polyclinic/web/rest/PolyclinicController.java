package ru.rozhdestvenskiy.polyclinic.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rozhdestvenskiy.polyclinic.web.rest.request.FreeTicketsRequest;
import ru.rozhdestvenskiy.polyclinic.web.rest.response.MessageResponse;
import ru.rozhdestvenskiy.polyclinic.web.rest.response.TicketResponse;
import ru.rozhdestvenskiy.polyclinic.service.PolyclinicService;

@Slf4j
@RestController
@RequestMapping("/rest-api")
public class PolyclinicController {

    private final PolyclinicService polyclinicService;

    @Autowired
    public PolyclinicController(PolyclinicService polyclinicService) {
        this.polyclinicService = polyclinicService;
    }

    @GetMapping("/free-ticket")
    public TicketResponse getFreeTickets(FreeTicketsRequest req) {
        log.info("Got REST request for get free tickets by doctor's id: {} and date: {}",
                req.getDoctorId(), req.getDate());

        TicketResponse response = polyclinicService.getFreeTicketsBy(req);
        log.info("REST response with list of free time appointment: {}", response);
        return response;
    }

    @GetMapping("/ticket/{id}")
    public MessageResponse getTicket(@PathVariable Long id) {
        log.info("Got REST request for get ticket's info by id: {}", id);

        MessageResponse response = polyclinicService.getTicket(id);
        log.info("REST response with message ticket employment: {}", response);
        return response;
    }

    @GetMapping("/occupied-ticket/{id}")
    public TicketResponse getOccupiedTicketsByPatientId(@PathVariable Long id) {
        log.info("Got REST request for get occupied tickets by user's id: {}", id);

        TicketResponse response = polyclinicService.getOccupiedTicketsByPatientId(id);
        log.info("REST response with list of free time appointment: {}", response);
        return response;
    }
}
