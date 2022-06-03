package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.ReplyDto;

import java.util.List;
import java.util.Optional;

public interface ReplyService {

    ReplyDto saveReply(ReplyDto replyDto);

    List<ReplyDto> getAllReplies();

    Optional<ReplyDto> getReplyById(Long replyId);

    ReplyDto updateReply(ReplyDto replyDto, Long replyId);

    void deleteReply(Long replyId);
}
