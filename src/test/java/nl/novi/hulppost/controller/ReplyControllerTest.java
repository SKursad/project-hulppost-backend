package nl.novi.hulppost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.config.AppConfiguration;
import nl.novi.hulppost.config.WebConfiguration;
import nl.novi.hulppost.dto.ReplyDTO;
import nl.novi.hulppost.security.CustomUserDetailsService;
import nl.novi.hulppost.security.JwtAuthenticationEntryPoint;
import nl.novi.hulppost.security.JwtAuthenticationFilter;
import nl.novi.hulppost.security.JwtTokenProvider;
import nl.novi.hulppost.service.*;
import nl.novi.hulppost.service.serviceImpl.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReplyService replyService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private UserService userService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private FileService attachmentService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private PasswordEncoder passwordEncoder;

    @MockBean
    AppConfiguration appConfiguration;

//    @MockBean
//    FileServiceImpl fileService;

    @MockBean
    WebConfiguration webConfiguration;


    @Test
    public void givenReplyObject_whenCreateReply_thenReturnSavedReply() throws Exception {

        // given - precondition or setup

        ReplyDTO replyDto = ReplyDTO.builder()
                .id(1L)
                .userId(1L)
                .requestId(1L)
                .text("Hallo, dit is een reactie op een hulpverzoek")
                .build();
        given(replyService.saveReply(any(ReplyDTO.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/api/v1/replies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDto)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text",
                        is(replyDto.getText())));

    }

//    @Test
//    public void givenListOfReplies_whenGetAllReplies_thenReturnRepliesList() throws Exception {
//
//        // given
//        List<ReplyDTO> listOfReplies = new ArrayList<>();
//
//        listOfReplies.add(ReplyDTO.builder()
//                .id(1L)
//                .userId(1L)
//                .requestId(1L)
//                .text("Dit is een reactie om te testen")
//                .build());
//
//        listOfReplies.add(ReplyDTO.builder()
//                .id(1L)
//                .userId(2L)
//                .requestId(2L)
//                .text("Dit is een tweede tekst voor om te testen")
//                .build());
//        given(replyService.getAllReplies(Optional.of(1L), Optional.of(2L))).willReturn(listOfReplies);
//
//        // when
//        ResultActions response = mockMvc.perform(get("/api/v1/replies"));
//
//        // then
//        response.andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.size()",
//                        is(listOfReplies.size())));
//    }

    @Test
    public void givenReplyId_whenGetReplyById_thenReturnReplyObject() throws Exception {

        // given
        Long replyId = 1L;
        ReplyDTO replyDto = ReplyDTO.builder()
                .id(1L)
                .userId(1L)
                .requestId(2L)
                .text("De test voor id ophalen van een reactie en dan terug weergeven als een object")
                .build();
        given(replyService.getReplyById(replyId)).willReturn(Optional.of(replyDto));

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/replies/{replyId}", replyId));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text",
                        is(replyDto.getText())));

    }

    @Test
    public void givenInvalidReplyId_whenGetReplyById_thenReturnEmpty() throws Exception {

        // given
        Long replyId = 2L;
        ReplyDTO replyDto = ReplyDTO.builder()
                .text("Test tekst voor reactie")
                .build();
        given(replyService.getReplyById(replyId)).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/replies/{replyId}", replyId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }
//
//    @Test
//    public void givenUpdatedReply_whenUpdateReply_thenReturnUpdateReplyObject() throws Exception {
//
//        // given
//        Long replyId = 1L;
//        ReplyDTO savedReply = ReplyDTO.builder()
//                .text("Test reactie één")
//                .build();
//
//        ReplyDTO updatedReply = ReplyDTO.builder()
//                .id(2L)
//                .text("Test reactie twee")
//                .build();
//        given(replyService.getReplyById(replyId)).willReturn(Optional.of(savedReply));
//        given(replyService.updateReply(any(ReplyDTO.class), anyLong()))
//                .willAnswer((invocation) -> invocation.getArgument(0));
//
//        // when
//        ResultActions response = mockMvc.perform(put("/api/v1/replies/{replyId}", replyId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedReply)));
//
//        // then
//        response.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.text",
//                        is(updatedReply.getText())));
//    }
//
//    @Test
//    public void givenUpdatedReply_whenUpdateReply_thenReturn404() throws Exception {
//
//        // given
//        Long replyId = 1L;
//        ReplyDTO savedReply = ReplyDTO.builder()
//                .text("Test reactie")
//                .build();
//
//        ReplyDTO updatedReply = ReplyDTO.builder()
//                .id(1L)
//                .text("Test reactie")
//                .build();
//        given(replyService.getReplyById(replyId)).willReturn(Optional.empty());
//        given(replyService.updateReply(any(ReplyDTO.class), anyLong()))
//                .willAnswer((invocation) -> invocation.getArgument(0));
//
//        // when
//        ResultActions response = mockMvc.perform(put("/api/v1/replies/{replyId}", replyId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedReply)));
//
//        // then
//        response.andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//    @Test
//    public void givenReplyId_whenDeleteReply_thenReturn200() throws Exception {
//
//        // given
//        Long replyId = 1L;
//        willDoNothing().given(replyService).deleteReply(replyId);
//
//        // when
//        ResultActions response = mockMvc.perform(delete("/api/v1/replies/{replyId}", replyId));
//
//        // then
//        response.andExpect(status().isOk())
//                .andDo(print());
//    }
}