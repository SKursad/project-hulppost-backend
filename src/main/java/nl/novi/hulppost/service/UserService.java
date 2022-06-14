package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.UserDto;
import nl.novi.hulppost.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getAllUsers();

    Optional<UserDto> getUserById(Long userId);

    UserDto registerHelpSeeker(UserDto userDto);

    UserDto registerVolunteer(UserDto userDto);

    UserDto registerAdmin(UserDto userDto);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(User user, String oldPassword);

    UserDto updateUser(UserDto userDto, Long userId);

    void deleteUser(Long userId);

    User findUserByEmail(String email);
}
