package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.category.*;

public interface ServiceCategoryService {

    ResultDTO<ServiceCategoryResponseDTO> create(ServiceCategoryCreateDTO dto);

    ResultDTO<ServiceCategoryResponseDTO> update(Long id, ServiceCategoryUpdateDTO dto);

    ResultNoDataDTO delete(Long id);

    ResultDTO<ServiceCategoryResponseDTO> getById(Long id);

    ResultListDTO<ServiceCategoryResponseDTO> getAll(PagingRequestDTO request);
}