package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.ReplyDto;
import nl.novi.hulppost.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/hulppost/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping
    public ResponseEntity<ReplyDto> saveReply(@RequestBody @Valid ReplyDto replyDto) {
        return new ResponseEntity<>(replyService.saveReply(replyDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReplyDto>> getAllReplies() {
        return new ResponseEntity<>(replyService.getAllReplies(), HttpStatus.OK);
    }

    @GetMapping({"/{replyId}"})
    public ResponseEntity<ReplyDto> getReplyById(@PathVariable("replyId") Long replyId) {
        return replyService.getReplyById(replyId).map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.notFound().build());
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<ReplyDto> updateReply(@PathVariable(value = "replyId") Long replyId,
                                                    @Valid @RequestBody ReplyDto replyDto) {
        return replyService.getReplyById(replyId)
                .map(savedReply -> {
                    ReplyDto updatedReply = replyService.updateReply(replyDto, replyId);
                    return new ResponseEntity<>(updatedReply, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/{replyId}"})
    public ResponseEntity<String> deleteComment(@PathVariable("replyId") Long replyId) {
        this.replyService.deleteReply(replyId);
        return new ResponseEntity<>("Aanvraag succesvol verwijderd ", HttpStatus.OK);
    }
}
