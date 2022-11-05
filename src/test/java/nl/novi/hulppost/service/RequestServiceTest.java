package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.RequestDTO;
import nl.novi.hulppost.model.FileAttachment;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.RequestRepository;
import nl.novi.hulppost.service.serviceImpl.RequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;
    @InjectMocks
    private RequestServiceImpl underTest;
    private RequestDTO requestDto;
    private Request request;


    @BeforeEach
    public void setup() {
//        requestRepository = mock(RequestRepository.class);
        underTest = new RequestServiceImpl(requestRepository);

        request = Request.builder()
                .id(1L)
                .user(new User())
                .title("Hulp bij uitlaten van mijn huisdier")
                .fileAttachment(new FileAttachment())
                .typeRequest("Sociaal")
                .content("Ik kan niet erg goed lopen door mijn operatie." + "\n" +
                        "Ik zoek iemand die bereid is om mijn hond uit te laten")
                .build();

        requestDto = RequestDTO.builder()
                .id(1L)
                .userId(1L)
                .title("Hulp bij uitlaten van mijn huisdier")
                .fileAttachment(new FileAttachment())
                .typeRequest("Sociaal")
                .content("Ik kan niet erg goed lopen door mijn operatie." + "\n" +
                        "Ik zoek iemand die bereid is om mijn hond uit te laten")
                .build();

    }

    @DisplayName("JUnit test for saveRequest method")
    @Test
    public void givenRequestObject_whenSaveRequest_thenReturnRequestObject() {

        // given - precondition or setup
        lenient().when(requestRepository.findById(request.getId())).thenReturn(any());
        given((Request) requestRepository.save(request)).willReturn(request);

        System.out.println(requestRepository);
        System.out.println(underTest);

        // when - action that's under test
        Request newRequest = requestRepository.save(request);

        System.out.println(newRequest);

        // then - verify the output
        assertThat(newRequest).isNotNull();

    }

    @DisplayName("JUnit test for getAllRequests method")
    @Test
    public void givenRequestsList_whenGetAllRequests_thenReturnRequestsList() {

        // given
        Request request1 = Request.builder()
                .id(1L)
                .user(new User())
                .title("Hulp bij uitlaten van mijn huisdier")
                .typeRequest("Sociaal")
                .content("Ik kan niet erg goed lopen door mijn operatie en zoek" +
                        " iemand die bereid is om mijn hond uit te laten")
                .build();
        given(requestRepository.findAll()).willReturn(List.of(request, request1));

        // when
        List<RequestDTO> requestList = underTest.getAllRequests(Optional.empty());

        // then
        assertThat(requestList).isNotNull();
        assertThat(requestList.size()).isEqualTo(2);

    }

    @DisplayName("JUnit test for getAllRequests method (negative scenario)")
    @Test
    public void givenEmptyRequestsList_whenGetAllRequests_thenReturnEmptyRequestsList() {

        // given
        Request request1 = Request.builder()
                .id(2L)
                .title("Hulp bij uitlaten van mijn huisdier")
                .typeRequest("Sociaal")
                .content("Ik kan niet erg goed lopen door mijn operatie" +
                        " en zoek iemand die bereid is om mijn hond uit te laten")
                .build();

        // when
        List<RequestDTO> requestList = underTest.getAllRequests(Optional.of(1L));

        // then
        assertThat(requestList).isEmpty();
        assertThat(requestList.size()).isEqualTo(0);

    }

    @DisplayName("JUnit test for getRequestById method")
    @Test
    public void givenRequestId_whenGetRequestById_thenReturnRequestObject() {

        // given
        given(requestRepository.findById(1L))
                .willReturn(Optional.of(request));

        // when
        RequestDTO savedRequest = underTest.getRequestById(request.getId()).get();

        // then
        assertThat(savedRequest).isNotNull();

    }

    @DisplayName("JUnit test for updateRequest method")
    @Test
    public void givenRequestObject_whenUpdateRequest_thenReturnUpdatedRequest() {

        //Setup
        request.setId(2L);
        request.setTitle("Hulp bij");
        request.setTypeRequest("Praktisch");
        request.setContent("Iemand die mij boekhouding kan nakijken");

        requestDto.setId(2L);
        requestDto.setTitle("Hulp bij administratie");
        requestDto.setTypeRequest("Praktisch");
        requestDto.setContent("Iemand gezocht die mij boekhouding kan nakijken");

        // given
        given(requestRepository.save(request)).willReturn(request);
        given(requestRepository.findById(2L)).willReturn(Optional.of(request));



        // when
        RequestDTO updatedRequest = underTest.updateRequest(requestDto, request.getId());

        // then
        assertThat(updatedRequest.getTitle()).isEqualTo("Hulp bij administratie");
        assertThat(updatedRequest.getTypeRequest()).isEqualTo("Praktisch");
        assertThat(updatedRequest.getContent()).isEqualTo("Iemand gezocht die mij boekhouding kan nakijken");

    }

    @DisplayName("JUnit test for deleteRequest method")
    @Test
    public void givenRequestId_whenDeleteRequest_thenDoNothing() {

        Request request1 = Request.builder()
                .id(1L)
                .title("Hulp bij uitlaten van mijn huisdier")
                .typeRequest("Sociaal")
                .content("Ik kan niet erg goed lopen door mijn operatie" +
                        " en zoek iemand die bereid is om mijn hond uit te laten")
                .build();
        // given
        Long requestId = 1L;
//
        //Setup
        request.setId(1L);
        request.setTitle("Hulp bij");
        request.setTypeRequest("Praktisch");
        request.setContent("Iemand die mij boekhouding kan nakijken");

//        given(requestRepository.save(request1)).willReturn(request1);
//        given(requestRepository.findById(1L)).willReturn(Optional.of(request1));
        given(requestRepository.getById(1L)).willReturn(request1);
        willDoNothing().given(requestRepository).deleteById(requestId);

        // when
        underTest.deleteRequest(requestId);

        // then
        verify(requestRepository, times(1)).deleteById(requestId);

    }

}

