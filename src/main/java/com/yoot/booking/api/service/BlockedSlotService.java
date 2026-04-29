package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.blockedslot.*;

public interface BlockedSlotService {

    ResultDTO<BlockedSlotResponseDTO> create(BlockedSlotCreateDTO dto);

    ResultListDTO<BlockedSlotResponseDTO> getAll(PagingRequestDTO request);

    ResultNoDataDTO delete(Long id);
}