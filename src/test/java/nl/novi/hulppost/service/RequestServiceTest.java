package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.RequestDto;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.model.enums.TypeRequest;
import nl.novi.hulppost.repository.RequestRepository;
import nl.novi.hulppost.service.serviceImpl.RequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
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
    private RequestDto requestDto;
    private Request request;


    @BeforeEach
    public void setup() {
//        requestRepository = mock(RequestRepository.class);
        underTest = new RequestServiceImpl(requestRepository, new ModelMapper());

        request = Request.builder()
                .id(1L)
                .title("Hulp bij uitlaten van mijn huisdier")
                .typeRequest(TypeRequest.Sociaal)
                .content("Ik kan niet erg goed lopen door mijn operatie." + "\n" +
                        "Ik zoek iemand die bereid is om mijn hond uit te laten")
                .build();

        requestDto = RequestDto.builder()
                .id(1L)
                .title("Hulp bij uitlaten van mijn huisdier")
                .typeRequest(TypeRequest.Sociaal)
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
                .title("Hulp bij uitlaten van mijn huisdier")
                .typeRequest(TypeRequest.Sociaal)
                .content("Ik kan niet erg goed lopen door mijn operatie en zoek" +
                        " iemand die bereid is om mijn hond uit te laten")
                .build();
        given(requestRepository.findAll()).willReturn(List.of(request, request1));

        // when
        List<RequestDto> requestList = underTest.getAllRequests();

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
                .typeRequest(TypeRequest.Sociaal)
                .content("Ik kan niet erg goed lopen door mijn operatie" +
                        " en zoek iemand die bereid is om mijn hond uit te laten")
                .build();
        given(requestRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<RequestDto> requestList = underTest.getAllRequests();

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
        RequestDto savedRequest = underTest.getRequestById(request.getId()).get();

        // then
        assertThat(savedRequest).isNotNull();

    }

    @DisplayName("JUnit test for updateRequest method")
    @Test
    public void givenRequestObject_whenUpdateRequest_thenReturnUpdatedRequest() {

        // given
        Long requestId = 1L;
        given(requestRepository.save(request)).willReturn(request);

        request.setTitle("Hulp bij administratie");
        request.setTypeRequest(TypeRequest.Praktisch);
        request.setContent("Iemand gezocht die mij boekhouding kan nakijken");

        // when
        RequestDto updatedRequest = underTest.updateRequest(requestDto, requestId);

        // then
        assertThat(updatedRequest.getTitle()).isEqualTo("Hulp bij administratie");
        assertThat(updatedRequest.getTypeRequest()).isEqualTo(TypeRequest.Praktisch).toString();
        assertThat(updatedRequest.getContent()).isEqualTo("Iemand gezocht die mij boekhouding kan nakijken");

    }

    @DisplayName("JUnit test for deleteRequest method")
    @Test
    public void givenRequestId_whenDeleteRequest_thenNothing() {

        // given
        Long requestId = 1L;
        willDoNothing().given(requestRepository).deleteById(requestId);

        // when
        underTest.deleteRequest(requestId);

        // then
        verify(requestRepository, times(1)).deleteById(requestId);

    }

}

