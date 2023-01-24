package ru.rozhdestvenskiy.polyclinic.web.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rozhdestvenskiy.polyclinic.dto.TicketDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {
    List<TicketDto> tickets;
}
