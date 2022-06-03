package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.dto.RequestDto;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.repository.RequestRepository;
import nl.novi.hulppost.service.RequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ModelMapper mapper;

    public RequestServiceImpl() {
    }

    public RequestServiceImpl(RequestRepository requestRepository, ModelMapper mapper) {
        this.requestRepository = requestRepository;
        this.mapper = mapper;
    }

    public RequestDto saveRequest(RequestDto requestDto) {
        Request request = mapToEntity(requestDto);
        Request newRequest = requestRepository.save(request);
        return mapToDto(newRequest);
    }

    public List<RequestDto> getAllRequests() {
        List<Request> requestList = requestRepository.findAll();
        List<RequestDto> requestDtoList = new ArrayList<>();

        for (Request request : requestList) {
            RequestDto requestDto = mapToDto(request);
            requestDtoList.add(requestDto);
        }

        return requestDtoList;
    }

    public Optional<RequestDto> getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag niet gevonden"));

        return Optional.of(mapToDto(request));

    }

    public RequestDto updateRequest(RequestDto requestDto, Long requestId) {

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag", "id", requestId));

        request.setTitle(requestDto.getTitle());
        request.setTypeRequest(requestDto.getTypeRequest());
        request.setContent(requestDto.getContent());
        Request updatedRequest = requestRepository.save(request);

        return mapToDto(updatedRequest);

    }

    public void deleteRequest(Long requestId) {
        requestRepository.deleteById(requestId);
    }

    private RequestDto mapToDto(Request request) {

//        RequestDto requestDto = new RequestDto();
//
//        requestDto.setId(request.getId());
//        requestDto.setTitle(request.getTitle());
//        requestDto.setTypeRequest(request.getTypeRequest());
//        requestDto.setContent(request.getContent());
//
//        return requestDto;
        return mapper.map(request, RequestDto.class);

    }

    private Request mapToEntity(RequestDto requestDto) {

//        Request request = new Request();

//        request.setId(requestDto.getId());
//        request.setTitle(requestDto.getTitle());
//        request.setTypeRequest(requestDto.getTypeRequest());
//        request.setContent(requestDto.getContent());
//
//        return request;

        return mapper.map(requestDto, Request.class);

    }

}
