package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.User;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;
    private User user;


    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .username("Kurshad")
                .email("Kurshad85@gmail.com")
                .password("Test1234A")
                .build();
    }

    @DisplayName("JUnit test checks if user exists by Email")
    @Test
    void itShouldCheckIfUserExistsByUsername() {

        // given - precondition or setup
        User user = User.builder()
                .id(1L)
                .username("TestPerson")
                .email("TestMail@gmail.com")
                .password("Test1234Z")
                .build();
        String username = "TestPerson";

        // when - action that's under test
        underTest.save(user);
        boolean expected = underTest.existsByUsername(username);

        // then - verify output
        AssertionsForClassTypes.assertThat(expected).isTrue();
    }

    @DisplayName("JUnit test checks if user exists by Email")
    @Test
    void itShouldCheckIfUserExistsByEmail() {

        // given
        User user = User.builder()
                .id(1L)
                .username("TestPerson")
                .email("TestMail@gmail.com")
                .password("Test1234Z")
                .build();
        String email = "TestMail@gmail.com";

        // when
        underTest.save(user);
        boolean expected = underTest.existsByEmail(email);

        // then
        AssertionsForClassTypes.assertThat(expected).isTrue();
    }

    @DisplayName("JUnit test checks if user exists by Email")
    @Test
    void itShouldFindTheUserByEmailOrUsername() {

        // given
        User user = User.builder()
                .id(1L)
                .username("TestDummy")
                .email("TestMail@gmail.com")
                .password("Test1234Z")
                .build();

        // when
        underTest.findByUsernameOrEmail("TestDummy", "TestMail@gmail.com");

        // then
        Assertions.assertEquals(user.getUsername(), "TestDummy");
        Assertions.assertEquals(user.getEmail(), "TestMail@gmail.com");
    }

    @DisplayName("JUnit test checks if user doesn't exists by Email")
    @Test
    void itShouldCheckIfUserEmailDoesNotExist() {

        // given
        String email = "TestPerson@gmail.com";

        // when
        boolean expected = underTest.existsByEmail(email);

        // then
        AssertionsForClassTypes.assertThat(expected).isFalse();
    }

    @DisplayName("JUnit test for save User operation")
    @Test
    public void givenUserObject_whenSave_thenReturnSavedUser() {

        // given
//        User user = User.builder()
//                .id(1L)
//                .username("Kurshad")
//                .email("Kurshad85@gmail.com")
//                .password("Test1234A")
//                .build();

        // when
        User savedUser = underTest.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0L);
    }

    @DisplayName("JUnit test for get all Users operation")
    @Test
    public void givenUsersList_whenFindAll_thenUsersList() {

        // given
//        User user = User.builder()
//                .id(1L)
//                .username("Kurshad")
//                .email("Kurshad85@gmail.com")
//                .password("Test1234A")
//                .build();

        User user1 = User.builder()
                .id(4L)
                .username("Joop")
                .email("Joop@gmail.com")
                .password("TesT1234B")
                .build();

        // when
        underTest.save(user);
        underTest.save(user1);
        List<User> UserList = underTest.findAll();

        // then
        assertThat(UserList).isNotNull();
        assertThat(UserList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get User by id operation")
    @Test
    public void givenUserObject_whenFindById_thenReturnUserObject() {

        // given
        User user = User.builder()
                .username("Kurshad")
                .email("Kurshad85@gmail.com")
                .password("Test1234A")
                .build();

        // when
        underTest.save(user);
        User UserDB = underTest.findById(user.getId()).get();

        // then
        assertThat(UserDB).isNotNull();
    }

    @DisplayName("JUnit test for get User by email operation")
    @Test
    public void givenUserEmail_whenFindByEmail_thenReturnUserObject() {

        // given
//        User user = User.builder()
//                .username("Kurshad")
//                .email("Kurshad85@gmail.com")
//                .password("Test1234A")
//                .build();
        underTest.save(user);

        // when
        User UserDB = underTest.findByEmail(user.getEmail());

        // then
        assertThat(UserDB).isNotNull();
    }

    @DisplayName("JUnit test for update User operation")
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUpdatedUser() {

        // given
        User user = User.builder()
                .username("Kurshad")
                .email("Kurshad85@gmail.com")
                .password("Test1234A")
                .build();
        underTest.save(user);

        // when
        User savedUser = underTest.findById(user.getId()).get();
        savedUser.setUsername("Salim");
        savedUser.setEmail("Kur@gmail.com");
        User updatedUser = underTest.save(savedUser);

        // then
        assertThat(updatedUser.getUsername()).isEqualTo("Salim");
        assertThat(updatedUser.getEmail()).isEqualTo("Kur@gmail.com");
    }

    @DisplayName("JUnit test for delete User operation")
    @Test
    public void givenUserObject_whenDelete_thenRemoveUser() {

        // given
        User user = User.builder()
                .username("Kurshad")
                .email("Kurshad85@gmail.com")
                .password("Test1234A")
                .build();
        underTest.save(user);

        // when
        underTest.deleteById(user.getId());
        Optional<User> UserOptional = underTest.findById(user.getId());

        // then
        assertThat(UserOptional).isEmpty();
    }

    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnUserObject() {

        // given
//        User user = User.builder()
//                .username("Kurshad")
//                .email("Kurshad85@gmail.com")
//                .password("Test1234A")
//                .build();
        underTest.save(user);
        String username = "Kurshad";
        String email = "Kurshad85@gmail.com";

        // when
        User savedUser = underTest.findByJPQL(username, email);

        // then
        assertThat(savedUser).isNotNull();
    }

    @DisplayName("JUnit test for custom query using JPQL with Named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnUserObject() {

        // given
//        User user = User.builder()
//                .id(1L)
//                .username("Kurshad")
//                .email("Kurshad85@gmail.com")
//                .password("Test1234A")
//                .build();
        underTest.save(user);
        String username = "Kurshad";
        String email = "Kurshad85@gmail.com";

        // when
        User savedUser = underTest.findByJPQLNamedParams(username, email);

        // then
        assertThat(savedUser).isNotNull();
    }
}

