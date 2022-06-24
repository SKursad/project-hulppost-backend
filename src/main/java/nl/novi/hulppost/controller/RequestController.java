package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.RequestDto;
import nl.novi.hulppost.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping({"/hulppost/requests"})
public class RequestController {

    @Autowired
    private RequestService requestService;


    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<RequestDto> saveRequest(@RequestBody @Valid RequestDto requestDto) {
        return new ResponseEntity<>(requestService.saveRequest(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RequestDto>> getAllRequests() {
        return new ResponseEntity<>(requestService.getAllRequests(), HttpStatus.OK);
    }

    @GetMapping({"/{requestId}"})
    public ResponseEntity<RequestDto> getRequestById(@PathVariable("requestId") Long requestId) {
        return requestService.getRequestById(requestId).map(ResponseEntity::ok).orElseGet(() ->
            ResponseEntity.notFound().build());
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<RequestDto> updateRequest(@PathVariable(value = "requestId") Long requestId,
                                              @Valid @RequestBody RequestDto requestDto) {
        return requestService.getRequestById(requestId)
                .map(savedRequest -> {
                    RequestDto updatedRequest = requestService.updateRequest(requestDto, requestId);
                    return new ResponseEntity<>(updatedRequest, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/{requestId}"})
    public ResponseEntity<String> deleteComment(@PathVariable("requestId") Long requestId) {
        this.requestService.deleteRequest(requestId);

        return new ResponseEntity<>("Aanvraag succesvol verwijderd ", HttpStatus.OK);
    }

}
