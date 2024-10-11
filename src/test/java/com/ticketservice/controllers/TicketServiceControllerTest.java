package com.ticketservice.controllers;

import com.ticketservice.exceptions.NoTicketsFoundException;
import com.ticketservice.models.ReservationRequest;
import com.ticketservice.models.SeatHold;
import com.ticketservice.services.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


class TicketServiceControllerTest {
    @InjectMocks
    private TicketServiceController ticketServiceController;

    @Mock
    private TicketService ticketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAvailableSeatsCountNoVenueLvlSuccess() throws IOException {
        int expectedCount = 10;
        when(ticketService.numSeatsAvailable(Optional.empty())).thenReturn(expectedCount);

        int actualCount = ticketServiceController.getAvailableSeatsCount(Optional.empty());
        assertEquals(expectedCount, actualCount);
    }

    @Test
    void getAvailableSeatsCountNoVenueLvlFail() throws IOException {
        when(ticketService.numSeatsAvailable(Optional.empty())).thenThrow(new IOException());

        int actualCount = ticketServiceController.getAvailableSeatsCount(Optional.empty());
        assertEquals(-1, actualCount);
    }

    @Test
    void getAvailableSeatsCountVenueLvlSuccess() throws IOException {
        int expectedCount = 2;
        when(ticketService.numSeatsAvailable(Optional.of(1))).thenReturn(expectedCount);

        int actualCount = ticketServiceController.getAvailableSeatsCount(Optional.of(1));
        assertEquals(expectedCount, actualCount);
    }

    @Test
    void getAvailableSeatsCountVenueLvlFail() throws IOException {
        int expectedCount = -1;
        when(ticketService.numSeatsAvailable(Optional.of(1))).thenThrow(new IOException());

        int actualCount = ticketServiceController.getAvailableSeatsCount(Optional.of(1));
        assertEquals(expectedCount, actualCount);
    }

    @Test
    void findAndHoldSeatsNoLvlSuccess() throws IOException {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(3)
                .secondsToExpire(5)
                .build();
        SeatHold expectedSeatHold = SeatHold.builder()
                .seatHoldId(1)
                .customerEmail("test@gmail.com")
                .build();
        when(ticketService.findAndHoldSeats(reservationRequest)).thenReturn(expectedSeatHold);

        SeatHold actualResult = ticketServiceController.findAndHoldSeats(reservationRequest);
        assertEquals(expectedSeatHold, actualResult);
    }

    @Test
    void findAndHoldSeatsNoLvlFail() throws IOException {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(3)
                .secondsToExpire(5)
                .build();
        when(ticketService.findAndHoldSeats(reservationRequest)).thenThrow(new IOException());

        SeatHold actualResult = ticketServiceController.findAndHoldSeats(reservationRequest);
        assertNull(actualResult);
    }

    @Test
    void findAndHoldSeatsNoLvlNotFound() throws IOException {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(3)
                .secondsToExpire(5)
                .build();
        when(ticketService.findAndHoldSeats(reservationRequest)).thenThrow(new NoTicketsFoundException("No tickets found"));

        SeatHold actualResult = ticketServiceController.findAndHoldSeats(reservationRequest);
        assertNull(actualResult);
    }

    @Test
    void findAndHoldSeatsLvlSuccess() throws IOException {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(3)
                .minLevel(1)
                .maxLevel(5)
                .secondsToExpire(5)
                .build();
        SeatHold expectedSeatHold = SeatHold.builder()
                .seatHoldId(1)
                .customerEmail("test@gmail.com")
                .build();
        when(ticketService.findAndHoldSeats(reservationRequest)).thenReturn(expectedSeatHold);

        SeatHold actualResult = ticketServiceController.findAndHoldSeats(reservationRequest);
        assertEquals(expectedSeatHold, actualResult);
    }

    @Test
    void findAndHoldSeatsLvlFail() throws IOException {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(3)
                .minLevel(1)
                .maxLevel(5)
                .secondsToExpire(5)
                .build();
        when(ticketService.findAndHoldSeats(reservationRequest)).thenThrow(new IOException());

        SeatHold actualResult = ticketServiceController.findAndHoldSeats(reservationRequest);
        assertNull(actualResult);
    }

    @Test
    void findAndHoldSeatLvlNotFound() throws IOException {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(3)
                .minLevel(1)
                .maxLevel(5)
                .secondsToExpire(5)
                .build();
        when(ticketService.findAndHoldSeats(reservationRequest)).thenThrow(new NoTicketsFoundException("No tickets found"));

        SeatHold actualResult = ticketServiceController.findAndHoldSeats(reservationRequest);
        assertNull(actualResult);
    }

    @Test
    void reserveSeatsSuccess() throws IOException {
        int holdId = 0;
        String customerEmail = "test@gmail.com";
        String reservationCode = "ABCDE";
        when(ticketService.reserveSeats(holdId,customerEmail)).thenReturn(reservationCode);

        String actualResult = ticketServiceController.reserveSeats(holdId, customerEmail);
        assertEquals(reservationCode, actualResult);
    }

    @Test
    void reserveSeatsHoldNotFound() throws IOException {
        int holdId = 0;
        String customerEmail = "test@gmail.com";
        String expectedCode = "";
        when(ticketService.reserveSeats(holdId,customerEmail)).thenReturn(expectedCode);

        String actualResult = ticketServiceController.reserveSeats(holdId, customerEmail);
        assertEquals(expectedCode, actualResult);
    }

    @Test
    void reserveSeatsHoldExpired() throws IOException {
        int holdId = 0;
        String customerEmail = "test@gmail.com";
        String expectedCode = "";
        when(ticketService.reserveSeats(holdId,customerEmail)).thenReturn(expectedCode);

        String actualResult = ticketServiceController.reserveSeats(holdId, customerEmail);
        assertEquals(expectedCode, actualResult);
    }

    @Test
    void reserveSeatsHoldFail() throws IOException {
        int holdId = 0;
        String customerEmail = "test@gmail.com";
        when(ticketService.reserveSeats(holdId,customerEmail)).thenThrow(new IOException());

        String actualResult = ticketServiceController.reserveSeats(holdId, customerEmail);
        assertNull(actualResult);
    }
}