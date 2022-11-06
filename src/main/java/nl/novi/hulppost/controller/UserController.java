package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.RequestDTO;
import nl.novi.hulppost.dto.UserDTO;
import nl.novi.hulppost.dto.UserImageDTO;
import nl.novi.hulppost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {


    //This class sends a request to the interface service layer
    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsersWithParam(@RequestParam Optional<Long> requestId,
                                                           @RequestParam Optional<Long> replyId) {
        return new ResponseEntity<>(userService.getUsersWithParam(requestId, replyId), HttpStatus.OK);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable(value = "userId") Long userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PostMapping("/{userId}")
//    public ResponseEntity<UserImageDTO> saveProfileImage(@PathVariable(value = "userId")
//                                                         @RequestBody @Valid UserImageDTO userImageDTO) {
//        return new ResponseEntity<>(userService.assignImageToUser(userImageDTO), HttpStatus.CREATED);
//    }


    @PutMapping("/{userId}")
//    @PreAuthorize("@methodLevelSecurityService.isAuthorizedUser(#userId, principal)")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "userId") Long userId,
                                              @Valid @RequestBody UserDTO userDTO) {
        return userService.getUserById(userId)
                .map(savedUser -> {
                    UserDTO updatedUser = userService.updateUser(userDTO, userId);
                    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/image")
    @PreAuthorize("@methodLevelSecurityService.isAuthorizedUser(#userId, principal)")
    public ResponseEntity<UserImageDTO> updateUserImage(@PathVariable(value = "userId") Long userId,
                                                        @RequestBody(required = false) UserImageDTO userImageDTO) {
        return userService.getUserById(userId)
                .map(savedUser -> {
                    UserImageDTO updatedImage = userService.updateUserImage(userImageDTO, userId);
                    return new ResponseEntity<>(updatedImage, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{userId}")
//    @PreAuthorize("@methodLevelSecurityService.isAuthorizedUser(#userId, principal)")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("Gebruiker succesvol verwijderd ", HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/deleteImage")
//    @PreAuthorize("@methodLevelSecurityService.isAuthorizedUser(#userId, principal)")
    public ResponseEntity<String> deleteImage(@PathVariable(value = "userId") Long userId) {
        userService.deleteImage(userId);
        return new ResponseEntity<>("Profielfoto succesvol verwijderd ", HttpStatus.OK);
    }

}
