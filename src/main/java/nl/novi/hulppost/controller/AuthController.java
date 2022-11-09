package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.UserDTO;
import nl.novi.hulppost.dto.RegistrationDTO;
import nl.novi.hulppost.payload.Password;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.payload.JWTAuthResponse;
import nl.novi.hulppost.security.JwtTokenProvider;
import nl.novi.hulppost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

//    @CrossOrigin("http://localhost:3000/")
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody RegistrationDTO userDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDto.getUsername(), userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @GetMapping("/{username}")
    public ResponseEntity <UserDTO> getUserByUsername(@Valid @PathVariable(value = "username") String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @PostMapping("/registration/helpSeeker")
    public ResponseEntity<RegistrationDTO> registerHelpSeeker(@Valid @RequestBody RegistrationDTO registrationDTO) {
        return new ResponseEntity<>(userService.registerHelpSeeker(registrationDTO), HttpStatus.CREATED);
    }


    @PostMapping("/registration/volunteer")
    public ResponseEntity<RegistrationDTO> registerVolunteer(@Valid @RequestBody RegistrationDTO registrationDTO) {
        return new ResponseEntity<>(userService.registerVolunteer(registrationDTO), HttpStatus.CREATED);
    }


    @PostMapping("/registration/admin")
    public ResponseEntity<RegistrationDTO> registerAdmin(@Valid @RequestBody RegistrationDTO registrationDTO) {
        return new ResponseEntity<>(userService.registerAdmin(registrationDTO), HttpStatus.CREATED);
    }

    @PostMapping("/changePassword")
    public ResponseEntity <String> changePassword(@Valid @RequestBody Password password){
        User user = userService.findUserByEmail(password.getEmail());
        if(!userService.checkIfValidOldPassword(user,password.getOldPassword())) {
            return new ResponseEntity<>("Wachtwoorden komen niet overeen", HttpStatus.UNAUTHORIZED);
        }
        userService.changePassword(user,password.getNewPassword());
        return new ResponseEntity<>("Wachtwoord succesvol gewijzigd", HttpStatus.OK);
    }

}
