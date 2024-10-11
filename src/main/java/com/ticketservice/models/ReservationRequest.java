package com.ticketservice.models;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ReservationRequest {
    private int numSeats;
    @Nullable
    private Integer minLevel;
    @Nullable
    private Integer maxLevel;
    private String customerEmail;
    private int secondsToExpire;
}
