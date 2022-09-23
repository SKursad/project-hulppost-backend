package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.dto.ReplyDTO;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Reply;
import nl.novi.hulppost.repository.ReplyRepository;
import nl.novi.hulppost.repository.RequestRepository;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    public ReplyServiceImpl() {
    }

    public ReplyServiceImpl(ReplyRepository replyRepository, UserRepository userRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public ReplyDTO saveReply(ReplyDTO replyDto) {
        Reply reply = mapToEntity(replyDto);
        Reply newReply = replyRepository.save(reply);
        return mapToDto(newReply);
    }

//    @Override
//    public List<ReplyDto> getAllReplies() {
//        List<Reply> replyList = replyRepository.findAll();
//        List<ReplyDto> replyDtoList = new ArrayList<>();
//
//        for (Reply reply : replyList) {
//            ReplyDto replyDto = mapToDto(reply);
//            replyDtoList.add(replyDto);
//        }
//
//        return replyDtoList;
//    }

    @Override
    public List<ReplyDTO> getAllReplies(Optional<Long> userId, Optional<Long> requestId) {
        List<Reply> replies;
        List<ReplyDTO> replyDTOList = new ArrayList<>();
        if (userId.isPresent() && requestId.isPresent()) {
            replies = replyRepository.findByUserIdAndRequestId(userId.get(), requestId.get());
        } else if (userId.isPresent()) {
            replies = replyRepository.findByUserId(userId.get());
        } else if (requestId.isPresent()) {
            replies = replyRepository.findByRequestId(requestId.get());
        } else
            replies = replyRepository.findAll();

        for (Reply reply : replies) {
            ReplyDTO replyDto = mapToDto(reply);
            replyDTOList.add(replyDto);
        }
        return replyDTOList;
    }

    @Override
    public Optional<ReplyDTO> getReplyById(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag niet gevonden"));

        return Optional.of(mapToDto(reply));
    }

    @Override
    public ReplyDTO updateReply(ReplyDTO replyDto, Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag", "id", replyId));

        reply.setText(replyDto.getText());
        Reply updatedReply = replyRepository.save(reply);

        return mapToDto(updatedReply);
    }

    @Override
    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }

    private ReplyDTO mapToDto(Reply reply) {
        ReplyDTO replyDto = new ReplyDTO();

        replyDto.setId(reply.getId());
        replyDto.setUserId(reply.getUser().getId());
        replyDto.setRequestId(reply.getRequest().getId());
        replyDto.setText(reply.getText());

        return replyDto;
    }

    private Reply mapToEntity(ReplyDTO replyDto) {

        Reply reply = new Reply();

        reply.setId(replyDto.getId());

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        reply.setUser(userRepository.findByUsername(username));

        Long requestId = replyDto.getRequestId();
        reply.setRequest(requestRepository.getById(requestId));

        reply.setText(replyDto.getText());

        return reply;

    }

}
