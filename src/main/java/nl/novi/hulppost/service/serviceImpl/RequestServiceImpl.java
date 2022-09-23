package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.dto.RequestDTO;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Attachment;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.repository.AttachmentRepository;
import nl.novi.hulppost.repository.RequestRepository;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.service.FileService;
import nl.novi.hulppost.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private UserRepository userRepository;

    @Autowired
    FileService fileService;

    public RequestServiceImpl() {
    }

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    public RequestServiceImpl(AttachmentRepository attachment, FileService fileService) {
        this.attachmentRepository = attachment;
        this.fileService = fileService;
    }



    @Override
    public RequestDTO saveRequest(RequestDTO requestDto) {
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
    public List<RequestDTO> getAllRequests(Optional<Long> userId) {
        List<Request> requests;
        List<RequestDTO> requestDTOList = new ArrayList();
        if (userId.isPresent()) {
            requests = requestRepository.findByUserId(userId.get());
        } else
            requests = requestRepository.findAll();

        for (Request request : requests) {
            RequestDTO requestDto = mapToDto(request);
            requestDTOList.add(requestDto);
        }

        return requestDTOList;
    }

    @Override
    public Optional<RequestDTO> getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag niet gevonden"));

        return Optional.of(mapToDto(request));
    }

    @Override
    public RequestDTO updateRequest(RequestDTO requestDto, Long requestId) {
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
        Request request = requestRepository.getById(requestId);
        if(request.getAttachment() != null) {
            fileService.deleteAttachmentImage(request.getAttachment().getName());
        }
        requestRepository.deleteById(requestId);
    }

    public void assignImageToRequest(Long fileId, Long requestId) {

        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        Optional<Attachment> attachment = attachmentRepository.findById(fileId);

        if (optionalRequest.isPresent() && attachment.isPresent()) {
            Attachment image = attachment.get();
            Request request = optionalRequest.get();
            request.setAttachment(image);
            requestRepository.save(request);
        }
    }


        private RequestDTO mapToDto(Request request) {

        RequestDTO requestDto = new RequestDTO();

        requestDto.setId(request.getId());
        requestDto.setUserId(request.getUser().getId());
//        requestDto.setAttachment(attachmentRepository.getById(request.getAttachment().getId()));
//        attachmentRepository.getById(requestDto.getAttachment().getId());
        requestDto.setAttachment(request.getAttachment());
        requestDto.setTitle(request.getTitle());
        requestDto.setContent(request.getContent());
        requestDto.setTypeRequest(request.getTypeRequest());

        return requestDto;
    }

    private Request mapToEntity(RequestDTO requestDto) {

        Request request = new Request();

        request.setId(requestDto.getId());
//        if(request.getAttachment() != null) {
//            request.setAttachment(requestDto.getAttachment());
//        }
        request.setAttachment(requestDto.getAttachment());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        request.setUser(userRepository.findByUsername(username));
        request.setTitle(requestDto.getTitle());
        request.setContent(requestDto.getContent());
        request.setTypeRequest(requestDto.getTypeRequest());

        return request;
    }

}
