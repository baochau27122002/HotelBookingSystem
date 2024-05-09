package com.example.demo.service;

import com.example.demo.dto.BookingData;
import com.example.demo.dto.BookingRequest;
import com.example.demo.dto.BookingResponse;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class HotelBookingSystem {


    public ResponseEntity<BookingResponse> processBooking(BookingRequest bookingRequest) {
        ResponseEntity<BookingResponse> response = null;
        // Input number of guests
        int numberOfGuests = 0;
        LocalDate checkOutDate = null;
        LocalDate checkInDate = null;
        LocalTime checkInTime = null;
        LocalTime checkOutTime = null;

        // check if the number of guests is entered correctly
        String numberOfGuestStrings = bookingRequest.getNumberOfGuests();
        try {
            if (numberOfGuestStrings == null) {
                return badRequest("Number of guests is required.");
            }

            numberOfGuests = Integer.parseInt(numberOfGuestStrings);
            if (numberOfGuests <= 0) {
                return badRequest("Number of guests must be a positive number.");
            }

        } catch (NumberFormatException e) {
            return badRequest("Number of guests must be a positive number.");
        }

        // Input check-in date
        String checkInDateStrings = bookingRequest.getCheckInDate();
        try {
            if (checkInDateStrings == null) {
                return badRequest("Check-in date is required.");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            checkInDate = LocalDate.parse(checkInDateStrings, formatter);
            if (checkInDate.isBefore(LocalDate.now())) {
                return badRequest("Check-in date must be after the current date.");
            }
        } catch (DateTimeParseException e) {
            return badRequest("Invalid date format. Please enter date in dd/mm/yyyy format.");
        }

        // Input check-out date
        String checkOutDateStrings = bookingRequest.getCheckOutDate();
        try {
            if (checkOutDateStrings == null) {
                return badRequest("Check-out date is required.");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            checkOutDate = LocalDate.parse(checkOutDateStrings, formatter);
            if (checkOutDate.isBefore(LocalDate.now())) {
                return badRequest("Check-out date must be after the current date.");
            }
        } catch (DateTimeParseException e) {
            return badRequest("Invalid date format. Please enter date in dd/mm/yyyy format.");
        }

        // Input check-in time
        String checkInTimeStrings = bookingRequest.getCheckInTime();
        try {
            if (checkInTimeStrings == null) {
                return badRequest("Check-in time is required.");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            checkInTime = LocalTime.parse(checkInTimeStrings, formatter);
            if (!checkInTime.equals(LocalTime.of(13, 0)) && !checkInTime.equals(LocalTime.of(19, 0))) {
                return badRequest("Check-in time must be 13:00 or 19:00.");
            }
        } catch (DateTimeParseException e) {
            return badRequest("Invalid time format. Please enter time in HH:mm format.");
        }

        // Input check-out time
        String checkOutTimeStrings = bookingRequest.getCheckOutTime();
        try {
            if (checkOutTimeStrings == null) {
                return badRequest("Check-out time is required.");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            checkOutTime = LocalTime.parse(checkOutTimeStrings, formatter);
            if (!checkOutTime.equals(LocalTime.of(12, 0)) && !checkOutTime.equals(LocalTime.of(18, 0))) {
                return badRequest("Check-out time must be 12:00 or 18:00.");
            }
        } catch (DateTimeParseException e) {
            return badRequest("Invalid time format. Please enter time in HH:mm format.");
        }

        // check ìf check-in date is before check-out date
        if (checkInDate.isAfter(checkOutDate)) {
                return badRequest("Check-in date must be before check-out date.");
        }

        if (checkInDate.isEqual(checkOutDate) && checkInTime.isAfter(checkOutTime)) {
            return badRequest("Check-in time must be before check-out time.");
        }

        // Display success message if all information is entered correctly
        return success("Room is booked successfully. We will contact you soon to confirm the room number.", bookingRequest);

    }

    private ResponseEntity<BookingResponse> badRequest(String message) {
        BookingResponse bookingResponse = BookingResponse.builder()
                .message(message)
                .build();

        return ResponseEntity.badRequest().body(bookingResponse);


    }

    private ResponseEntity<BookingResponse> success(String message, BookingRequest request) {

        String numberOfGuests = request.getNumberOfGuests();

        int numberOfGuestsInt = Integer.parseInt(numberOfGuests);
        BookingData bookingData = BookingData.builder()
                .numberOfGuests(numberOfGuestsInt)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .checkInTime(request.getCheckInTime())
                .checkOutTime(request.getCheckOutTime())
                .build();

        BookingResponse bookingResponse = BookingResponse.builder()
                .message(message)
                .data(bookingData)
                .build();

        return ResponseEntity.ok().body(bookingResponse);
    }
}