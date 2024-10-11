package com.ticketservice.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketservice.models.SeatReservation;
import com.ticketservice.utils.IdGenerator;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class SeatReservationRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SeatReservation saveSeatReservation(SeatReservation seatReservation) throws IOException {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(
                            "src/main/resources/data/seat-reservation-" + seatReservation.getConfirmationId() + ".json"),
                    seatReservation);
            IdGenerator.addGeneratedConfirmationCodes(seatReservation.getConfirmationId());
        return seatReservation;
    }
}
