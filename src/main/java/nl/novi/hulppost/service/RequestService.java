package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.RequestDTO;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    RequestDTO saveRequest(RequestDTO requestDto);

    List<RequestDTO> getAllRequests(Optional<Long> userId);

    Optional<RequestDTO> getRequestById(Long requestId);

    RequestDTO updateRequest(RequestDTO requestDto, Long requestId);

    void deleteRequest(Long requestId);

    void assignImageToRequest(Long fileId, Long requestId);

}
