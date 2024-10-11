package com.ticketservice.controllers;

import com.ticketservice.exceptions.NoTicketsFoundException;
import com.ticketservice.models.ReservationRequest;
import com.ticketservice.models.SeatHold;
import com.ticketservice.services.TicketService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
public class TicketServiceController {
    private final TicketService ticketService;

    public TicketServiceController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/availableSeatsCount")
    public int getAvailableSeatsCount(@RequestParam(required = false) Optional<Integer> venueLevel) {
        try {
            return this.ticketService.numSeatsAvailable(venueLevel);
        } catch (IOException e) {
            return -1;
        }
    }

    @PostMapping("/findAndHoldSeats")
    public SeatHold findAndHoldSeats(@RequestBody ReservationRequest reservationRequest) {
        try {
            return this.ticketService.findAndHoldSeats(reservationRequest);
        } catch (IOException | NoTicketsFoundException e) {
            return null;
        }
    }

    @PostMapping("/reserveSeats")
    public String reserveSeats(@RequestParam int seatHoldId, @RequestParam String customerEmail) {
        try {
            return this.ticketService.reserveSeats(seatHoldId, customerEmail);
        } catch (IOException e) {
            return null;
        }
    }
}
