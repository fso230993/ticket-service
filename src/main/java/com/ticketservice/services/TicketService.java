package com.ticketservice.services;

import com.ticketservice.enums.SeatStatus;
import com.ticketservice.exceptions.NoTicketsFoundException;
import com.ticketservice.models.ReservationRequest;
import com.ticketservice.models.Seat;
import com.ticketservice.models.SeatHold;
import com.ticketservice.models.SeatReservation;
import com.ticketservice.repositories.SeatHoldRepository;
import com.ticketservice.repositories.SeatRepository;
import com.ticketservice.repositories.SeatReservationRepository;
import com.ticketservice.utils.IdGenerator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TicketService {

    private final SeatHoldRepository seatHoldRepository;
    private final SeatRepository seatRepository;
    private final SeatReservationRepository seatReservationRepository;

    public TicketService(SeatHoldRepository seatHoldRepository, SeatRepository seatRepository, SeatReservationRepository seatReservationRepository) {
        this.seatHoldRepository = seatHoldRepository;
        this.seatRepository = seatRepository;
        this.seatReservationRepository = seatReservationRepository;
    }

    private List<Seat> updateAvailableSeats() throws IOException {
        List<Seat> availableSeats = this.seatHoldRepository.verifyIfSeatHoldExpired();
        availableSeats.addAll(this.seatRepository.getAllSeatsAvailable());
        this.seatRepository.saveSeats(availableSeats);
        return availableSeats;
    }

    /**
     * The number of seats in the requested level that are neither held nor reserved
     *
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return the number of tickets available on the provided level
     */
    public int numSeatsAvailable(Optional<Integer> venueLevel) throws IOException {
        List<Seat> availableSeats = updateAvailableSeats();
        return venueLevel.map(integer -> (int) availableSeats.stream()
                .filter(seat -> seat.getVenueLevel().equals(venueLevel.get()))
                .count()).orElseGet(availableSeats::size);
    }

    private List<Seat> getBestSeatsAvailable(int numSeats, Stream<Seat> seatStream) {
        Map<Integer, List<Seat>> map = seatStream
                .collect(Collectors.groupingBy(Seat::getVenueLevel))
                .entrySet().stream().filter(entry -> entry.getValue().size() >= numSeats)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<Seat> availableSeats = map.values().stream().flatMap(List::stream).toList();

        if (availableSeats.isEmpty()) {
            throw new NoTicketsFoundException("No available seats");
        }

        return availableSeats.stream()
                .sorted(Comparator.comparingInt(Seat::getSeatId))
                .toList().subList(0, numSeats);
    }


    /**
     * Find and hold the best available seats for a customer
     *
     * @param ReservationRequest requestBody with the information needed for holding the seats
     * @return a SeatHold object identifying the specific seats and related
     * information
     */

    public SeatHold findAndHoldSeats(ReservationRequest reservationRequest) throws IOException {
        Stream<Seat> seatStream = updateAvailableSeats().stream();

        if (reservationRequest.getMinLevel() != null && reservationRequest.getMaxLevel() != null) {
            seatStream = seatStream
                    .filter(s -> s.getVenueLevel() >= reservationRequest.getMinLevel()
                            && s.getVenueLevel() <= reservationRequest.getMaxLevel());
        }

        Date holdStartDate = new Date();
        Date holdEndDate = Date.from(holdStartDate.toInstant()
                .plusSeconds(reservationRequest.getSecondsToExpire()));
        List<Seat> selectedSeats = getBestSeatsAvailable(reservationRequest.getNumSeats(), seatStream);
        selectedSeats.forEach(s -> s.setSeatStatus(SeatStatus.HELD));
        int id = IdGenerator.generateSeatHoldId();
        while (this.seatHoldRepository.holdIdExists(id)) {
            id = IdGenerator.generateSeatHoldId();
        }
        SeatHold seatHold = this.seatHoldRepository.saveSeatHold(SeatHold.builder()
                .seatHoldId(id)
                .customerEmail(reservationRequest.getCustomerEmail())
                .selectedSeats(selectedSeats)
                .holdStartDate(holdStartDate)
                .holdEndDate(holdEndDate)
                .build());

        this.seatRepository.updateSeatStatus(selectedSeats);
        return seatHold;
    }

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold
     *                      is assigned
     * @return a reservation confirmation code
     */

    public String reserveSeats(int seatHoldId, String customerEmail) throws IOException {
        updateAvailableSeats();
        SeatHold seatHold = this.seatHoldRepository.getSeatHold(seatHoldId);
        String confirmationId = "";

        if (seatHold != null && seatHold.getHoldEndDate().toInstant().isAfter(Instant.now())) {
            confirmationId = IdGenerator.generateConfirmationCode();
            while (IdGenerator.getGeneratedConfirmationCodes().contains(confirmationId)) {
                confirmationId = IdGenerator.generateConfirmationCode();
            }
            seatHold.getSelectedSeats().forEach(s -> s.setSeatStatus(SeatStatus.RESERVED));
            double total = seatHold.getSelectedSeats().stream()
                    .mapToDouble(Seat::getPrice).sum();
            this.seatReservationRepository.saveSeatReservation(SeatReservation.builder()
                    .selectedSeats(seatHold.getSelectedSeats())
                    .confirmationId(confirmationId)
                    .customerEmail(customerEmail)
                    .total(total)
                    .build());
            this.seatHoldRepository.deleteReservedHold(seatHoldId);
        }
        return confirmationId;
    }
}