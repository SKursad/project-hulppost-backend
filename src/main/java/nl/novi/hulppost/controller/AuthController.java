package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.UserDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auths")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDto.getUsername(), userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("/registration/helpSeeker")
    public ResponseEntity<UserDto> registerHelpSeeker(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.registerHelpSeeker(userDto), HttpStatus.CREATED);
    }


    @PostMapping("/registration/volunteer")
    public ResponseEntity<UserDto> registerVolunteer(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.registerVolunteer(userDto), HttpStatus.CREATED);
    }


    @PostMapping("/registration/admin")
    public ResponseEntity<UserDto> registerAdmin(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.registerAdmin(userDto), HttpStatus.CREATED);
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid @RequestBody Password password){
        User user = userService.findUserByEmail(password.getEmail());
        if(!userService.checkIfValidOldPassword(user,password.getOldPassword())) {
            return "Wachtwoorden komen niet overeen";
        }
        userService.changePassword(user,password.getNewPassword());
        return "Wachtwoord succesvol gewijzigd";
    }

}
