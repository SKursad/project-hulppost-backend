package nl.novi.hulppost.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.config.AppConfiguration;
import nl.novi.hulppost.config.WebConfiguration;
import nl.novi.hulppost.model.Reply;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.model.Role;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.ReplyRepository;
import nl.novi.hulppost.service.serviceImpl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
    WebApplicationContext context;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AppConfiguration appConfiguration;

    @MockBean
    FileServiceImpl fileService;

    @MockBean
    WebConfiguration webConfiguration;


    @BeforeEach
    public void setup() {
        User user = User.builder()
                .id(8L)
                .email("test@test.nl")
                .username("shizzle")
                .password("whatever")
                .build();

        Request request = Request.builder()
                .id(2L)
                .title("Test")
                .typeRequest("Praktisch")
                .content("Content")
                .timestamp(new Date())
                .build();
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
        replyRepository.deleteAll();
    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void givenReplyObject_whenCreateReply_thenReturnSavedReply() throws Exception {
//
//        // given - precondition or setup
//        Reply reply = Reply.builder()
//                .id(1L)
//                .user(user)
//                .request(request)
//                .text("Hallo, dit is een reactie op een aanvraag")
//                .timestamp(new Date())
//                .build();
//
//        // when - action that's under test
//        ResultActions response = mockMvc.perform(post("/api/v1/replies", request.getId()).with(user("shizzle"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(reply)));
//
//        // then - verify the output
//        response.andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.text",
//                        is(reply.getText())));
//
//    }

    @Test
    @WithMockUser(roles = "VOLUNTEER")
    public void givenListOfReplies_whenGetAllReplies_thenReturnRepliesList() throws Exception {
        //setup
        User user = User.builder()
                .id(8L)
                .email("test@test.nl")
                .username("shizzle")
                .password("whatever")
                .build();

        Request request = Request.builder()
                .id(2L)
                .title("Test")
                .typeRequest("Praktisch")
                .content("Content")
                .timestamp(new Date())
                .build();

        // given
        List<Reply> listOfReplies = new ArrayList<>();
        listOfReplies.add(Reply.builder()
                .id(1L)
                .user(user)
                .request(request)
                .text("Dit is een reactie om te testen")
                .timestamp(new Date())
                .build());

        listOfReplies.add(Reply.builder()
                .id(2L)
                .text("Dit is een tweede tekst voor om te testen")
                .user(user)
                .request(request)
                .timestamp(new Date())
                .build());
        replyRepository.saveAll(listOfReplies);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/replies", request.getId()).with(user("User")));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfReplies.size())));
    }

    @Test
    public void givenReplyId_whenGetReplyById_thenReturnReplyObject() throws Exception {
        //setup
        User user = User.builder()
                .id(8L)
                .email("test@test.nl")
                .username("shizzle")
                .password("whatever")
                .build();

        Request request = Request.builder()
                .id(2l)
                .title("Test")
                .typeRequest("Praktisch")
                .content("Content")
                .timestamp(new Date())
                .build();
        // given
        Reply reply = Reply.builder()
                .text("De test voor id ophalen van een reactie en dan terug weergeven als object")
                .user(user)
                .request(request)
                .timestamp(new Date())
                .build();
        replyRepository.save(reply);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/replies/{replyId}", reply.getId()).with(user("user")));

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
        ResultActions response = mockMvc.perform(get("/api/v1/replies/{replyId}", replyId).with(user("user")));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

//    @Test
//    @WithMockUser(roles = "VOLUNTEER")
//    public void givenUpdatedReply_whenUpdateReply_thenReturnUpdateReplyObject() throws Exception {
//
//
//        //setup
//        User user = User.builder()
//                .id(1L)
//                .email("test@test.nl")
//                .username("shizzle")
//                .password("test123")
//                .build();
//
//        Request request = Request.builder()
//                .id(2L)
//                .title("Test")
//                .typeRequest("Praktisch")
//                .content("Content")
//                .timestamp(new Date())
//                .build();
//
//        // given
//        Reply savedReply = Reply.builder()
//                .id(5L)
//                .text("Test reactie één")
//                .user(user)
//                .request(request)
//                .timestamp(new Date())
//                .build();
//        replyRepository.save(savedReply);
//
//        Reply updatedReply = Reply.builder()
//                .id(5L)
//                .user(user)
//                .request(request)
//                .text("Test reactie twee")
//                .timestamp(new Date())
//                .build();
//
//        // when
//        ResultActions response = mockMvc.perform(put("/api/v1/replies/{replyId}", savedReply.getId()).with(user("User"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedReply)));
//
//        // then
//        response.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.text",
//                        is(updatedReply.getText())));
//    }

//    @Test
//    @WithMockUser(roles = "VOLUNTEER")
//    @Access(value = AccessType.PROPERTY)
//    public void givenUpdatedReply_whenUpdateReply_thenReturn404() throws Exception {
//
//        //setup
//        User user = User.builder()
//                .id(1L)
//                .email("test@test.nl")
//                .username("shizzle")
//                .password("whatever")
//                .build();
//
//        Request request = Request.builder()
//                .id(2L)
//                .title("Test")
//                .typeRequest("Praktisch")
//                .content("Content")
//                .timestamp(new Date())
//                .build();
//
//
//        // given
//        Long replyId = 2L;
//        Reply savedReply = Reply.builder()
//                .text("Test reactie")
//                .user(user)
//                .request(request)
//                .timestamp(new Date())
//                .build();
//
//        Reply updatedReply = Reply.builder()
//                .id(2L)
//                .user(user)
//                .request(request)
//                .text("Test reactie gewijzigd")
//                .timestamp(new Date())
//                .build();
//
//        // when
//        ResultActions response = mockMvc.perform(put("/api/v1/replies/{replyId}", replyId, user("shizzle"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedReply)));
//
//        // then
//        response.andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(roles = "VOLUNTEER")
//    public void givenReplyId_whenDeleteReply_thenReturn200() throws Exception {
//        //setup
//        User user = User.builder()
//                .id(1L)
//                .email("test@test.nl")
//                .username("shizzle")
//                .password("whatever")
//                .build();
//
//        Request request = Request.builder()
//                .id(2L)
//                .title("Test")
//                .typeRequest("Praktisch")
//                .content("Content")
//                .timestamp(new Date())
//                .build();
//
//
//        // given
//        Reply savedReply = Reply.builder()
//                .text("Test voor verwijderen")
//                .request(request)
//                .timestamp(new Date())
//                .build();
//        replyRepository.save(savedReply);
//
//        // when
//        ResultActions response = mockMvc.perform(delete("/api/v1/replies/{replyId}", savedReply.getId()));
//
//        // then
//        response.andExpect(status().isOk()).andDo(print());
//    }
}
