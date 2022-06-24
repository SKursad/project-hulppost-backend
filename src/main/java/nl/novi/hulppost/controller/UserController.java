package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.GetUsersDto;
import nl.novi.hulppost.dto.UserDto;
import nl.novi.hulppost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/hulppost/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<GetUsersDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@Valid @PathVariable(value = "userId") Long userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "userId") Long userId,
                                              @Valid @RequestBody UserDto userDto) {
        return userService.getUserById(userId)
                .map(savedUser -> {
                    UserDto updatedUser = userService.updateUser(userDto, userId);
                    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("Gebruiker succesvol verwijderd ", HttpStatus.OK);
    }

}
