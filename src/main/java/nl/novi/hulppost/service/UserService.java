package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getAllUsers();

    Optional<UserDto> getUserById(Long userId);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long userId);

    void deleteUser(Long userId);

}
