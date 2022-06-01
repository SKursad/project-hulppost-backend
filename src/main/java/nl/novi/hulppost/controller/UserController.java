package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.UserDto;
import nl.novi.hulppost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/hulppost/gebruikers")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@Valid @PathVariable(value = "userId") Long userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.CREATED);
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
