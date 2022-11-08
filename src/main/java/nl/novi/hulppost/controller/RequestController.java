package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.RequestDTO;
import nl.novi.hulppost.model.FileAttachment;
import nl.novi.hulppost.service.FileService;
import nl.novi.hulppost.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/api/v1/requests"})
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private FileService fileService;

    public RequestController(RequestService requestService, FileService fileService) {
        this.requestService = requestService;
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<RequestDTO> saveRequest(@RequestBody @Valid RequestDTO requestDto) {
        return new ResponseEntity<>(requestService.saveRequest(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAllRequests(@RequestParam Optional<Long> userId) {
        return new ResponseEntity<>(requestService.getAllRequests(userId), HttpStatus.OK);
    }

    @GetMapping({"/{requestId}"})
    public ResponseEntity<RequestDTO> getRequestById(@PathVariable("requestId") Long requestId) {
        return requestService.getRequestById(requestId).map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.notFound().build());
    }

    @PutMapping("/{requestId}")
    @PreAuthorize("@methodLevelSecurityService.hasAuthToChangeRequest(#requestId, principal)")
    public ResponseEntity<RequestDTO> updateRequest(@PathVariable(value = "requestId") Long requestId,
                                                    @Valid @RequestBody RequestDTO requestDto) {
        return requestService.getRequestById(requestId)
                .map(savedRequest -> {
                    RequestDTO updatedRequest = requestService.updateRequest(requestDto, requestId);
                    return new ResponseEntity<>(updatedRequest, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping({"/{requestId}"})
    @PreAuthorize("@methodLevelSecurityService.hasAuthToChangeRequest(#requestId, principal)")
    public ResponseEntity<String> deleteRequest(@PathVariable("requestId") Long requestId) {
        requestService.deleteRequest(requestId);

        return new ResponseEntity<>("Aanvraag succesvol verwijderd", HttpStatus.OK);
    }

    @DeleteMapping({"/{requestId}/deleteImage"})
    @PreAuthorize("@methodLevelSecurityService.hasAuthToChangeRequest(#requestId, principal)")
    public ResponseEntity<String> deleteAttachment(@PathVariable("requestId") Long requestId) {
        requestService.deleteAttachment(requestId);

        return new ResponseEntity<>("Bijlage succesvol verwijderd ", HttpStatus.OK);
    }

    @PostMapping("/{requestId}/image")
    @PreAuthorize("@methodLevelSecurityService.hasAuthToChangeRequest(#requestId, principal)")
    public ResponseEntity<String> assignImageToRequest(@PathVariable("requestId") Long requestId,
                                                       @RequestBody(required = false) MultipartFile file) throws Exception {

        FileAttachment attachment = fileService.saveAttachment(file, requestId);
        requestService.assignImageToRequest(attachment.getId(), requestId);
        return new ResponseEntity<>("Afbeelding succesvol geüpload", HttpStatus.CREATED);
    }

}
