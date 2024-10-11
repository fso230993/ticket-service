package com.ticketservice.tasks;

import com.ticketservice.repositories.SeatHoldRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScheduledTask {
    private final SeatHoldRepository seatHoldRepository;

    public ScheduledTask(SeatHoldRepository seatHoldRepository) {
        this.seatHoldRepository = seatHoldRepository;
    }

    @Scheduled(fixedRate = 30000)
    public void updateSeatsAvailable() throws IOException {
        this.seatHoldRepository.verifyIfSeatHoldExpired();
    }
}
