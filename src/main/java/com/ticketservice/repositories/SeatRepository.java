package com.ticketservice.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketservice.enums.SeatStatus;
import com.ticketservice.models.Seat;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.ticketservice.constants.TicketConstants.SEATS_PATH;

@Component
public class SeatRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Seat> getAllSeatsAvailable() throws IOException {
        return objectMapper.readValue(new File(SEATS_PATH), new TypeReference<List<Seat>>() {})
                .stream()
                .filter(seat -> seat.getSeatStatus().equals(SeatStatus.AVAILABLE))
                .toList();
    }
    public void saveSeats(List<Seat> seats) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(SEATS_PATH), seats);
    }

    public void updateSeatStatus(List<Seat> selectedSeats) throws IOException {
            List<Seat> seats = getAllSeatsAvailable();
            seats.removeAll(selectedSeats);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(SEATS_PATH), seats);
    }

}
