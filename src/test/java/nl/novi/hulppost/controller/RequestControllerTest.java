package nl.novi.hulppost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.config.AppConfiguration;
import nl.novi.hulppost.config.SecurityConfiguration;
import nl.novi.hulppost.config.WebConfiguration;
import nl.novi.hulppost.dto.RequestDTO;
import nl.novi.hulppost.dto.UserDTO;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.model.FileAttachment;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.UserRepository;
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
import org.springframework.context.annotation.Role;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@AutoConfigureMockMvc
@WebAppConfiguration
//@SpringBootTest
@ContextConfiguration
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
public class RequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RequestService requestService;

    @MockBean
    ReplyService replyService;

    @MockBean
    UserService userService;

    @MockBean
    AccountService accountService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    PasswordEncoder passwordEncoder;

    @MockBean
    FileServiceImpl fileService;

    @MockBean
    FileAttachment fileAttachment;

    @MockBean
    WebConfiguration webConfiguration;

    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    SecurityConfiguration securityConfiguration;


//    @Mock
//    private ApplicationConfig applicationConfig;


    @Test
    public void givenRequestObject_whenCreateRequest_thenReturnSavedRequest() throws Exception {
        User user = User.builder()
                .username("User")
                .password("User")
                .email("shizzle@shizzlecompany.com")
                .build();

        // given - precondition or setup
        Account account = Account.builder()
//                .id(1L)
                .firstName("Test")
                .surname("Person")
                .birthday(new Date(2002-8-12))
                .gender("M")
                .zipCode("1455AZ")
                .build();

        Request request = Request.builder()
                .user(user)
                .id(1L)
                .title("Hulp bij uitlaten huisdier")
                .typeRequest("Sociaal")
                .content("Door de operaties aan mijn been kan ik niet meer zo goed lopen." + "\n" +
                        "Ik zoek iemand die bereid is om mijn hond uit te laten")
                .build();
        given(requestService.saveRequest(any(RequestDTO.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/api/v1/requests")
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
        given(requestService.getAllRequests(Optional.empty())).willReturn(listOfRequests);
//        given(requestService.getAllRequests(Optional.of(1L))).willReturn(listOfRequests);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/requests"));

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
        ResultActions response = mockMvc.perform(get("/api/v1/requests/{requestId}", requestId));

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
        ResultActions response = mockMvc.perform(get("/api/v1/requests/{requestId}", requestId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

//    @Test
//    public void givenUpdatedRequest_whenUpdateRequest_thenReturnUpdateRequestObject() throws Exception {
//
//        // given
//        Long requestId = 1L;
//        RequestDTO savedRequest = RequestDTO.builder()
//                .title("Maaltijden rondbrengen")
//                .typeRequest("Praktisch")
//                .content("Zorgcentrum HulpOrganisatie is op zoek naar enkele enthousiaste vrijwilligers, " +
//                        "die maaltijden bij ouderen in de wijk willen bezorgen.")
//                .build();
//
//        RequestDTO updatedRequest = RequestDTO.builder()
//                .id(2L)
//                .title("Maaltijden rondbrengen bij ouderen in Slotermeer")
//                .typeRequest("Praktisch")
//                .content("Zorgcentrum HulpOrganisatie in Slotermeer naar enkele enthousiaste vrijwilligers, " +
//                        "die maaltijden bij ouderen in de wijk willen bezorgen." + "\n" +
//                        "Dit doe je met je eigen auto, de gereden kilometers kunnen worden gedeclareerd.")
//                .build();
//        given(requestService.getRequestById(requestId)).willReturn(Optional.of(savedRequest));
//        given(requestService.updateRequest(any(RequestDTO.class), anyLong()))
//                .willAnswer((invocation) -> invocation.getArgument(0));
//
//        // when
//        ResultActions response = mockMvc.perform(put("/api/v1/requests/{id}", requestId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedRequest)));
//
//        // then
//        response.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title",
//                        is(updatedRequest.getTitle())))
//                .andExpect(jsonPath("$.typeRequest",
//                        is(updatedRequest.getTypeRequest())))
//                .andExpect(jsonPath("$.content",
//                        is(updatedRequest.getContent())));
//    }

//    @WithMockUser(username = "User", roles="ADMIN")
//    @Test
//    public void givenUpdatedRequest_whenUpdateRequest_thenReturn404() throws Exception {
////        SecurityContextHolder.setContext(getDesiredSecurityContext("User"));
//
//        User user = User.builder()
//                .id(1L)
//                .username("User")
//                .password("User")
//                .email("shizzle@shizzlecompany.com")
//                .build();

        // given - precondition or setup
//        Account account = Account.builder()
////                .id(1L)
//                .firstName("Test")
//                .surname("Person")
//                .birthday(new Date(2002-8-12))
//                .gender("M")
//                .zipCode("1455AZ")
//                .build();
//
//        Request request = Request.builder()
//                .user(user)
//                .id(1L)
//                .title("Hulp bij uitlaten huisdier")
//                .typeRequest("Sociaal")
//                .content("Door de operaties aan mijn been kan ik niet meer zo goed lopen." + "\n" +
//                        "Ik zoek iemand die bereid is om mijn hond uit te laten")
//                .build();

//        // given
//        Long requestId = 1L;
//        RequestDTO savedRequest = RequestDTO.builder()
//                .title("Vrijwiliger gzcht")
//                .typeRequest("Sociaal")
//                .content("Wj zijn op zoek naar een vrijwilliger die d gebruik van Emal wilt leren aan senioren")
//                .build();
//
//        RequestDTO updatedRequest = RequestDTO.builder()
//                .id(1L)
//                .userId(1L)
//                .title("Vrijwilliger gezocht")
//                .typeRequest("Sociaal")
//                .content("Wj zijn op zoek naar een vrijwilliger die het gebruik van Email wilt leren aan senioren")
//                .build();
//
//        given(userRepository.findByUsername(user.getUsername())).willReturn(user);
//        given(requestService.getRequestById(requestId)).willReturn(Optional.empty());
//        given(requestService.updateRequest(any(RequestDTO.class), anyLong()))
//                .willAnswer((invocation) -> invocation.getArgument(0));
//
//        // when
//        ResultActions response = mockMvc.perform(put("/api/v1/requests/{requestId}", requestId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedRequest)));
//
//        // then
//        response.andExpect(status().isNotFound())
//                .andDo(print());
//    }

//    @Test
//    public void givenRequestId_whenDeleteRequest_thenReturn200() throws Exception {
//
//        // given
//        Long requestId = 1L;
//        willDoNothing().given(requestService).deleteRequest(requestId);
//
//        // when
//        ResultActions response = mockMvc.perform(delete("/api/v1/requests/{requestId}", requestId));
//
//        // then
//        response.andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    // This will create a security context with the list of roles required
//    private SecurityContext getDesiredSecurityContext(String username) {
//        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(username, "test", new ArrayList<>());
//        SecurityContext securityContext = new SecurityContextImpl();
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                userDetails, null, userDetails.getAuthorities()
//        );
//        securityContext.setAuthentication(authenticationToken);
//        return securityContext;
//    }
}

