package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.dto.RequestDto;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Attachment;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.repository.AttachmentRepository;
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
    private AttachmentRepository attachmentRepository;

    @Autowired
    private ModelMapper mapper;

    public RequestServiceImpl() {
    }

    public RequestServiceImpl(RequestRepository requestRepository, ModelMapper mapper) {
        this.requestRepository = requestRepository;
        this.mapper = mapper;
    }

    public RequestServiceImpl(RequestRepository requestRepository, ModelMapper mapper, AttachmentRepository attachment) {
        this.requestRepository = requestRepository;
        this.mapper = mapper;
        this.attachmentRepository = attachment;
    }

    @Override
    public RequestDto saveRequest(RequestDto requestDto) {
        Request request = mapToEntity(requestDto);
        if (request.getAttachment() != null) {
            Attachment inDB = attachmentRepository.findById(request.getAttachment().getId()).get();
            inDB.setRequest(request);
            request.setAttachment(inDB);
        }
        Request newRequest = requestRepository.save(request);
        return mapToDto(newRequest);
    }

    @Override
    public List<RequestDto> getAllRequests() {
        List<Request> requestList = requestRepository.findAll();
        List<RequestDto> requestDtoList = new ArrayList();

        for (Request request : requestList) {
            RequestDto requestDto = mapToDto(request);
            requestDtoList.add(requestDto);
        }

        return requestDtoList;
    }

    @Override
    public Optional<RequestDto> getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag niet gevonden"));

        return Optional.of(mapToDto(request));
    }

    @Override
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

    @Override
    public void deleteRequest(Long requestId) {
        requestRepository.deleteById(requestId);
    }

    private RequestDto mapToDto(Request request) {
        return mapper.map(request, RequestDto.class);
    }

    private Request mapToEntity(RequestDto requestDto) {
        return mapper.map(requestDto, Request.class);
    }

}
