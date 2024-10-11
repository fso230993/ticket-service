package com.ticketservice.models;

import com.ticketservice.enums.SeatStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {
    private Integer seatId;
    private Integer venueLevel;
    private SeatStatus seatStatus;
    private Double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;
        Seat seat = (Seat) o;
        return seatId.equals(seat.seatId);
    }

    @Override
    public int hashCode() {
        return seatId.hashCode();
    }
}
