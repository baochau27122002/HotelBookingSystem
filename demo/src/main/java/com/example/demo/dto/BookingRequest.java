package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
  private String numberOfGuests;
  private String checkInDate;
  private String checkOutDate;
  private String checkInTime;
  private String checkOutTime;
}
