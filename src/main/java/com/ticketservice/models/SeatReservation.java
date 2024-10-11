package com.ticketservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class SeatReservation {
    private String confirmationId;
    private String customerEmail;
    private List<Seat> selectedSeats;
    private double total;
}
