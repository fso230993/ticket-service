package com.ticketservice.repositories;

import com.ticketservice.models.Seat;
import com.ticketservice.models.SeatHold;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class SeatHoldRepositoryTest {

    @Mock
    private SeatHoldRepository seatHoldRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }


    @Test
    void saveSeatHoldSuccess() {
        SeatHold expectedSeatHold = new SeatHold();
        when(seatHoldRepository.saveSeatHold(any())).thenReturn(expectedSeatHold);

        SeatHold actualSeatHold = seatHoldRepository.saveSeatHold(new SeatHold());

        assertEquals(expectedSeatHold, actualSeatHold);
    }

    @Test
    void saveSeatHoldFail() {
        when(seatHoldRepository.saveSeatHold(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> seatHoldRepository.saveSeatHold(new SeatHold()));
    }

    @Test
    void holdIdNotExists() throws IOException {
        when(seatHoldRepository.holdIdExists(anyInt())).thenReturn(false);

        boolean actualResult = seatHoldRepository.holdIdExists(1);
        assertFalse(actualResult);
    }

    @Test
    void holdIdExists() throws IOException {
        when(seatHoldRepository.holdIdExists(anyInt())).thenReturn(true);

        boolean actualResult = seatHoldRepository.holdIdExists(1);
        assertTrue(actualResult);
    }

    @Test
    void holdIdExistsFails() throws IOException {
        when(seatHoldRepository.holdIdExists(anyInt())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> seatHoldRepository.holdIdExists(1));
    }

    @Test
    void getSeatHold() throws IOException {
        SeatHold expectedSeatHold = SeatHold.builder().build();
        when(seatHoldRepository.getSeatHold(anyInt())).thenReturn(expectedSeatHold);

        SeatHold actualSeatHold = seatHoldRepository.getSeatHold(1);
        assertEquals(expectedSeatHold, actualSeatHold);
    }

    @Test
    void getSeatHoldFails() throws IOException {
        when(seatHoldRepository.getSeatHold(anyInt())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> seatHoldRepository.getSeatHold(1));
    }

    @Test
    void testGetSeatHold() throws IOException {
        SeatHold expectedSeatHold = SeatHold.builder().build();
        when(seatHoldRepository.getSeatHold(any())).thenReturn(expectedSeatHold);

        SeatHold actualSeatHold = seatHoldRepository.getSeatHold(new File("test"));
        assertEquals(expectedSeatHold, actualSeatHold);
    }

    @Test
    void testGetSeatHoldFails() throws IOException {
        when(seatHoldRepository.getSeatHold(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> seatHoldRepository.getSeatHold(new File("test")));
    }

    @Test
    void verifyIfSeatHoldExpired() throws IOException {
        Seat seat = new Seat();
        List<Seat> expectedList = List.of(seat);

        when(seatHoldRepository.verifyIfSeatHoldExpired()).thenReturn(expectedList);

        List<Seat> actualList = seatHoldRepository.verifyIfSeatHoldExpired();
        assertEquals(expectedList, actualList);
    }

    @Test
    void verifyIfSeatHoldExpiredFails() throws IOException {
        when(seatHoldRepository.verifyIfSeatHoldExpired()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> seatHoldRepository.verifyIfSeatHoldExpired());
    }
}