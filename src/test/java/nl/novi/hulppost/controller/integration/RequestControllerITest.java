package nl.novi.hulppost.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.model.enums.TypeRequest;
import nl.novi.hulppost.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RequestControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        requestRepository.deleteAll();
    }

    @Test
    public void givenRequestObject_whenCreateRequest_thenReturnSavedRequest() throws Exception {

        // given - precondition or setup
        Request request = Request.builder()
                .account(new Account())
                .id(1L).title("Hulp bij ophalen pakket voedselbank")
                .typeRequest(TypeRequest.Praktisch)
                .content("Hallo, Voor een cliënte van de Thuisbegeleiding van de Amstelring ben ik op zoek " +
                        "naar een persoon die haar kan helpen met het ophalen van een pakket" +
                        " bij de voedselbank in Hoofddorp.")
                .build();
        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/hulppost/requests").with(user("Test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title",
                        is(request.getTitle())))
                .andExpect(jsonPath("$.typeRequest",
                        is(TypeRequest.Praktisch.toString())))
                .andExpect(jsonPath("$.content",
                        is(request.getContent())));

    }

    @Test
    public void givenListOfRequests_whenGetAllRequests_thenReturnRequestsList() throws Exception {

        // given
        List<Request> listOfRequests = new ArrayList();
        listOfRequests.add(Request.builder()
                .title("Opzoek naar hulp bij verhuizen")
                .typeRequest(TypeRequest.Praktisch)
                .content("Ik ben op zoek naar iemand die mij kan helpen bij het verhuizen")
                .build());
        listOfRequests.add(Request.builder()
                .title("Vrijwilliger gezocht")
                .typeRequest(TypeRequest.Sociaal)
                .content("Wj zijn op zoek naar een vrijwilliger die het gebruik van Email wilt leren aan senioren")
                .build());
        requestRepository.saveAll(listOfRequests);

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
        Request request = Request.builder()
                .title("Hulp bij uitlaten van mijn huisdier")
                .typeRequest(TypeRequest.Sociaal)
                .content("Door de operaties aan mijn been kan ik niet meer zo goed lopen." + "\n" +
                        "Ik zoek iemand die bereid is om mijn hond uit te laten")
                .build();
        requestRepository.save(request);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/requests/{requestId}", request.getId()));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title",
                        is(request.getTitle())))
                .andExpect(jsonPath("$.typeRequest",
                        is(request.getTypeRequest().toString())))
                .andExpect(jsonPath("$.content",
                        is(request.getContent())));

    }

    @Test
    public void givenInvalidRequestId_whenGetRequestById_thenReturnEmpty() throws Exception {

        // given
        Long requestId = 2L;
        Request request = Request.builder()
                .id(1L)
                .title("Vrijwilliger gezocht")
                .typeRequest(TypeRequest.Sociaal)
                .content("Wj zijn op zoek naar een vrijwilliger die het gebruik van Email wilt leren aan senioren")
                .build();
        requestRepository.save(request);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/requests/{requestId}", requestId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedRequest_whenUpdateRequest_thenReturnUpdateRequestObject() throws Exception {

        // given
        Request savedRequest = Request.builder()
                .title("Maaltijd rondbrengen")
                .typeRequest(TypeRequest.Sociaal)
                .content("Zorgcentrum HulpOrganisatie in Slotermeer is op zoek naar enkele enthousiste vrijwilligers, " +
                        "die maaltijden bij ouderen in de wijk willen bezorgen.")
                .build();
        requestRepository.save(savedRequest);

        Request updatedRequest = Request.builder()
                .title("Maaltijden rondbrengen bij ouderen in Slotermeer")
                .typeRequest(TypeRequest.Praktisch)
                .content("Zorgcentrum HulpOrganisatie in Slotermeer is op zoek naar enkele enthousiaste vrijwilligers, " +
                        "die maaltijden bij ouderen in de wijk willen bezorgen." + "\n" +
                        "Dit doe je met je eigen auto, de gereden kilometers kunnen worden gedeclareerd.")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/requests/{requestId}", savedRequest.getId()).with(user("Test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title",
                        is(updatedRequest.getTitle())))
                .andExpect(jsonPath("$.typeRequest",
                        is(updatedRequest.getTypeRequest().toString())))
                .andExpect(jsonPath("$.content",
                        is(updatedRequest.getContent())));
    }

    @Test
    public void givenUpdatedRequest_whenUpdateRequest_thenReturn404() throws Exception {

        // given
        Long requestId = 10L;
        Request savedRequest = Request.builder()
                .title("Maaltijden rondbrengen")
                .typeRequest(TypeRequest.Praktisch)
                .content("Zorgcentrum HulpOrganisatie in Slotermeer is op zoek naar enkele enthousiste vrijwilligers, " +
                        "die maaltijden bij ouderen in de wijk willen bezorgen.")
                .build();
        requestRepository.save(savedRequest);

        Request updatedRequest = Request.builder()
                .title("Maaltijden rondbrengen bij ouderen in Slotermeer")
                .typeRequest(TypeRequest.Praktisch)
                .content("Zorgcentrum HulpOrganisatie in Slotermeer is op zoek naar enkele enthousiaste vrijwilligers, " +
                        "die maaltijden bij ouderen in de wijk willen bezorgen." + "\n" +
                        "Dit doe je met je eigen auto, de gereden kilometers kunnen worden gedeclareerd.")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/requests/{requestId}", requestId).with(user("Test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void givenRequestId_whenDeleteRequest_thenReturn200() throws Exception {

        // given
        Request savedRequest = Request.builder()
                .title("Vrijwilliger gezocht")
                .typeRequest(TypeRequest.Sociaal)
                .content("Wj zijn op zoek naar een vrijwilliger die het gebruik van Email wilt leren aan senioren")
                .build();
        requestRepository.save(savedRequest);

        // when
        ResultActions response = mockMvc.perform(delete("/hulppost/requests/{requestId}", savedRequest.getId()).with(user("Test")));

        // then
        response.andExpect(status().isOk()).andDo(print());
    }
}

