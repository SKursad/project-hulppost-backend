package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.ReplyDTO;
import nl.novi.hulppost.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    public ReplyController() {
    }

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }



    @PostMapping
    public ResponseEntity<ReplyDTO> saveReply(@RequestBody @Valid ReplyDTO replyDto) {
        return new ResponseEntity<>(replyService.saveReply(replyDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReplyDTO>> getAllReplies(@RequestParam Optional<Long> userId,
                                                        @RequestParam Optional<Long> requestId) {
        return new ResponseEntity<>(replyService.getAllReplies(userId, requestId), HttpStatus.OK);
    }

    @GetMapping({"/{replyId}"})
    public ResponseEntity<ReplyDTO> getReplyById(@PathVariable("replyId") Long replyId) {
        return replyService.getReplyById(replyId).map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.notFound().build());
    }

    @PutMapping("/{replyId}")
    @PreAuthorize("@methodLevelSecurityService.hasAuthToChangeReply(#replyId, principal)")
    public ResponseEntity<ReplyDTO> updateReply(@PathVariable(value = "replyId") Long replyId,
                                                @Valid @RequestBody ReplyDTO replyDto) {
        return replyService.getReplyById(replyId)
                .map(savedReply -> {
                    ReplyDTO updatedReply = replyService.updateReply(replyDto, replyId);
                    return new ResponseEntity<>(updatedReply, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/{replyId}"})
    @PreAuthorize("@methodLevelSecurityService.hasAuthToChangeReply(#replyId, principal)")
    public ResponseEntity<String> deleteComment(@PathVariable("replyId") Long replyId) {
        this.replyService.deleteReply(replyId);
        return new ResponseEntity<>("Antwoord succesvol verwijderd ", HttpStatus.OK);
    }

}
