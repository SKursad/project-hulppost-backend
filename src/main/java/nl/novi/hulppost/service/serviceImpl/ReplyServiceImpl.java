package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.dto.ReplyDto;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Reply;
import nl.novi.hulppost.repository.ReplyRepository;
import nl.novi.hulppost.service.ReplyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ModelMapper mapper;

    public ReplyServiceImpl() {
    }

    public ReplyServiceImpl(ReplyRepository replyRepository, ModelMapper mapper) {
        this.replyRepository = replyRepository;
        this.mapper = mapper;
    }

    @Override
    public ReplyDto saveReply(ReplyDto replyDto) {
        Reply reply = mapToEntity(replyDto);
        Reply newReply = replyRepository.save(reply);
        return mapToDto(newReply);
    }

    @Override
    public List<ReplyDto> getAllReplies() {
        List<Reply> replyList = replyRepository.findAll();
        List<ReplyDto> replyDtoList = new ArrayList<>();

        for (Reply reply : replyList) {
            ReplyDto replyDto = mapToDto(reply);
            replyDtoList.add(replyDto);
        }

        return replyDtoList;
    }

    @Override
    public Optional<ReplyDto> getReplyById(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aanvraag niet gevonden"));

        return Optional.of(mapToDto(reply));
    }

    @Override
    public ReplyDto updateReply(ReplyDto replyDto, Long replyId) {
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

    private ReplyDto mapToDto(Reply reply) {
//        ReplyDto replyDto = new ReplyDto();
//
//        replyDto.setId(reply.getId());
//        replyDto.setText(reply.getText());
//
//        return replyDto;
        return mapper.map(reply, ReplyDto.class);
    }

    private Reply mapToEntity(ReplyDto replyDto) {
//        Reply reply = new Reply();

//        reply.setId(replyDto.getId());
//        reply.setText(replyDto.getText());
//
//        return reply;

        return mapper.map(replyDto, Reply.class);
    }
}
