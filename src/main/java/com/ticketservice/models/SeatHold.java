package com.ticketservice.models;

import lombok.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatHold {
    private int seatHoldId;
    private String customerEmail;
    private List<Seat> selectedSeats;
    private Date holdStartDate;
    private Date holdEndDate;
}
