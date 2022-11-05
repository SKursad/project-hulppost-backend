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
    public ReplyDTO saveReply(ReplyDTO replyDTO) {
        Reply reply = mapToEntity(replyDTO);
        Reply newReply = replyRepository.save(reply);

        return mapToDTO(newReply);
    }

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
            ReplyDTO replyDTO = mapToDTO(reply);
            replyDTOList.add(replyDTO);
        }
        return replyDTOList;
    }

    @Override
    public Optional<ReplyDTO> getReplyById(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag niet gevonden"));

        return Optional.of(mapToDTO(reply));
    }

    @Override
    public ReplyDTO updateReply(ReplyDTO replyDTO, Long replyId) {
        Reply inDB = replyRepository.findById(replyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag", "id", replyId));

        inDB.setText(replyDTO.getText());
        Reply updatedReply = replyRepository.save(inDB);

        return mapToDTO(updatedReply);
    }

    @Override
    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }

    private ReplyDTO mapToDTO(Reply reply) {
        ReplyDTO replyDTO = new ReplyDTO();

        replyDTO.setId(reply.getId());
        replyDTO.setUserId(reply.getUser().getId());
        replyDTO.setRequestId(reply.getRequest().getId());
        replyDTO.setText(reply.getText());
        replyDTO.setTimestamp(reply.getTimestamp());

        return replyDTO;
    }

    private Reply mapToEntity(ReplyDTO replyDTO) {

        Reply reply = new Reply();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        reply.setId(replyDTO.getId());
        reply.setUser(userRepository.findByUsername(username));
        Long requestId = replyDTO.getRequestId();
        reply.setRequest(requestRepository.getById(requestId));
        reply.setTimestamp(replyDTO.getTimestamp());
        reply.setText(replyDTO.getText());

        return reply;

    }

}
