package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.dto.RequestDTO;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.FileAttachment;
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
    public RequestDTO saveRequest(RequestDTO requestDTO) {
        Request request = mapToEntity(requestDTO);
        if (request.getFileAttachment() != null) {
            FileAttachment inDB = attachmentRepository.findById(request.getFileAttachment().getId()).get();
            inDB.setRequest(request);
            request.setFileAttachment(inDB);
        }
        Request newRequest = requestRepository.save(request);
        return mapToDTO(newRequest);
    }

    @Override
    public List<RequestDTO> getAllRequests(Optional<Long> userId) {
        List<Request> requests;
        List<RequestDTO> requestDTOList = new ArrayList<>();
        if (userId.isPresent()){
            requests = requestRepository.findByUserId(userId.get());
        } else
            requests = requestRepository.findAll();

        for (Request request : requests) {
            RequestDTO requestDTO = mapToDTO(request);
            requestDTOList.add(requestDTO);
        }

        return requestDTOList;
    }

    @Override
    public Optional<RequestDTO> getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag niet gevonden"));

        return Optional.of(mapToDTO(request));
    }

    @Override
    public RequestDTO updateRequest(RequestDTO requestDTO, Long requestId) {
        Request inDB = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag", "id", requestId));

        inDB.setTitle(requestDTO.getTitle());
        inDB.setTypeRequest(requestDTO.getTypeRequest());
        inDB.setContent(requestDTO.getContent());
        Request updatedRequest = requestRepository.save(inDB);

        return mapToDTO(updatedRequest);
    }

    @Override
    public void deleteRequest(Long requestId) {
        Request request = requestRepository.getById(requestId);
        if (request.getFileAttachment() != null) {
            fileService.deleteAttachmentImage(request.getFileAttachment().getName());
        }
        requestRepository.deleteById(requestId);
    }

    public void assignImageToRequest(Long fileId, Long requestId) {
        Request request = requestRepository.getById(requestId);

        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        Optional<FileAttachment> attachment = attachmentRepository.findById(fileId);

        if (optionalRequest.isPresent() && attachment.isPresent()) {
            if (request.getFileAttachment() != null) {
                fileService.deleteAttachmentImage(request.getFileAttachment().getName());
            }
            FileAttachment image = attachment.get();
            Request requestNewImage = optionalRequest.get();
            request.setFileAttachment(image);
            requestRepository.save(requestNewImage);
        }
    }

    @Override
    public void deleteAttachment(Long requestId) {
        Request inDB = requestRepository.getById(requestId);
        try {
            fileService.deleteAttachmentImage(inDB.getFileAttachment().getName());
            inDB.setFileAttachment(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Request deletedAttachment = requestRepository.save(inDB);
        mapToDTO(deletedAttachment);
    }


    private RequestDTO mapToDTO(Request request) {

        RequestDTO requestDTO = new RequestDTO();

        requestDTO.setId(request.getId());
        requestDTO.setTimestamp(request.getTimestamp());
        requestDTO.setUserId(request.getUser().getId());
        requestDTO.setFileAttachment(request.getFileAttachment());
        requestDTO.setTitle(request.getTitle());
        requestDTO.setContent(request.getContent());
        requestDTO.setTypeRequest(request.getTypeRequest());

        return requestDTO;
    }

    private Request mapToEntity(RequestDTO requestDTO) {

        Request request = new Request();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        request.setId(requestDTO.getId());
        request.setTimestamp(requestDTO.getTimestamp());
        request.setFileAttachment(requestDTO.getFileAttachment());
        request.setUser(userRepository.findByUsername(username));
        request.setTitle(requestDTO.getTitle());
        request.setContent(requestDTO.getContent());
        request.setTypeRequest(requestDTO.getTypeRequest());

        return request;
    }

}
