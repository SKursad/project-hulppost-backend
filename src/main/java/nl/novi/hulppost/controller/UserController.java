package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.UserDTO;
import nl.novi.hulppost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hulppost/users")
public class UserController {

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


    @PutMapping("/{userId}")
    @PreAuthorize("@methodLevelSecurityService.isAuthorizedUser(#userId, principal)")
    public ResponseEntity<UserDTO> updateUser(@PathVariable (value = "userId") Long userId,
                                              @Valid @RequestBody(required = false) UserDTO userDTO) {
        return userService.getUserById(userId)
                .map(savedUser -> {
                    UserDTO updatedUser = userService.updateUser(userDTO, userId);
                    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@methodLevelSecurityService.isAuthorizedUser(#userId, principal)")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("Gebruiker succesvol verwijderd ", HttpStatus.OK);
    }

}
