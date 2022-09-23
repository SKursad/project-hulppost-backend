package nl.novi.hulppost.service.serviceImpl;


import nl.novi.hulppost.dto.RegistrationDTO;
import nl.novi.hulppost.dto.UserDTO;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.model.Role;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.AccountRepository;
import nl.novi.hulppost.repository.RoleRepository;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.service.FileService;
import nl.novi.hulppost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    FileService fileService;

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, FileService fileService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileService = fileService;
    }

    @Override
    public Optional<UserDTO> getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Gebruiker niet gevonden"));

        return Optional.of(mapToDto2(user));
    }

    @Override
    public List<UserDTO> getUsersWithParam(Optional<Long> requestId, Optional<Long> replyId) {
        List<User> users;
        List<UserDTO> userDtoList = new ArrayList<>();
        if (requestId.isPresent() && replyId.isPresent()) {
            users = userRepository.findByRequestIdAndReplyId(requestId.get(), replyId.get());
        } else if (requestId.isPresent()) {
            users = userRepository.findByRequestId(requestId.get());
        } else if (replyId.isPresent()) {
            users = userRepository.findByReplyId(replyId.get());
        } else
            users = userRepository.findAll();

        for (User user : users) {
            UserDTO userDto = mapToDto2(user);
            userDtoList.add(userDto);
        }

        return userDtoList;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return (mapToDto2(user));
    }

    @Override
    public RegistrationDTO registerHelpSeeker(RegistrationDTO registrationDTO) {
        User user = mapRegistrationToUser(registrationDTO);
        Account account = mapRegistrationToAccount(registrationDTO);

        Role roles = roleRepository.findByName("ROLE_HELP-SEEKER");
        user.setRoles(Collections.singleton(roles));

        User helpSeeker = userRepository.save(user);

        Account savedAccount = accountRepository.save(account);

        return mapToRegistrationReplyDto(helpSeeker, savedAccount);
    }

    @Override
    public RegistrationDTO registerVolunteer(RegistrationDTO registrationDTO) {
        User user = mapRegistrationToUser(registrationDTO);
        Account account = mapRegistrationToAccount(registrationDTO);

        Role roles = roleRepository.findByName("ROLE_VOLUNTEER");
        user.setRoles(Collections.singleton(roles));

        User volunteer = userRepository.save(user);

        Account savedAccount = accountRepository.save(account);

        return mapToRegistrationReplyDto(volunteer, savedAccount);
    }


    @Override
    public RegistrationDTO registerAdmin(RegistrationDTO registrationDTO) {
        User user = mapRegistrationToUser(registrationDTO);
        Account account = mapRegistrationToAccount(registrationDTO);

        Role roles = roleRepository.findByName("ROLE_ADMIN");
        user.setRoles(Collections.singleton(roles));

        User admin = userRepository.save(user);

        Account savedAccount = accountRepository.save(account);

        return mapToRegistrationReplyDto(admin, savedAccount);
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
    public UserDTO updateUser(UserDTO userDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException("Gebruiker", "id", userId));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        if(userDTO.getImage() != null) {
            String savedImageName;
            try {
                savedImageName = fileService.saveProfileImage(userDTO.getImage());
                fileService.deleteProfileImage(user.getImage());
                user.setImage(savedImageName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        User updatedUser = userRepository.save(user);

        return mapToDto2(updatedUser);
    }


    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserDTO mapToDto2(User user) {
        UserDTO userDto = new UserDTO();

        userDto.setId(user.getId());
        userDto.setReplyId(user.getReply().stream().count());
        userDto.setRequestId(user.getRequest().stream().count());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setImage(user.getImage());

        return userDto;
    }

    public RegistrationDTO mapToRegistrationReplyDto(User user, Account account) {
        RegistrationDTO registrationDTO = new RegistrationDTO();

        registrationDTO.setId(user.getId());
        registrationDTO.setUsername(user.getUsername());
        registrationDTO.setPassword(passwordEncoder.encode(user.getPassword()));
        registrationDTO.setEmail(user.getEmail());
        registrationDTO.setFirstName(account.getFirstName());
        registrationDTO.setSurname(account.getSurname());
        registrationDTO.setBirthday(account.getBirthday());
        registrationDTO.setGender(account.getGender());
        registrationDTO.setZipCode(account.getZipCode());

        return registrationDTO;

    }

    public User mapRegistrationToUser(RegistrationDTO registrationDTO) {
        User user = new User();

        user.setId(registrationDTO.getId());
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        return user;
    }

    public Account mapRegistrationToAccount(RegistrationDTO registrationDTO) {
        Account account = new Account();

        account.setId(registrationDTO.getId());
        account.setFirstName(registrationDTO.getFirstName());
        account.setSurname(registrationDTO.getSurname());
        account.setBirthday(registrationDTO.getBirthday());
        account.setGender(registrationDTO.getGender());
        account.setZipCode(registrationDTO.getZipCode());

        return account;
    }

}

