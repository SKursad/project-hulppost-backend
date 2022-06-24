package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.GetUsersDto;
import nl.novi.hulppost.dto.UserDto;
import nl.novi.hulppost.exception.ResourceNotFoundException;
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

    private UserDto userDto;

    private User user;


    @BeforeEach
    public void setup() {
//        userRepository = Mockito.mock(UserRepository.class);
//        underTest = new UserServiceImpl(userRepository);
//        passwordEncoder = new BCryptPasswordEncoder();

        user = User.builder()
                .id(1L)
                .username("Kurshad")
                .email("Kurshad85@gmail.com")
                .password(passwordEncoder.encode("Test1234"))
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .username("Kurshad")
                .email("Kurshad85@gmail.com")
                .password(passwordEncoder.encode("Test1234"))
                .build();

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
        verify(userRepository, never()).save( any(User.class));
    }

    // JUnit test for getAllUsers method
    @DisplayName("JUnit test for getAllUsers method")
    @Test
    public void givenUsersList_whenGetAllUsers_thenReturnUsersList() {

        // given
        User user1 = User.builder()
                .id(2L)
                .username("TestPerson")
                .email("test@gmail.com")
                .password(passwordEncoder.encode("Test1234"))
                .build();

        given(userRepository.findAll()).willReturn(List.of(user, user1));

        // when
        List<GetUsersDto> userList = underTest.getAllUsers();

        // then
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    // JUnit test for getAllUsers method
    @DisplayName("JUnit test for getAllUsers method (negative scenario)")
    @Test
    public void givenEmptyUsersList_whenGetAllUsers_thenReturnEmptyUsersList() {
        // given

        User user1 = User.builder()
                .id(2L)
                .username("TestPerson")
                .email("test@gmail.com")
                .build();

        given(userRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<GetUsersDto> userList = underTest.getAllUsers();

        // then
        assertThat(userList).isEmpty();
        assertThat(userList.size()).isEqualTo(0);
    }

    // JUnit test for getUserById method
    @DisplayName("JUnit test for getUserById method")
    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject() {

        // given
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        // when
        UserDto savedUser = underTest.getUserById(user.getId()).get();

        // then
        assertThat(savedUser).isNotNull();

    }

    //     JUnit test for updateUser method
    @DisplayName("JUnit test for updateUser method")
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUpdatedUser() {

        // given
        given(userRepository.save(user)).willReturn(user);
        user.setEmail("Kur@gmail.com");
        user.setUsername("Kur");

        // when
        UserDto updatedUser = underTest.updateUser(userDto, user.getId());

        // then
        assertThat(updatedUser.getEmail()).isEqualTo("Kur@gmail.com");
        assertThat(updatedUser.getUsername()).isEqualTo("Kur");
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