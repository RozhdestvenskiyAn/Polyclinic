package ru.rozhdestvenskiy.polyclinic.web.soap.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

@Slf4j
public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        log.warn(ex.toString());
    }

}
