package nl.novi.hulppost.service.serviceImpl;


import nl.novi.hulppost.dto.GetUsersDto;
import nl.novi.hulppost.dto.RequestDto;
import nl.novi.hulppost.dto.UserDto;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Role;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.RoleRepository;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

//    @Override
//    public List<UserDto> getAllUsers() {
//        List<User> userList = userRepository.findAll();
//        List<UserDto> userDtoList = new ArrayList<>();
//
//        for (User user : userList) {
//            UserDto userDto = mapToDto(user);
//            userDtoList.add(userDto);
//        }
//        return userDtoList;
//    }

    @Override
    public List<GetUsersDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<GetUsersDto> userDtoList = new ArrayList<>();

        for (User user : userList) {
            GetUsersDto getUserDto = mapToDto2(user);
            userDtoList.add(getUserDto);
        }
        return userDtoList;
    }

//    @Override
//    public Page<GetUsersDto> getAllUsers(Pageable pageable) {
//        List<User> userList = userRepository.findAll();
//        List<GetUsersDto> userDtoList = new ArrayList<>();
//
//        for (User user : userList) {
//            GetUsersDto getUserDto = mapToDto2(user);
//            userDtoList.add(getUserDto);
//        }
//        return userDtoList;
//    }

    @Override
    public Optional<UserDto> getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Gebruiker niet gevonden"));

        return Optional.of(mapToDto(user));
    }

    @Override
    public UserDto registerHelpSeeker(UserDto userDto) {
        User user = mapToEntity(userDto);

        Role roles = roleRepository.findByName("ROLE_HELP-SEEKER");
        user.setRoles(Collections.singleton(roles));

        User helpSeeker = userRepository.save(user);

        return mapToDto(helpSeeker);
    }

    @Override
    public UserDto registerVolunteer(UserDto userDto) {
        User user = mapToEntity(userDto);

        Role roles = roleRepository.findByName("ROLE_VOLUNTEER");
        user.setRoles(Collections.singleton(roles));

        User volunteer = userRepository.save(user);

        return mapToDto(volunteer);
    }

    @Override
    public UserDto registerAdmin(UserDto userDto) {
        User user = mapToEntity(userDto);

        Role roles = roleRepository.findByName("ROLE_ADMIN");
        user.setRoles(Collections.singleton(roles));

        User admin = userRepository.save(user);

        return mapToDto(admin);
    }

    @Override
    public User findUserByEmail(String email) {
        if (!userRepository.existsByEmail(email))
            throw new ResourceNotFoundException("Het e-mail bestaat niet");
        return userRepository.findByEmail(email);
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException("Gebruiker", "id", userId));

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setAccountId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(passwordEncoder.encode(user.getPassword()));
        userDto.setEmail(user.getEmail());

        return userDto;
//        return mapper.map(user, UserDto.class);
    }

    public GetUsersDto mapToDto2(User user) {
        GetUsersDto getUsersDto = new GetUsersDto();

        getUsersDto.setId(user.getId());
        getUsersDto.setUsername(user.getUsername());
        getUsersDto.setEmail(user.getEmail());

        return getUsersDto;
    }

    public User mapToEntity(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setId(userDto.getAccountId());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        return user;

//        return mapper.map(userDto, User.class);
    }

}

