package nl.novi.hulppost.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.model.Reply;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.model.enums.TypeRequest;
import nl.novi.hulppost.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ReplyControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void givenReplyObject_whenCreateReply_thenReturnSavedReply() throws Exception {

        // given - precondition or setup

        Reply reply = Reply.builder()
                .id(1L)
                .text("Hallo, dit is een reactie op een aanvraag")
                .build();

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/hulppost/reacties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reply)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text",
                        is(reply.getText())));

    }

    @Test
    public void givenListOfReplies_whenGetAllReplies_thenReturnRepliesList() throws Exception {

        // given
        List<Reply> listOfReplies = new ArrayList();
        listOfReplies.add(Reply.builder()
                .text("Dit is een reactie om te testen")
                .build());

        listOfReplies.add(Reply.builder()
                .text("Dit is een tweede tekst voor om te testen")
                .build());
        replyRepository.saveAll(listOfReplies);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/reacties"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfReplies.size())));
    }

    @Test
    public void givenReplyId_whenGetReplyById_thenReturnReplyObject() throws Exception {

        // given
        Reply reply = Reply.builder()
                .text("De test voor id ophalen van een reactie en dan terug weergeven als object")
                .build();
        replyRepository.save(reply);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/reacties/{replyId}", reply.getId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text",
                        is(reply.getText())));

    }

    @Test
    public void givenInvalidReplyId_whenGetReplyById_thenReturnEmpty() throws Exception {

        // given
        Long replyId = 2L;
        Reply reply = Reply.builder()
                .text("Test tekst voor reactie")
                .build();
        replyRepository.save(reply);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/reacties/{replyId}", replyId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void givenUpdatedReply_whenUpdateReply_thenReturnUpdateReplyObject() throws Exception {

        // given
        Reply savedReply = Reply.builder()
                .text("Test reactie één")
                .build();
        replyRepository.save(savedReply);

        Reply updatedReply = Reply.builder()
                .text("Test reactie twee")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/reacties/{replyId}", savedReply.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedReply)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text",
                        is(updatedReply.getText())));
    }

    @Test
    public void givenUpdatedReply_whenUpdateReply_thenReturn404() throws Exception {

        // given
        Long replyId = 1L;
        Reply savedReply = Reply.builder()
                .text("Test reactie")
                .build();

        Reply updatedReply = Reply.builder()
                .id(1L)
                .text("Test reactie gewijzigd")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/reacties/{replyId}", replyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedReply)));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenReplyId_whenDeleteReply_thenReturn200() throws Exception {

        // given
        Reply savedReply = Reply.builder()
                .text("Test voor verwijderen")
                .build();
        replyRepository.save(savedReply);

        // when
        ResultActions response = mockMvc.perform(delete("/hulppost/reacties/{replyId}", savedReply.getId()));

        // then
        response.andExpect(status().isOk()).andDo(print());
    }
}
