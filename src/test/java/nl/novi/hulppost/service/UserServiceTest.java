package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.UserDTO;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Reply;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl underTest;

    private UserDTO userDTO;

    private User user;

    private Reply reply;


    @BeforeEach
    public void setup() {
//        userRepository = Mockito.mock(UserRepository.class);
//        underTest = new UserServiceImpl(userRepository);

        user = User.builder()
                .id(1L)
                .username("Kurshad")
                .email("Kurshad85@gmail.com")
                .password(passwordEncoder.encode("Test1234"))
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .username("Kurshad")
                .email("Kurshad85@gmail.com")
                .build();

    }

    @DisplayName("JUnit test for getUsername method")
    @Test
    public void givenUserObject_whenGetUsername_thenVerify() {

        // setup
        user.setUsername("TestUsername");
        user.setReply(new ArrayList<Reply>(1));
        user.setRequest(new ArrayList<Request>(1));

        // given
        given(userRepository.findByUsername("TestUsername")).willReturn((user));

        // when
        UserDTO verifyUsername = underTest.getUserByUsername(user.getUsername());

        // then
        assertThat(verifyUsername.getUsername()).isEqualTo("TestUsername");

    }

    // JUnit test for saveUser method
    @DisplayName("JUnit test for saveUser method")
    @Test
    public void givenUserObject_whenSaveUser_thenReturnUserObject() {

        // given - precondition or setup
        lenient().when(userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()))
                .thenReturn(Optional.empty());

        given(userRepository.save(user)).willReturn(user);


        System.out.println(userRepository);
        System.out.println(underTest);

        // when -  action under test
        User newUser = userRepository.save(user);

        System.out.println(newUser);
        // then - verify the output
        assertThat(newUser).isNotNull();
    }


    @DisplayName("JUnit test for update method which throws exception")
    @Test
    public void givenNotExistingEmail_whenUpdateUser_thenThrowsException() {

        // given
        lenient().when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(user);


        System.out.println(userRepository);
        System.out.println(underTest);

        // when
        assertThrows(ResourceNotFoundException.class, () -> {
            underTest.findUserByEmail(user.getEmail());
        });

        // then
        verify(userRepository, never()).save(any(User.class));
    }

    //     JUnit test for getAllUsers method
    @DisplayName("JUnit test for getAllUsers method")
    @Test
    public void givenUsersList_whenGetAllUsers_thenReturnUsersList() {

        user.setReply(new ArrayList<Reply>(1));
        user.setRequest(new ArrayList<Request>(1));

        // given
        User user1 = User.builder()
                .id(2L)
                .username("TestPerson")
                .reply(new ArrayList<Reply>(1))
                .request(new ArrayList<Request>(1))
                .email("test@gmail.com")
                .password(passwordEncoder.encode("Test1234"))
                .build();

        given(userRepository.findAll()).willReturn(List.of(user, user1));

        // when
        List<UserDTO> userList = underTest.getUsersWithParam(Optional.empty(), Optional.empty());

        // then
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    //     JUnit test for getAllUsers method
    @DisplayName("JUnit test for getAllUsers method (negative scenario)")
    @Test
    public void givenEmptyUsersList_whenGetAllUsers_thenReturnEmptyUsersList() {
        // given

        User user1 = User.builder()
                .id(2L)
                .username("TestPerson")
                .email("test@gmail.com")
                .build();

        lenient().when((userRepository.findAll())).thenReturn(Collections.emptyList());

        // when
        List<UserDTO> userList = underTest.getUsersWithParam(Optional.of(0L), Optional.of(0L));

        // then
        assertThat(userList).isEmpty();
        assertThat(userList.size()).isEqualTo(0);
    }

    // JUnit test for getUserById method
    @DisplayName("JUnit test for getUserById method")
    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject() {

        user.setReply(new ArrayList<Reply>(1));
        user.setRequest(new ArrayList<Request>(1));

        // given
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        // when
        UserDTO savedUser = underTest.getUserById(user.getId()).get();

        // then
        assertThat(savedUser).isNotNull();

    }

    //     JUnit test for updateUser method
    @DisplayName("JUnit test for updateUser method")
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUpdatedUser() {

        //Setup
        user.setId(2L);
        user.setEmail("Kur@gmail.com");
        user.setUsername("Kur");
        user.setReply(new ArrayList<Reply>(1));
        user.setRequest(new ArrayList<Request>(1));

        userDTO.setId(2L);
        userDTO.setEmail("Kursad@gmail.com");
        userDTO.setUsername("Kursad");

        // given
        given(userRepository.save(user)).willReturn(user);
        given(userRepository.findById(2L)).willReturn(Optional.of(user));


        // when
        UserDTO updatedUser = underTest.updateUser(userDTO, user.getId());

        // then
        assertThat(updatedUser.getEmail()).isEqualTo("Kursad@gmail.com");
        assertThat(updatedUser.getUsername()).isEqualTo("Kursad");
    }

    // JUnit test for deleteUser method
    @DisplayName("JUnit test for deleteUser method")
    @Test
    public void givenUserId_whenDeleteUser_thenNothing() {

        // given
        Long userId = 1L;
//        user = User.builder()
//        .id(1L)
//        .username("kur")
//        .email("Kur@gmail.com")
//        .password("Test12345")
//        .build();

        willDoNothing().given(userRepository).deleteById(userId);

        // when
        underTest.deleteUser(userId);

        // then
        verify(userRepository, times(1)).deleteById(userId);
    }
}