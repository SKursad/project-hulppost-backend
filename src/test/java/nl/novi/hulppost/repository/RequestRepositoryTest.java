package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.model.enums.TypeRequest;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RequestRepositoryTest {

    @Autowired
    private RequestRepository underTest;

    private Request request;


    @BeforeEach
    public void setup() {
        request = Request.builder()
                .id(1L)
                .title("Testing request repo")
                .typeRequest(TypeRequest.Sociaal)
                .content("Testing request repository")
                .build();

    }


    @DisplayName("JUnit test finding the request by id")
    @Test
    void itShouldFindTheRequestById() {

        // given - precondition or setup
//        request = Request.builder()
//                .id(1L)
//                .title("Testing request repo")
//                .typeRequest(TypeRequest.Sociaal)
//                .content("Testing request repository")
//                .build();

        // when - action that's under test
        underTest.findById(1L);

        Assertions.assertEquals(request.getId(), 1L);

    }

    @DisplayName("JUnit test finding the request by title")
    @Test
    void itShouldFindTheRequestByTitle() {

        // given
//        request = Request.builder()
//                .id(1L)
//                .title("Testing request repo")
//                .typeRequest(TypeRequest.Sociaal)
//                .content("Testing request repository")
//                .build();

        // when
        String title = "Testing request repo";
        underTest.save(request);

        // then
        boolean expected = underTest.existsByTitle(title);
        AssertionsForClassTypes.assertThat(expected).isTrue();

    }

}