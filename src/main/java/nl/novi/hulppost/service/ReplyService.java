package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.ReplyDTO;

import java.util.List;
import java.util.Optional;

public interface ReplyService {

    ReplyDTO saveReply(ReplyDTO replyDto);

//    List<ReplyDto> getAllReplies();

    List<ReplyDTO> getAllReplies(Optional<Long> userId, Optional<Long> requestId);

    Optional<ReplyDTO> getReplyById(Long replyId);

    ReplyDTO updateReply(ReplyDTO replyDto, Long replyId);

    void deleteReply(Long replyId);

}
