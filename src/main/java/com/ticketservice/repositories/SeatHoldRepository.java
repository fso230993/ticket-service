package com.ticketservice.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketservice.models.Seat;
import com.ticketservice.models.SeatHold;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ticketservice.constants.TicketConstants.SEATHOLD_PATH;

@Component
public class SeatHoldRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SeatHold saveSeatHold(SeatHold seatHold) throws IOException {
        String fileName = String.format(SEATHOLD_PATH, seatHold.getSeatHoldId());
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(fileName), seatHold);
        return seatHold;
    }

    public boolean holdIdExists(int seatHoldId) {
        return new File(String.format(SEATHOLD_PATH, seatHoldId)).exists();
    }

    public SeatHold getSeatHold(int seatHoldId) throws IOException {
        return objectMapper.readValue(new File(String.format(SEATHOLD_PATH, seatHoldId)), new TypeReference<SeatHold>() {
        });
    }

    public SeatHold getSeatHold(File fileName) throws IOException {
        return objectMapper.readValue(fileName, new TypeReference<SeatHold>() {
        });
    }

    private void deleteExpiredHolds(List<File> expiredFiles) {
        expiredFiles.forEach(File::delete);
    }

    public void deleteReservedHold(int seatHoldId) {
        new File(String.format(SEATHOLD_PATH, seatHoldId)).delete();
    }

    public List<Seat> verifyIfSeatHoldExpired() throws IOException {
        File folder = new File("src/main/resources/data/");
        File[] filesList = folder.listFiles(pathname -> pathname.getName().contains("seathold"));
        List<File> expiredHolds = new ArrayList<>();
        List<Seat> seatList = new ArrayList<>();

        for (File file : filesList) {
            SeatHold seatHold = getSeatHold(file);
            if (seatHold.getHoldEndDate().before(new Date())) {
                seatList.addAll(seatHold.getSelectedSeats());
                expiredHolds.add(file);
            }
        }
        deleteExpiredHolds(expiredHolds);
        return seatList;
    }
}
