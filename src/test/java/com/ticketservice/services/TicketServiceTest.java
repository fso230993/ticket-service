package com.ticketservice.services;

import com.ticketservice.exceptions.NoTicketsFoundException;
import com.ticketservice.models.ReservationRequest;
import com.ticketservice.models.Seat;
import com.ticketservice.models.SeatHold;
import com.ticketservice.models.SeatReservation;
import com.ticketservice.repositories.SeatHoldRepository;
import com.ticketservice.repositories.SeatRepository;
import com.ticketservice.repositories.SeatReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class TicketServiceTest {
    @Mock
    private SeatHoldRepository seatHoldRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private SeatReservationRepository seatReservationRepository;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void numSeatsAvailableNoLvl() throws IOException {

        List<Seat> availableSeats = new ArrayList<>();
        availableSeats.add(new Seat());

        when(seatHoldRepository.verifyIfSeatHoldExpired()).thenReturn(availableSeats);
        when(seatRepository.getAllSeatsAvailable()).thenReturn(availableSeats);

        int expectedCount = availableSeats.size() * 2;

        int count = ticketService.numSeatsAvailable(Optional.empty());
        assertEquals(expectedCount,count);
    }

    @Test
    void numSeatsAvailableLvl() throws IOException {

        List<Seat> availableSeats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setVenueLevel(1);
        availableSeats.add(seat);

        when(seatHoldRepository.verifyIfSeatHoldExpired()).thenReturn(availableSeats);
        when(seatRepository.getAllSeatsAvailable()).thenReturn(availableSeats);

        int expectedCount = availableSeats.size() * 2;

        int count = ticketService.numSeatsAvailable(Optional.of(1));
        assertEquals(expectedCount,count);
    }

    @Test
    void findAndHoldSeatsNoLvl() throws IOException {

        List<Seat> availableSeats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setVenueLevel(1);
        seat.setSeatId(3);
        availableSeats.add(seat);
        SeatHold expectedSeatHold = new SeatHold();

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(1)
                .secondsToExpire(5)
                .build();
        when(seatHoldRepository.verifyIfSeatHoldExpired()).thenReturn(availableSeats);
        when(seatRepository.getAllSeatsAvailable()).thenReturn(availableSeats);
        when(seatHoldRepository.saveSeatHold(any())).thenReturn(expectedSeatHold);

        SeatHold actualResult = ticketService.findAndHoldSeats(reservationRequest);
        assertEquals(expectedSeatHold,actualResult);
    }

    @Test
    void findAndHoldSeatsLvl() throws IOException {

        List<Seat> availableSeats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setVenueLevel(1);
        seat.setSeatId(13);
        availableSeats.add(seat);
        SeatHold expectedSeatHold = new SeatHold();

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(1)
                .minLevel(1)
                .maxLevel(5)
                .secondsToExpire(5)
                .build();
        when(seatHoldRepository.verifyIfSeatHoldExpired()).thenReturn(availableSeats);
        when(seatRepository.getAllSeatsAvailable()).thenReturn(availableSeats);
        when(seatHoldRepository.saveSeatHold(any())).thenReturn(expectedSeatHold);

        SeatHold actualResult = ticketService.findAndHoldSeats(reservationRequest);
        assertEquals(expectedSeatHold,actualResult);
    }

    @Test
    void findAndHoldSeatsNoLvlNotFound() throws IOException {

        List<Seat> availableSeats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setVenueLevel(1);
        seat.setSeatId(13);
        availableSeats.add(seat);
        SeatHold expectedSeatHold = new SeatHold();

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(5)
                .secondsToExpire(5)
                .build();
        when(seatHoldRepository.verifyIfSeatHoldExpired()).thenReturn(availableSeats);
        when(seatRepository.getAllSeatsAvailable()).thenReturn(availableSeats);
        when(seatHoldRepository.saveSeatHold(any())).thenReturn(expectedSeatHold);

        assertThrows(NoTicketsFoundException.class, () -> {
            ticketService.findAndHoldSeats(reservationRequest);
        });

    }

    @Test
    void findAndHoldSeatsLvlNotFound() throws IOException {

        List<Seat> availableSeats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setVenueLevel(1);
        seat.setSeatId(13);
        availableSeats.add(seat);
        SeatHold expectedSeatHold = new SeatHold();

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(3)
                .minLevel(1)
                .maxLevel(5)
                .secondsToExpire(5)
                .build();
        when(seatHoldRepository.verifyIfSeatHoldExpired()).thenReturn(availableSeats);
        when(seatRepository.getAllSeatsAvailable()).thenReturn(availableSeats);
        when(seatHoldRepository.saveSeatHold(any())).thenReturn(expectedSeatHold);

        assertThrows(NoTicketsFoundException.class, () -> {
            ticketService.findAndHoldSeats(reservationRequest);
        });

    }

    @Test
    void findAndHoldSeatsNoLvlFail() throws IOException {

        List<Seat> availableSeats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setVenueLevel(1);
        seat.setSeatId(13);
        availableSeats.add(seat);

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(5)
                .secondsToExpire(5)
                .build();
        when(seatHoldRepository.verifyIfSeatHoldExpired()).thenReturn(availableSeats);
        when(seatRepository.getAllSeatsAvailable()).thenThrow(new RuntimeException());
        when(seatHoldRepository.saveSeatHold(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            ticketService.findAndHoldSeats(reservationRequest);
        });

    }

    @Test
    void findAndHoldSeatsLvlFail() throws IOException {

        List<Seat> availableSeats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setVenueLevel(1);
        seat.setSeatId(13);
        availableSeats.add(seat);

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .customerEmail("test@gmail.com")
                .numSeats(3)
                .minLevel(1)
                .maxLevel(5)
                .secondsToExpire(5)
                .build();
        when(seatHoldRepository.verifyIfSeatHoldExpired()).thenReturn(availableSeats);
        when(seatRepository.getAllSeatsAvailable()).thenReturn(availableSeats);
        when(seatHoldRepository.saveSeatHold(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            ticketService.findAndHoldSeats(reservationRequest);
        });
    }

    @Test
    void reserveSeats() throws IOException {
        String code = "ABCDE";
        int seatHoldId = 1;
        String customerEmail = "test@gmail.com";

        SeatReservation expectedSeatReservation = SeatReservation
                .builder()
                .confirmationId(code)
                .build();
        SeatHold seatHold = new SeatHold();
        Seat seat = new Seat();
        seat.setPrice(100.00);
        seatHold.setSelectedSeats(List.of(seat));
        seatHold.setHoldEndDate(Date.from(Instant.now().plusSeconds(100000)));
        when(seatHoldRepository.getSeatHold(anyInt())).thenReturn(seatHold);
        when(seatReservationRepository.saveSeatReservation(any())).thenReturn(expectedSeatReservation);

        String actualReservation = ticketService.reserveSeats(seatHoldId, customerEmail);
        assertEquals(5, actualReservation.length());
    }
}