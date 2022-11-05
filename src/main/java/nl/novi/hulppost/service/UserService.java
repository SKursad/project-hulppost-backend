package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.UserDTO;
import nl.novi.hulppost.dto.RegistrationDTO;
import nl.novi.hulppost.dto.UserImageDTO;
import nl.novi.hulppost.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    //    PageResponse getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);
//    List<UserDto> getAllUsers();
    //    Page<User> findByUsername(String username, Pageable page);
    UserDTO getUserByUsername(String username);

    Optional<UserDTO> getUserById(Long userId);

    List<UserDTO> getUsersWithParam(Optional<Long> requestId, Optional<Long> replyId);

    RegistrationDTO registerHelpSeeker(RegistrationDTO registrationDTO);

    RegistrationDTO registerVolunteer(RegistrationDTO registrationDTO);

    RegistrationDTO registerAdmin(RegistrationDTO registrationDTO);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(User user, String oldPassword);

    UserDTO updateUser(UserDTO userDTO, Long userId);
    UserImageDTO updateUserImage (UserImageDTO userImageDTO, Long userId);

    void deleteUser(Long userId);

    User findUserByEmail(String email);

    void deleteImage(Long userId);

}
