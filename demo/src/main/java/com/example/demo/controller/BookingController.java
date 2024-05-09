package com.example.demo.controller;

import com.example.demo.dto.BookingRequest;
import com.example.demo.dto.BookingResponse;
import com.example.demo.service.HotelBookingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hotel")
public class BookingController {

  @Autowired
  private HotelBookingSystem hotelBookingSystem;

  @PostMapping("/book")
  public ResponseEntity<BookingResponse> book(@RequestBody BookingRequest bookingRequest) {
    return hotelBookingSystem.processBooking(bookingRequest);
  }

}


