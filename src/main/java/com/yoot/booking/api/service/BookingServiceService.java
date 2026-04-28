package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.bookingservice.*;
import com.yoot.booking.api.dto.Common.*;

import java.util.List;

public interface BookingServiceService {

    ResultListDTO<BookingServiceResponseDTO> getAll(PagingRequestDTO request);

    ResultDTO<BookingServiceResponseDTO> getById(Long id);

    ResultDTO<BookingServiceResponseDTO> create(BookingServiceCreateDTO request);

    ResultDTO<BookingServiceResponseDTO> update(Long id, BookingServiceUpdateDTO request);

    ResultNoDataDTO delete(Long id);
}