package com.example.demo.service;

import com.example.demo.dto.BookingData;
import com.example.demo.dto.BookingRequest;
import com.example.demo.dto.BookingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HotelBookingSystemTest {

  // VALID INPUT DATA
  private final String VALID_NUMBER_OF_GUESTS = "1";
  private final String VALID_CHECK_IN_DATE = "10/10/2024";
  private final String VALID_CHECK_OUT_DATE = "10/10/2025";
  private final String VALID_CHECK_IN_TIME_MIN = "13:00";
  private final String VALID_CHECK_IN_TIME_MAX = "19:00";

  private final String VALID_CHECK_OUT_TIME_MIN = "12:00";
  private final String VALID_CHECK_OUT_TIME_MAX = "18:00";


  // INVALID INPUT DATA
  private final String NUMBER_OF_GUESTS_ZERO = "0";
  private final String NUMBER_OF_GUESTS_NEGATIVE = "-10";
  private final String NUMBER_OF_GUESTS_NAN = "TEN";
  private final String INVALID_CHECK_IN_DATE = "Jan 1st, 2022";
  private final String INVALID_CHECK_OUT_DATE = "Jan 3rd, 2022";
  private final String INVALID_CHECK_IN_TIME = "8:00 AM";
  private final String INVALID_CHECK_OUT_TIME = "12:00 PM";
  private final String DATE_IN_THE_PAST = "01/01/2000";
  private final String OUT_OF_CHECK_IN_TIME = "06:00";
  private final String OUT_OF_CHECK_OUT_TIME = "20:00";
  @InjectMocks
  private HotelBookingSystem hotelBookingSystem;

  @Test
  @DisplayName("Booking successfully")
  void testBookingSuccess() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();
    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);
    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.OK.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Room is booked successfully. We will contact you soon to confirm the room number.", response.getBody().getMessage());
    BookingData bookingData = response.getBody().getData();
    assertNotNull(bookingData);
    assertEquals(Integer.parseInt(VALID_NUMBER_OF_GUESTS), bookingData.getNumberOfGuests());
    assertEquals(VALID_CHECK_IN_DATE, bookingData.getCheckInDate());
    assertEquals(VALID_CHECK_OUT_DATE, bookingData.getCheckOutDate());
    assertEquals(VALID_CHECK_IN_TIME_MIN, bookingData.getCheckInTime());
    assertEquals(VALID_CHECK_OUT_TIME_MIN, bookingData.getCheckOutTime());

  }

  @Test
  @DisplayName("Booking when number of guests is zero")
  void testBookingNumberOfGuestsZero() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(NUMBER_OF_GUESTS_ZERO)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Number of guests must be a positive number.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when number of guests is negative")
  void testBookingNumberOfGuestsNegative() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(NUMBER_OF_GUESTS_NEGATIVE)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Number of guests must be a positive number.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when number of guests is not a number")
  void testBookingNumberOfGuestsNaN() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(NUMBER_OF_GUESTS_NAN)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Number of guests must be a positive number.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-in date is invalid")
  void testBookingInvalidCheckInDate() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(INVALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Invalid date format. Please enter date in dd/mm/yyyy format.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-in date is before current date")
  void testBookingCheckInDateBeforeCurrentDate() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(DATE_IN_THE_PAST)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Check-in date must be after the current date.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-out date is invalid")
  void testBookingInvalidCheckOutDate() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(INVALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Invalid date format. Please enter date in dd/mm/yyyy format.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-out date is before current date")
  void testBookingCheckOutDateBeforeCurrentDate() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(DATE_IN_THE_PAST)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Check-out date must be after the current date.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-out date is before check-in date")
  void testBookingCheckOutDateBeforeCheckInDate() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_OUT_DATE)
            .checkOutDate(VALID_CHECK_IN_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Check-in date must be before check-out date.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-in time is invalid")
  void testBookingInvalidCheckInTime() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(INVALID_CHECK_IN_TIME)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Invalid time format. Please enter time in HH:mm format.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-in time is out of range")
  void testBookingCheckInTimeOutOfRange() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(OUT_OF_CHECK_IN_TIME)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Check-in time must be 13:00 or 19:00.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-out time is invalid")
  void testBookingInvalidCheckOutTime() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(INVALID_CHECK_OUT_TIME)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Invalid time format. Please enter time in HH:mm format.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-out time is out of range")
  void testBookingCheckOutTimeOutOfRange() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(OUT_OF_CHECK_OUT_TIME)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Check-out time must be 12:00 or 18:00.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking one day when check-in time is after check-out time")
  void testBookingCheckInTimeAfterCheckOutTime() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_IN_DATE) // same date
            .checkInTime(VALID_CHECK_IN_TIME_MAX) // check-in time is after check-out time
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Check-in time must be before check-out time.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }
  @Test
  @DisplayName("Booking when number of guests is missing")
  void testBookingNumberOfGuestsMissing() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Number of guests is required.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-in date is missing")
  void testBookingCheckInDateMissing() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Check-in date is required.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-out date is missing")
  void testBookingCheckOutDateMissing() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Check-out date is required.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-in time is missing")
  void testBookingCheckInTimeMissing() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkOutTime(VALID_CHECK_OUT_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Check-in time is required.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  @DisplayName("Booking when check-out time is missing")
  void testBookingCheckOutTimeMissing() {
    // Given
    BookingRequest bookingRequest = BookingRequest
            .builder()
            .numberOfGuests(VALID_NUMBER_OF_GUESTS)
            .checkInDate(VALID_CHECK_IN_DATE)
            .checkOutDate(VALID_CHECK_OUT_DATE)
            .checkInTime(VALID_CHECK_IN_TIME_MIN)
            .build();

    // Call the method under test
    ResponseEntity<BookingResponse> response = hotelBookingSystem.processBooking(bookingRequest);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Check-out time is required.", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }
}
