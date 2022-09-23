package nl.novi.hulppost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.config.AppConfiguration;
import nl.novi.hulppost.config.WebConfiguration;
import nl.novi.hulppost.dto.RequestDTO;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.model.User;
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
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;

    @MockBean
    private ReplyService replyService;

    @MockBean
    private FileService attachmentService;

    @MockBean
    private UserService userService;

    @MockBean
    private AccountService accountService;

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
    public void givenRequestObject_whenCreateRequest_thenReturnSavedRequest() throws Exception {

        // given - precondition or setup
        Account account = Account.builder()
//                .id(1L)
                .firstName("Test")
                .surname("Person")
                .birthday("12/08/02")
                .gender("M")
                .zipCode("1455AZ")
                .build();

        Request request = Request.builder()
                .user(new User())
                .id(1L)
                .title("Hulp bij uitlaten van mijn huisdier")
                .typeRequest("Sociaal")
                .content("Door de operaties aan mijn been kan ik niet meer zo goed lopen." + "\n" +
                        "Ik zoek iemand die bereid is om mijn hond uit te laten")
                .build();
        given(requestService.saveRequest( any(RequestDTO.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/hulppost/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title",
                        is(request.getTitle())))
                .andExpect(jsonPath("$.typeRequest",
                        is(request.getTypeRequest())))
                .andExpect(jsonPath("$.content",
                        is(request.getContent())));
    }

    @Test
    public void givenListOfRequests_whenGetAllRequests_thenReturnRequestsList() throws Exception {

        // given
        List<RequestDTO> listOfRequests = new ArrayList<>();

        listOfRequests.add(RequestDTO.builder()
                .title("Opzoek naar hulp bij verhuizen")
                .typeRequest("Sociaal")
                .content("Ik ben op zoek naar iemand die mij kan helpen bij het verhuizen")
                .build());

        listOfRequests.add(RequestDTO.builder()
                .title("Hulp bij uitlaten van mijn huisdier")
                .typeRequest("Sociaal")
                .content("Ik kan niet erg goed lopen door mijn operaties aan mijn been " + "\n" +
                        "Ik zoek iemand die bereid is om mijn hond uit te laten").build());
        given(requestService.getAllRequests(Optional.of(1L))).willReturn(listOfRequests);
//        given(requestService.getAllRequests(Optional.of(1L))).willReturn(listOfRequests);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/requests"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfRequests.size())));
    }

    @Test
    public void givenRequestId_whenGetRequestById_thenReturnRequestObject() throws Exception {

        // given
        Long requestId = 1L;
        RequestDTO requestDto = RequestDTO.builder()
                .title("Opzoek naar hulp bij verhuizen")
                .typeRequest("Praktisch")
                .content("Ik ben op zoek naar iemand die mij kan helpen bij het verhuizen")
                .build();
        given(requestService.getRequestById(requestId)).willReturn(Optional.of(requestDto));

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/requests/{requestId}", requestId));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title",
                        is(requestDto.getTitle())))
                .andExpect(jsonPath("$.typeRequest",
                        is(requestDto.getTypeRequest())))
                .andExpect(jsonPath("$.content",
                        is(requestDto.getContent())));

    }

    @Test
    public void givenInvalidRequestId_whenGetRequestById_thenReturnEmpty() throws Exception {

        // given
        Long requestId = 2L;
        RequestDTO requestDto = RequestDTO.builder()
                .title("Vrijwilliger gezocht")
                .typeRequest("Sociaal")
                .content("Wj zijn op zoek naar een vrijwilliger die het gebruik " +
                        "van Email wilt leren aan senioren")
                .build();
        given(requestService.getRequestById(requestId)).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/requests/{requestId}", requestId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void givenUpdatedRequest_whenUpdateRequest_thenReturnUpdateRequestObject() throws Exception {

        // given
        Long requestId = 1L;
        RequestDTO savedRequest = RequestDTO.builder()
                .title("Maaltijden rondbrengen")
                .typeRequest("Praktisch")
                .content("Zorgcentrum HulpOrganisatie is op zoek naar enkele enthousiaste vrijwilligers, " +
                        "die maaltijden bij ouderen in de wijk willen bezorgen.")
                .build();

        RequestDTO updatedRequest = RequestDTO.builder()
                .id(2L)
                .title("Maaltijden rondbrengen bij ouderen in Slotermeer")
                .typeRequest("Praktisch")
                .content("Zorgcentrum HulpOrganisatie in Slotermeer naar enkele enthousiaste vrijwilligers, " +
                        "die maaltijden bij ouderen in de wijk willen bezorgen." + "\n" +
                        "Dit doe je met je eigen auto, de gereden kilometers kunnen worden gedeclareerd.")
                .build();
        given(requestService.getRequestById(requestId)).willReturn(Optional.of(savedRequest));
        given(requestService.updateRequest(any(RequestDTO.class), anyLong()))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/requests/{id}", requestId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedRequest)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title",
                        is(updatedRequest.getTitle())))
                .andExpect(jsonPath("$.typeRequest",
                        is(updatedRequest.getTypeRequest())))
                .andExpect(jsonPath("$.content",
                        is(updatedRequest.getContent())));
    }

    @Test
    public void givenUpdatedRequest_whenUpdateRequest_thenReturn404() throws Exception {

        // given
        Long requestId = 1L;
        RequestDTO savedRequest = RequestDTO.builder()
                .title("Vrijwiliger gzcht")
                .typeRequest("Sociaal")
                .content("Wj zijn op zoek naar een vrijwilliger die d gebruik van Emal wilt leren aan senioren")
                .build();

        RequestDTO updatedRequest = RequestDTO.builder()
                .id(1L)
                .title("Vrijwilliger gezocht")
                .typeRequest("Sociaal")
                .content("Wj zijn op zoek naar een vrijwilliger die het gebruik van Email wilt leren aan senioren")
                .build();
        given(requestService.getRequestById(requestId)).willReturn(Optional.empty());
        given(requestService.updateRequest(any(RequestDTO.class), anyLong()))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/requests/{requestId}", requestId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenRequestId_whenDeleteRequest_thenReturn200() throws Exception {

        // given
        Long requestId = 1L;
        willDoNothing().given(requestService).deleteRequest(requestId);

        // when
        ResultActions response = mockMvc.perform(delete("/hulppost/requests/{requestId}", requestId));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}

