package ru.rozhdestvenskiy.polyclinic.web.soap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.polyclinic.soap.CreateScheduleRequest;
import ru.polyclinic.soap.CreateScheduleResponse;
import ru.rozhdestvenskiy.polyclinic.service.PolyclinicService;

@Slf4j
@Endpoint
public class PolyclinicEndpoint {
    private static final String NAMESPACE_URI = "http://polyclinic.ru/soap";

    private PolyclinicService polyclinicService;

    @Autowired
    public PolyclinicEndpoint(PolyclinicService polyclinicService) {
        this.polyclinicService = polyclinicService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createScheduleRequest")
    @ResponsePayload
    public CreateScheduleResponse createSchedule(@RequestPayload CreateScheduleRequest request) {
        log.info("Got SOAP request for create doctor's schedule by doctor id: {}, " +
                 "time schedule start: {},  duration: {} and count ticket: {}",
                request.getDoctorId(), request.getTimeStartSchedule(), request.getDurationMin(), request.getCount());

        polyclinicService.createSchedule(request);

        CreateScheduleResponse res = new CreateScheduleResponse();
        res.setMessage("Schedule is created and successfully saved");
        return res;
    }
}
