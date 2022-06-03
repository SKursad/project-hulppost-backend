package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.RequestDto;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    RequestDto saveRequest(RequestDto requestDto);

    List<RequestDto> getAllRequests();

    Optional<RequestDto> getRequestById(Long requestId);

    RequestDto updateRequest(RequestDto requestDto, Long requestId);

    void deleteRequest(Long requestId);
}
