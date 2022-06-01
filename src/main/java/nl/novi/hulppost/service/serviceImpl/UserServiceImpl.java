package nl.novi.hulppost.service.serviceImpl;


import nl.novi.hulppost.dto.UserDto;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userList) {
            UserDto userDto = mapToDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    public Optional<UserDto> getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Gebruiker niet gevonden"));

        return Optional.of(mapToDto(user));
    }

    public UserDto saveUser(UserDto userDto) {
        User user = mapToEntity(userDto);
        Optional<User> savedUser = userRepository.findByEmail(userDto.getEmail());
        if (savedUser.isPresent()) {
            throw new ResourceNotFoundException("Gebruiker niet gevonden");
        } else {
            User newUser = userRepository.save(user);
            return mapToDto(newUser);
        }
    }

    public UserDto updateUser(UserDto userDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Gebruiker", "id", userId));

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setAccountId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public User mapToEntity(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setId(userDto.getAccountId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());

        return user;
    }
}

