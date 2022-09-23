package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.ReplyDTO;
import nl.novi.hulppost.model.Reply;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.ReplyRepository;
import nl.novi.hulppost.repository.RequestRepository;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.service.serviceImpl.ReplyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ReplyServiceImpl underTest;

    private ReplyDTO replyDto;

    private Reply reply;

//    private User user;


    @BeforeEach
    public void setup() {
//        replyRepository = Mockito.mock(ReplyRepository.class);
//        underTest = new ReplyServiceImpl(replyRepository);



        reply = Reply.builder()
                .id(1L)
                .user(new User())
                .request(new Request())
                .text("Dit is een tekst om de servicelaag te testen")
                .build();

        replyDto = ReplyDTO.builder()
                .id(1L)
                .text("Dit is een tekst om de servicelaag te testen")
                .build();

//        user = User.builder()
//                .id(1L)
//                .username("User")
//                .build();



    }

    // JUnit test for saveReply method
    @DisplayName("JUnit test for saveReply method")
    @Test
    public void givenReplyObject_whenSaveReply_thenReturnReplyObject() {

        // given - precondition or setup
        lenient().when(replyRepository.findById(reply.getId()))
                .thenReturn(Optional.empty());

        given(replyRepository.save(reply)).willReturn(reply);


        System.out.println(replyRepository);
        System.out.println(underTest);

        // when -  action under test
        Reply newReply = replyRepository.save(reply);

        System.out.println(newReply);
        // then - verify the output
        assertThat(newReply).isNotNull();
    }

    // JUnit test for getAllReplies method
    @DisplayName("JUnit test for getAllReplies method")
    @Test
    public void givenRepliesList_whenGetAllReplies_thenReturnRepliesList() {

        // given
        Reply reply1 = Reply.builder()
                .id(2L)
                .user(new User())
                .request(new Request())
                .text( "Dit is een tekst om de servicelaag te testen")
                .build();


        given(replyRepository.findAll()).willReturn(List.of(reply, reply1));

        // when
        List<ReplyDTO> replyList = underTest.getAllReplies(Optional.empty(), Optional.empty());

        // then
        assertThat(replyList).isNotNull();
        assertThat(replyList.size()).isEqualTo(2);
    }

    // JUnit test for getAllReplies method
    @DisplayName("JUnit test for getAllReplies method (negative scenario)")
    @Test
    public void givenEmptyRepliesList_whenGetAllReplies_thenReturnEmptyRepliesList() {
        // given

        Reply reply1 = Reply.builder()
                .id(2L)
                .text("Dit is een tekst om de servicelaag te testen")
                .build();

        given(replyRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<ReplyDTO> replyList = underTest.getAllReplies(Optional.empty(), Optional.empty());

        // then
        assertThat(replyList).isEmpty();
        assertThat(replyList.size()).isEqualTo(0);
    }

    // JUnit test for getReplyById method
    @DisplayName("JUnit test for getReplyById method")
    @Test
    public void givenReplyId_whenGetReplyById_thenReturnReplyObject() {

        // given
        given(replyRepository.findById(1L)).willReturn(Optional.of(reply));

        // when
        ReplyDTO savedReply = underTest.getReplyById(reply.getId()).get();

        // then
        assertThat(savedReply).isNotNull();

    }

    //     JUnit test for updateReply method
    @DisplayName("JUnit test for updateReply method")
    @Test
    public void givenReplyObject_whenUpdateReply_thenReturnUpdatedReply() {

        //Setup
        reply.setId(1L);
        reply.setText("Dit is tekst om de servicelaag te testen");
        reply.setUser(new User());
        reply.setRequest(new Request());

        replyDto.setId(1L);
        replyDto.setText("Dit is een tekst om de servicelaag te testen");

        // given
        given(replyRepository.save(reply)).willReturn(reply);
        given(replyRepository.findById(1L)).willReturn(Optional.of(reply));

        // when
        ReplyDTO updatedReply = underTest.updateReply(replyDto, reply.getId());

        // then
        assertThat(updatedReply.getText()).isEqualTo("Dit is een tekst om de servicelaag te testen");
    }

    // JUnit test for deleteReply method
    @DisplayName("JUnit test for deleteReply method")
    @Test
    public void givenReplyId_whenDeleteReply_thenNothing() {

        // given
        Long replyId = 1L;
//        reply = Reply.builder()
//        .id(1L)
//        .text("Dit is een tekst om de servicelaag te testen")
//        .build();

        willDoNothing().given(replyRepository).deleteById(replyId);

        // when
        underTest.deleteReply(replyId);

        // then
        verify(replyRepository, times(1)).deleteById(replyId);
    }
}
