package com.ticketservice.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IdGenerator {
    private static int seatHoldId = 0;
    private static int confirmationCodeLength = 5;
    private static List<String> generatedConfirmationCodes = new ArrayList<>();

    public static List<String> getGeneratedConfirmationCodes() {
        return generatedConfirmationCodes;
    }

    public static void addGeneratedConfirmationCodes(String generatedConfirmationCodes) {
        IdGenerator.generatedConfirmationCodes.add(generatedConfirmationCodes);
    }

    public static int generateSeatHoldId() {
        return seatHoldId++;
    }

    public static String generateConfirmationCode() {
        StringBuilder stB = new StringBuilder();
        Random random = new Random();
        int min = 'A';
        int max = 'Z';

        for (int i = 0; i < confirmationCodeLength; i++) {
            int randomNum = random.nextInt(max - min) + min;
            stB.append((char) randomNum);
        }
        return stB.toString();
    }
}
