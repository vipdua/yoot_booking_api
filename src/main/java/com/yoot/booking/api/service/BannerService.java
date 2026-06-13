package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.banner.*;
import com.yoot.booking.api.entity.BannerPosition;

public interface BannerService {

    ResultListDTO<BannerResponseDTO> getByPosition(BannerPosition position);

    ResultDTO<BannerResponseDTO> create(BannerCreateDTO dto);

    ResultDTO<BannerResponseDTO> update(Long id, BannerUpdateDTO dto);

    ResultNoDataDTO delete(Long id);
}